package com.sample.autoconfig;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

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

  public LanguageAutoConfig(LanguageProperties properties) {
    this.clientProperties = properties;
  }


  // benefit of configuring as a separate bean: could be easier to override if needed?
  @Bean
  @ConditionalOnMissingBean
  public CredentialsProvider googleCredentials() throws IOException {
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
  public LanguageServiceClient languageServiceClient(CredentialsProvider credentialsProvider,
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
            .setHeaderProvider(
                new UserAgentHeaderProvider(this.getClass()));// custom provider class.
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

    // for each method, set retry settings.
    // Useful settings for users. should expose settings for each method.
    // If property not set, set to default -- need to access from gapic-context for each method.

    // TODO: check gapic lib, if code no retry has this?
    RetrySettings annotateTextSettingsRetrySettings = clientSettingsBuilder.annotateTextSettings().getRetrySettings()
        .toBuilder()
        .setInitialRetryDelay(this.clientProperties.getAnnotateTextInitialRetryDelay() == null ? Duration.ofMillis(100L): this.clientProperties.getAnnotateTextMaxRetryDelay())
        .setRetryDelayMultiplier(this.clientProperties.getAnnotateTextRetryDelayMultiplier() == null ? 1.3 : this.clientProperties.getAnnotateTextRpcTimeoutMultiplier())
        .setMaxRetryDelay(this.clientProperties.getAnnotateTextMaxRetryDelay() == null ? Duration.ofMillis(60000L) : this.clientProperties.getAnnotateTextMaxRetryDelay())
        .setInitialRpcTimeout(this.clientProperties.getAnntateTextInitialRpcTimeout() == null ? Duration.ofMillis(600000L) : this.clientProperties.getAnntateTextInitialRpcTimeout())
        .setRpcTimeoutMultiplier(this.clientProperties.getAnnotateTextRpcTimeoutMultiplier() == null ? 1.0 : this.clientProperties.getAnnotateTextRpcTimeoutMultiplier())
        .setMaxRpcTimeout(this.clientProperties.getAnntateTextMaxRpcTimeout() == null ? Duration.ofMillis(600000L) : this.clientProperties.getAnntateTextMaxRpcTimeout())
        .setTotalTimeout(this.clientProperties.getAnntateTextTotalTimeout() == null ? Duration.ofMillis(600000L) : this.clientProperties.getAnntateTextTotalTimeout())
        .build();
    clientSettingsBuilder.annotateTextSettings().setRetrySettings(annotateTextSettingsRetrySettings);
    // as sample, only set for one method, in real code, should set for all applicable methods.

    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }
}
