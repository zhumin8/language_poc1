package com.sample.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.threeten.bp.Duration;

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
    void testServiceRetrySettingsFromProperties() {
        this.contextRunner
                .withPropertyValues(
                        "spring.cloud.gcp.language.language-service.enabled=true",
                        "spring.cloud.gcp.language.language-service.retry-settings.retry-delay-multiplier=2",
                        "spring.cloud.gcp.language.language-service.retry-settings.max-retry-delay=PT0.9S"
                )
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);

                            RetrySettings analyzeSentimentRetrySettings =
                                    client.getSettings().analyzeSentimentSettings().getRetrySettings();
                            assertThat(analyzeSentimentRetrySettings.getRetryDelayMultiplier()).isEqualTo(2);
                            assertThat(analyzeSentimentRetrySettings.getMaxRetryDelay()).isEqualTo(Duration.ofMillis(900));
                            // if properties only override certain retry settings, others should still take on client library defaults
                            assertThat(analyzeSentimentRetrySettings.getInitialRetryDelay()).isEqualTo(Duration.ofMillis(100)); // default
                        });
    }

    @Test
    void testMethodRetrySettingsFromProperties() {
        this.contextRunner
                .withPropertyValues(
                        "spring.cloud.gcp.language.language-service.enabled=true",
                        "spring.cloud.gcp.language.language-service.retry-settings.retry-delay-multiplier=2",
                        "spring.cloud.gcp.language.language-service.retry-settings.max-retry-delay=PT0.9S",
                        "spring.cloud.gcp.language.language-service.annotate-text-retry-settings.retry-delay-multiplier=3"
                )
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);

                            RetrySettings annotateTextRetrySettings =
                                    client.getSettings().annotateTextSettings().getRetrySettings();
                            // Method-level override should take precedence over service-level
                            assertThat(annotateTextRetrySettings.getRetryDelayMultiplier()).isEqualTo(3);
                            // For settings without method-level overrides but when service-level is provided, fall back to that
                            assertThat(annotateTextRetrySettings.getMaxRetryDelay()).isEqualTo(Duration.ofMillis(900));
                            // Settings with neither method not service-level overrides should still take on client library defaults
                            assertThat(annotateTextRetrySettings.getInitialRetryDelay()).isEqualTo(Duration.ofMillis(100)); // default
                        });
    }
}