package com.sample.autoconfig;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
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

/**
 * Create client with client library default setting, except:
 * - set credentialsProvider, so user/service account/compute engine key can be intake from spring
 * property "spring.cloud.gcp.credentials.location/encoded-key"
 * - set HeaderProvider for internal metrics
 * - set optional quotaProjectId, this overrides projectId obtained from credentials when present
 * - set ExecutorThreadCount when present, sets to client library default if not specified (not tested yet)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LanguageServiceClient.class)
@ConditionalOnProperty(value = "spring.cloud.gcp.language.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LanguageProperties.class)
public class LanguageAutoConfig {

  private static final Log LOGGER = LogFactory.getLog(LanguageAutoConfig.class);
  private final LanguageProperties clientProperties;

  public LanguageAutoConfig(LanguageProperties properties){
    this.clientProperties = properties;
  }


  // benefit of configuring as a separate bean: could be easier to override if needed?
  @Bean
  @ConditionalOnMissingBean
  public CredentialsProvider googleCredentials() throws IOException {
    return new DefaultCredentialsProvider(this.clientProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient(CredentialsProvider credentialsProvider) throws IOException {

    LanguageServiceSettings.Builder clientSettingsBuilder =
        LanguageServiceSettings.newBuilder()
        .setCredentialsProvider(credentialsProvider)
        // with this header provider:
        // user-agent: Spring-autoconfig//3.2.1 spring-cloud-autogen-config-[packagename]/3.2.1;
        // with default, Map<String, String> headersMap = language.getSettings().getHeaderProvider().getHeaders();
        // is empty map.
        .setHeaderProvider(new UserAgentHeaderProvider(this.getClass()));// custom provider class.
            // .setEndpoint("language.googleapis.com:443")

    // this only looks in "spring.cloud.gcp.language.quotaProjectId", if null just leave empty
    // client lib will look for ADC projectId. "spring.cloud.gcp.project-id" is not used.
    // quota project when set, a custom set quota project id takes priority over one detected by credentials:
    // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L170-L176
    if (clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(clientProperties.getQuotaProjectId());
    }

    if (clientProperties.getExecutorThreadCount() != null) {

      ExecutorProvider executorProvider = LanguageServiceSettings.defaultExecutorProviderBuilder()
          .setExecutorThreadCount(clientProperties.getExecutorThreadCount()).build();
      // TransportChannelProvider transportChannelProvider = LanguageServiceStubSettings.defaultTransportChannelProvider()
      //     .withExecutor((Executor) executorProvider);
      clientSettingsBuilder
        .setBackgroundExecutorProvider(executorProvider);
      // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientSettings.java#L160-L176
      // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L178-L184
      // .setTransportChannelProvider(transportChannelProvider)
    }


    // // for each method, set retry settings.
    // // - is this too much to expose to users?
    // // - or set a retrysettings bean with defaults -- allow users to override.
    // // Do not set retry settings, because each method
    // // has own set of default retry settings/ or no retry. Too much lower level details to expose.
    // clientSettingsBuilder.annotateTextSettings().getRetrySettings().toBuilder().setTotalTimeout(
    //     Duration.ofMillis(600000L));



    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }
}
