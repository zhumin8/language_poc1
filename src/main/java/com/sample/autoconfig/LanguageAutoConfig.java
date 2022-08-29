package com.sample.autoconfig;
// TODO: package name needs to end with a service-specific suffix if we want to use the UserAgentHeaderProvider

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Create client with client library default setting, except: - set credentialsProvider, so
 * user/service account/compute engine key can be intake from spring property
 * "spring.cloud.gcp.credentials.location/encoded-key" - set HeaderProvider for internal metrics -
 * set optional quotaProjectId, this overrides projectId obtained from credentials when present -
 * set ExecutorThreadCount when present, sets to client library default if not specified (not tested
 * yet)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LanguageServiceClient.class)
@ConditionalOnProperty(value = "spring.cloud.gcp.language.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LanguageProperties.class)
public class LanguageAutoConfig {

  private static final Log LOGGER = LogFactory.getLog(LanguageAutoConfig.class);
  private final LanguageProperties clientProperties;


  // // retry param map for defaults. Not needed directly. For reference here only.
  // // clear out after settings done.
  // private static final ImmutableMap<String, RetrySettings> RETRY_PARAM_DEFINITIONS;
  //
  // static {
  //   ImmutableMap.Builder<String, RetrySettings> definitions = ImmutableMap.builder();
  //   RetrySettings settings = null;
  //   settings =
  //       RetrySettings.newBuilder()
  //           .setInitialRetryDelay(Duration.ofMillis(100L))
  //           .setRetryDelayMultiplier(1.3)
  //           .setMaxRetryDelay(Duration.ofMillis(60000L))
  //           .setInitialRpcTimeout(Duration.ofMillis(600000L))
  //           .setRpcTimeoutMultiplier(1.0)
  //           .setMaxRpcTimeout(Duration.ofMillis(600000L))
  //           .setTotalTimeout(Duration.ofMillis(600000L))
  //           .build();
  //   definitions.put("retry_policy_0_params", settings);
  //   RETRY_PARAM_DEFINITIONS = definitions.build();
  // }
  // // end of not needed block


  public LanguageAutoConfig(LanguageProperties properties) {
    this.clientProperties = properties;
  }


  // benefit of configuring as a separate bean: could be easier to override if needed?
  @Bean
  @ConditionalOnMissingBean (name="googleLanguageCredentials")  // TODO: check which way is correct -- name vs qualifier
  @Qualifier ("googleLanguageCredentials")
  public CredentialsProvider googleLanguageCredentials() throws IOException {
    return new DefaultCredentialsProvider(this.clientProperties);
  }

  // include service name in the bean name to avoid conflict.
  // example of conflict:
  @Bean
  @ConditionalOnMissingBean
  public TransportChannelProvider defaultLanguageTransportChannelProvider() {
    return LanguageServiceSettings.defaultTransportChannelProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient(@Qualifier("googleLanguageCredentials") CredentialsProvider credentialsProvider,
      TransportChannelProvider defaultTransportChannelProvider)
      throws IOException {

    LanguageServiceSettings.Builder clientSettingsBuilder =
        LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(credentialsProvider)
            // default transport channel provider, allow user to override bean for example to configure a proxy
            // https://github.com/googleapis/google-cloud-java#configuring-a-proxy
            .setTransportChannelProvider(defaultTransportChannelProvider)
            // with this header provider:
            // user-agent: Spring-autoconfig//3.2.1 spring-cloud-autogen-config-[packagename]/3.2.1;
            // with default, Map<String, String> headersMap = language.getSettings().getHeaderProvider().getHeaders();
            // is empty map.
            .setHeaderProvider(computeUserAgentString());// custom provider class.
    // .setEndpoint("language.googleapis.com:443")

    // this only looks in "spring.cloud.gcp.language.quotaProjectId", if null just leave empty
    // client lib will look for ADC projectId. "spring.cloud.gcp.project-id" is not used.
    // quota project when set, a custom set quota project id takes priority over one detected by credentials:
    // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L170-L176
    if (clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(clientProperties.getQuotaProjectId());
      LOGGER.info("Quota project id set to: " + clientProperties.getQuotaProjectId()
          + ", this overrides project id from credentials.");
    }

    if (clientProperties.getExecutorThreadCount() != null) {

      ExecutorProvider executorProvider = LanguageServiceSettings.defaultExecutorProviderBuilder()
          .setExecutorThreadCount(clientProperties.getExecutorThreadCount()).build();
      clientSettingsBuilder
          .setBackgroundExecutorProvider(executorProvider);
    }

    // To use REST (HTTP1.1/JSON) transport (instead of gRPC) for sending and receiving requests over
    // can set a property to enable this if specified, but seems too niche usecase?
    if (clientProperties.isUseRest()) {
      clientSettingsBuilder.setTransportChannelProvider(
          LanguageServiceSettings.defaultHttpJsonTransportProviderBuilder().build());
    }

    // Retry Settings: set for each method.
    // Useful settings for users. should expose settings for each method.
    // If property not set, set to default -- need to access from gapic-context for each method.

    // for relevant retry settings, follow logic in:
    // com.google.api.generator.gapic.composer.common.RetrySettingsComposer.createRetrySettingsExprs
    // Defaults already set in LanguageProperties. So just set to property values here.
    RetrySettings annotateTextSettingsRetrySettings = clientSettingsBuilder.annotateTextSettings()
        .getRetrySettings()
        .toBuilder()
            // we either need to make sure client library gets generated together with Spring autoconfig, OR to avoid setting defaults (only set value if user provided).
        .setInitialRetryDelay(this.clientProperties.getAnnotateTextMaxRetryDelay())
        .setRetryDelayMultiplier(this.clientProperties.getAnnotateTextRpcTimeoutMultiplier())
        .setMaxRetryDelay(this.clientProperties.getAnnotateTextMaxRetryDelay())
        .setInitialRpcTimeout(this.clientProperties.getAnnotateTextInitialRpcTimeout())
        .setRpcTimeoutMultiplier(this.clientProperties.getAnnotateTextRpcTimeoutMultiplier())
        .setMaxRpcTimeout(this.clientProperties.getAnnotateTextMaxRpcTimeout())
        .setTotalTimeout(this.clientProperties.getAnnotateTextTotalTimeout())
        .build();
    clientSettingsBuilder.annotateTextSettings()
        .setRetrySettings(annotateTextSettingsRetrySettings);
    // as sample, only set for one method, in real code, should set for all applicable methods.

    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }

  private HeaderProvider userAgentHeaderProvider() {
    String springLibrary = "spring-cloud-autogen-config-language";
    String version = this.getClass().getPackage().getImplementationVersion();

    // see concord tools.yaml google3/cloud/analysis/concord/configs/api/attribution-prod/tools.yaml?rcl=469347651&l=428
    return () -> Collections.singletonMap("user-agent", "Spring-autoconfig/" + version + " " + springLibrary + "/" + version);
  }
}
