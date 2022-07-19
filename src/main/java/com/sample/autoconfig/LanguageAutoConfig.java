package com.sample.autoconfig;

import com.google.api.core.ApiFunction;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.cloud.ServiceOptions;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.stub.LanguageServiceStubSettings;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.spring.core.UserAgentHeaderProvider;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executor;
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
// @Repeatable(Schedules.class)
public class LanguageAutoConfig {

  private final LanguageProperties clientProperties;
  private final CredentialsProvider credentialsProvider;
  private final GcpProjectIdProvider projectIdProvider;

  public LanguageAutoConfig(LanguageProperties properties,
      CredentialsProvider coreCredentialsProvider,
      GcpProjectIdProvider coreProjectIdProvider)
      throws IOException {
    this.clientProperties = properties;
    if (properties.getCredentials().hasKey()) {
      this.credentialsProvider = (CredentialsProvider) new DefaultCredentialsProvider(properties);
    } else {
      this.credentialsProvider = coreCredentialsProvider;
    }

    if (clientProperties.getProjectId() != null) {
      this.projectIdProvider = (GcpProjectIdProvider) () -> clientProperties.getProjectId();
    } else {
      this.projectIdProvider = coreProjectIdProvider;
    }
    Object aClass = LanguageServiceClient.class;
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient() throws IOException {
    ExecutorProvider executorProvider = LanguageServiceSettings.defaultExecutorProviderBuilder()
        .setExecutorThreadCount(10).build();
    // TransportChannelProvider transportChannelProvider = LanguageServiceStubSettings.defaultTransportChannelProvider()
    //     .withExecutor((Executor) executorProvider);

    LanguageServiceSettings.defaultApiClientHeaderProviderBuilder();

    LanguageServiceSettings clientSettings =
        LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(this.credentialsProvider)
            .setQuotaProjectId(this.projectIdProvider.getProjectId())
            .setBackgroundExecutorProvider(executorProvider)
            // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientSettings.java#L160-L176
            // https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/rpc/ClientContext.java#L178-L184
            // .setTransportChannelProvider(transportChannelProvider)

            // with this header provider:
            // user-agent: Spring/3.2.1 spring-cloud-gcp-demo/3.2.1;
            // with default, Map<String, String> headersMap = language.getSettings().getHeaderProvider().getHeaders();
            // is empty map.
            .setHeaderProvider(new UserAgentHeaderProvider(this.getClass())) // change header
            // .getStubSettingsBuilder().setCredentialsProvider()
            // .setEndpoint("language.googleapis.com:443")
        .build();

    // -------------------
    // testings and try-outs
    RetrySettings retrySettings = ServiceOptions.getDefaultRetrySettings();
    retrySettings.getTotalTimeout();
    ServiceOptions.getDefaultProjectId();
    // ServiceOptions.getDefaultProjectId();
    LanguageServiceStubSettings.Builder languageServiceSettingsBuilder =
        LanguageServiceStubSettings.newBuilder();

    // for each methods
    languageServiceSettingsBuilder
        .analyzeSentimentSettings()
        .getRetrySettings()
        .toBuilder()
        .setTotalTimeout(retrySettings.getTotalTimeout())
        // .setTotalTimeout(Duration.ofSeconds(30))
        .build();
    languageServiceSettingsBuilder
        .analyzeSentimentSettings()
        .setRetrySettings(retrySettings);
    languageServiceSettingsBuilder.annotateTextSettings().getRetrySettings().toBuilder().setTotalTimeout(retrySettings.getTotalTimeout());

    languageServiceSettingsBuilder.unaryMethodSettingsBuilders().forEach(
        builder -> {
          Set<Code> retryableCodes = builder.getRetryableCodes();
        }
    );
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


    return LanguageServiceClient.create(clientSettings);
  }
}
