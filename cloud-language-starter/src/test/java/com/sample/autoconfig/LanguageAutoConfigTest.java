package com.sample.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.threeten.bp.Duration;

@ExtendWith(MockitoExtension.class)
class LanguageAutoConfigTest {

  private static final String SERVICE_CREDENTIAL_LOCATION = "src/test/resources/fake-credential-key.json";
  private static final String SERVICE_CREDENTIAL_CLIENT_ID = "45678";
  private static final String SERVICE_CREDENTIAL_LOCATION_2 = "src/test/resources/fake-credential-key-2.json";
  private ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(LanguageAutoConfig.class));

  private SpringApplicationBuilder applicationBuilder =
      new SpringApplicationBuilder(LanguageAutoConfig.class)
          .properties(
              "spring.cloud.gcp.language.language-service.enabled=true")
          .web(WebApplicationType.NONE);

  @Mock
  private RetrySettings mockAnnotateTextRetrySettings;
  @Mock
  private RetrySettings.Builder mockAnnotateTextRetrySettingsBuilder;


  @Test
  void testKeyManagementClientCreated() {
    try (ConfigurableApplicationContext c = applicationBuilder.run()) {
      LanguageServiceClient client = c.getBean(LanguageServiceClient.class);
      assertThat(client).isNotNull();
    }
  }

  @Test
  void testShouldTakeCoreCredentials() {
    this.contextRunner
        .withPropertyValues(
            "spring.auto.shared.credentials.location=file:" + SERVICE_CREDENTIAL_LOCATION_2)
        .run(ctx -> {
          LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
          Credentials credentials = client.getSettings().getCredentialsProvider().getCredentials();
          assertThat(((ServiceAccountCredentials) credentials).getClientId()).isEqualTo(
              "12345");
        });
  }

  @Test
  void testShouldTakeServiceCredentials() {
    this.contextRunner
        .withPropertyValues(
            "spring.cloud.gcp.language.language-service.credentials.location=file:" + SERVICE_CREDENTIAL_LOCATION)
        .run(ctx -> {
          LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
          Credentials credentials = client.getSettings().getCredentialsProvider().getCredentials();
          assertThat(((ServiceAccountCredentials) credentials).getClientId()).isEqualTo(
              SERVICE_CREDENTIAL_CLIENT_ID);
        });
  }

    @Test
    void defaultAnnotateTextRetrySettingsUsed() {
        contextRunner
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings().annotateTextSettings().getRetrySettings().getRetryDelayMultiplier()).isEqualTo(1.3);
                        });
    }

    @Test
    void customAnnotateTextRetrySettingsUsedWhenProvided() {
      when(mockAnnotateTextRetrySettings.toBuilder()).thenReturn(mockAnnotateTextRetrySettingsBuilder);
      when(mockAnnotateTextRetrySettingsBuilder.build()).thenReturn(mockAnnotateTextRetrySettings);
//      when(mockAnnotateTextRetrySettings.getInitialRetryDelay()).thenReturn(Duration.ofMillis(100L));
//      when(mockAnnotateTextRetrySettings.getMaxRetryDelay()).thenReturn(Duration.ofMillis(100L));
//      when(mockAnnotateTextRetrySettings.getRetryDelayMultiplier()).thenReturn(2.0);
//      when(mockAnnotateTextRetrySettings.getInitialRpcTimeout()).thenReturn(Duration.ofMillis(100L));
//      when(mockAnnotateTextRetrySettings.getMaxRpcTimeout()).thenReturn(Duration.ofMillis(100L));
//      when(mockAnnotateTextRetrySettings.getRpcTimeoutMultiplier()).thenReturn(2.0);
      when(mockAnnotateTextRetrySettings.getTotalTimeout()).thenReturn(Duration.ofMillis(100L));

        contextRunner
                .withBean("annotateTextRetrySettings", RetrySettings.class, () -> mockAnnotateTextRetrySettings)
//                .withBean("analyzeSentimentRetrySettings", RetrySettings.class, () -> mockAnalyzeSentimentRetrySettings)
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings().annotateTextSettings().getRetrySettings()).isSameAs(mockAnnotateTextRetrySettings);
                            assertThat(client.getSettings().annotateTextSettings().getRetrySettings().getTotalTimeout()).isEqualTo(Duration.ofMillis(100L));
                            // TODO: How to achieve granularity of: if bean only overrides certain retry settings, then the others still take on client library defaults?
                            // assertThat(client.getSettings().annotateTextSettings().getRetrySettings().getRetryDelayMultiplier()).isEqualTo(1.3);
                        });
    }

}