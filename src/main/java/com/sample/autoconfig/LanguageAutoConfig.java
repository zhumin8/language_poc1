package com.sample.autoconfig;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
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

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LanguageServiceClient.class)
@ConditionalOnProperty(value = "spring.cloud.gcp.language.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LanguageProperties.class)
public class LanguageAutoConfig {

  private static final Log LOGGER = LogFactory.getLog(LanguageAutoConfig.class);
  private final LanguageProperties clientProperties;
  private final CredentialsProvider credentialsProvider;
  // private final GcpProjectIdProvider quotaProjectIdProvider;

  public LanguageAutoConfig(LanguageProperties properties,
      CredentialsProvider coreCredentialsProvider)
      throws IOException {
    this.clientProperties = properties;
    if (properties.getCredentials().hasKey()) {
      this.credentialsProvider = (CredentialsProvider) new DefaultCredentialsProvider(properties);
    } else {
      this.credentialsProvider = coreCredentialsProvider;
    }

    // GcpProjectIdProvider: looks for property id in "spring.cloud.gcp.projectID" if null,
    // then get default by ServiceOptions.getDefaultProjectId()
    // if (clientProperties.getQuotaProjectId() != null) {
    //   this.quotaProjectIdProvider = (GcpProjectIdProvider) () -> clientProperties.getQuotaProjectId();
    // } else {
    //   this.quotaProjectIdProvider = coreProjectIdProvider;
    // }

  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient() throws IOException {

    LanguageServiceSettings.defaultApiClientHeaderProviderBuilder();

    // LanguageServiceSettings.defaultCredentialsProviderBuilder();
    // LanguageServiceSettings clientSettings =
    LanguageServiceSettings.Builder clientSettingsBuilder =
        LanguageServiceSettings.newBuilder()
        .setCredentialsProvider(this.credentialsProvider)
        // quota project when set, a custom set quota project id takes priority over one detected by credentials:
        // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L170-L176
        // .setQuotaProjectId(this.quotaProjectIdProvider.getProjectId())

        // with this header provider:
        // user-agent: Spring/3.2.1 spring-cloud-gcp-[packagename]/3.2.1;
        // with default, Map<String, String> headersMap = language.getSettings().getHeaderProvider().getHeaders();
        // is empty map.
        .setHeaderProvider(new UserAgentHeaderProvider(this.getClass()));// custom provider class.
// .getStubSettingsBuilder().setCredentialsProvider()
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


    // for each method, set retry settings.
    // is this too much to expose to users?
    // or set a retrysettings bean with defaults -- allow users to override.
    clientSettingsBuilder.annotateTextSettings().getRetrySettings().toBuilder().setTotalTimeout(
        Duration.ofMillis(600000L));



    // // -------------------
    // // testings and try-outs
    // RetrySettings retrySettings = ServiceOptions.getDefaultRetrySettings();
    // retrySettings.getTotalTimeout();
    // ServiceOptions.getDefaultProjectId();
    // // ServiceOptions.getDefaultProjectId();
    // LanguageServiceStubSettings.Builder languageServiceSettingsBuilder =
    //     LanguageServiceStubSettings.newBuilder();
    //
    // // for each methods
    // languageServiceSettingsBuilder
    //     .analyzeSentimentSettings()
    //     .getRetrySettings()
    //     .toBuilder()
    //     .setTotalTimeout(retrySettings.getTotalTimeout())
    //     // .setTotalTimeout(Duration.ofSeconds(30))
    //     .build();
    // languageServiceSettingsBuilder
    //     .analyzeSentimentSettings()
    //     .setRetrySettings(retrySettings);
    // languageServiceSettingsBuilder.annotateTextSettings().getRetrySettings().toBuilder().setTotalTimeout(retrySettings.getTotalTimeout());
    //
    // languageServiceSettingsBuilder.unaryMethodSettingsBuilders().forEach(
    //     builder -> {
    //       Set<Code> retryableCodes = builder.getRetryableCodes();
    //     }
    // );


    // // // languageServiceSettingsBuilder.analyzeSyntaxSettings().setRetryableCodes();
    // // RetrySettings retrySettings = languageServiceSettingsBuilder
    // //     .analyzeSentimentSettings()
    // //     .getRetrySettings()
    // //     .toBuilder()
    // //     .setTotalTimeout(Duration.ofSeconds(30))
    // //     .build();
    // // // ApiFunction<UnaryCallSettings.Builder<?, ?>, Void> settingsUpdater
    // // RetrySettings.Builder unaryCallSettings= UnaryCallSettings.newUnaryCallSettingsBuilder().getRetrySettings().toBuilder().setTotalTimeout(Duration.ofSeconds(30));
    // // // ApiFunction<UnaryCallSettings.Builder<?, ?>, Void> settingsUpdater = () -> unaryCallSettings;
    // // languageServiceSettingsBuilder.applyToAllUnaryMethods();
    // LanguageServiceStubSettings languageServiceSettings = languageServiceSettingsBuilder.build();
    //
    //
    // LanguageServiceSettings settings = LanguageServiceSettings.create(
    //     languageServiceSettings).toBuilder()
    //     .setCredentialsProvider(this.credentialsProvider)
    //     .setQuotaProjectId(this.projectIdProvider.getProjectId())
    //     .setBackgroundExecutorProvider(provider)
    //     .build();
    // // UnaryCallSettings<AnalyzeEntitiesRequest, AnalyzeEntitiesResponse> unaryCallSettings = clientSettings.analyzeEntitiesSettings();


    // -------------------


    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }
}
