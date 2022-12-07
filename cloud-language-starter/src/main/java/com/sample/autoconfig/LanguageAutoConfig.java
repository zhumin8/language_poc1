package com.sample.autoconfig; // generated as client-lib-package-name.spring

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Create client with client library default setting, except: - set credentialsProvider, so
 * user/service account/compute engine key can be intake from spring property
 * "spring.cloud.gcp.credentials.location/encoded-key" - set HeaderProvider for internal metrics -
 * set optional quotaProjectId, this overrides projectId obtained from credentials when present -
 * set ExecutorThreadCount when present, sets to client library default if not specified (not tested
 * yet)
 */
@AutoConfiguration // Spring Boot 2.7 new annotation,  is meta-annotated with @Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LanguageServiceClient.class) // When lib has multiple services,
// an antoconfig class per service will be created
@ConditionalOnProperty(value = "spring.cloud.gcp.language.language-service.enabled", matchIfMissing = true)
@EnableConfigurationProperties({LanguageProperties.class})
public class LanguageAutoConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(LanguageAutoConfig.class);
  private final LanguageProperties clientProperties;
  private final CredentialsProvider credentialsProvider;

  public LanguageAutoConfig(LanguageProperties properties,
      CredentialsProvider credentialsProvider) throws IOException {
    this.clientProperties = properties;
    if (this.clientProperties.getCredentials().hasKey()) {
      this.credentialsProvider = new DefaultCredentialsProvider(this.clientProperties);
    } else {
      this.credentialsProvider = credentialsProvider;
    }
  }

  // include service name in the bean name to avoid conflict.
  // example of conflict:
  @Bean
  @ConditionalOnMissingBean(name = "defaultLanguageTransportChannelProvider")
  public TransportChannelProvider defaultLanguageTransportChannelProvider() {
    return LanguageServiceSettings.defaultTransportChannelProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceSettings languageServiceSettings(
      @Qualifier("defaultLanguageTransportChannelProvider") TransportChannelProvider defaultTransportChannelProvider)
      throws IOException {

    LanguageServiceSettings.Builder clientSettingsBuilder =
        LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(this.credentialsProvider)
            // default transport channel provider, allow user to override bean for example to configure a proxy
            // https://github.com/googleapis/google-cloud-java#configuring-a-proxy
            .setTransportChannelProvider(defaultTransportChannelProvider)
            .setHeaderProvider(
                this.userAgentHeaderProvider());// custom provider class.

    // this only looks in "spring.cloud.gcp.language.quotaProjectId", if null just leave empty
    // client lib will look for ADC projectId. "spring.cloud.gcp.project-id" is not used.
    // quota project when set, a custom set quota project id takes priority over one detected by credentials:
    // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L170-L176
    if (this.clientProperties.getQuotaProjectId() != null) {
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

    // safer (in case of version mismatch b/w spring-autoconfig & client lib)
    // to not set defaults in properties, only modify value if property is set.
    RetrySettings.Builder annotateTextRetrySettingsBuilder = clientSettingsBuilder.annotateTextSettings()
        .getRetrySettings()
        .toBuilder();

    if (this.clientProperties.getAnnotateTextInitialRetryDelay() != null) {
      annotateTextRetrySettingsBuilder.setInitialRetryDelay(this.clientProperties.getAnnotateTextInitialRetryDelay());
    }
    if (this.clientProperties.getAnnotateTextRetryDelayMultiplier() != null) {
      annotateTextRetrySettingsBuilder.setRetryDelayMultiplier(this.clientProperties.getAnnotateTextRetryDelayMultiplier());
    }
    // ...
    clientSettingsBuilder.annotateTextSettings()
        .setRetrySettings(annotateTextRetrySettingsBuilder.build());
    return clientSettingsBuilder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient(LanguageServiceSettings languageServiceSettings)
      throws IOException {
    return LanguageServiceClient.create(languageServiceSettings);
  }

  /**
   * Returns the "user-agent" header value which should be added to the google-cloud-java REST API
   * calls. e.g., {@code Spring-autoconfig/1.0.0.RELEASE spring-autogen-language/1.0.0.RELEASE}.
   *
   * @return the user agent string.
   */
  // custom user agent header provider.
  private HeaderProvider userAgentHeaderProvider() {
    // String springLibrary = "spring-cloud-gcp-language";
    String springLibrary = "spring-autogen-language"; // get service name directly
    String version = this.getClass().getPackage().getImplementationVersion(); // META-INF/MANIFEST.MF

    // see concord tools.yaml google3/cloud/analysis/concord/configs/api/attribution-prod/tools.yaml?rcl=469347651&l=428
    return () -> Collections.singletonMap("user-agent", springLibrary + "/" + version);

    // return () -> Collections.singletonMap("user-agent", "Spring/" + version + " " + springLibrary + "/" + version);
  }
}
