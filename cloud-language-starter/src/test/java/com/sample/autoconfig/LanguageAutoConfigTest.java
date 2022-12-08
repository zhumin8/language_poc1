package com.sample.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.sample.shared.Retry;
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
    void testRetrySettingsFromProperties() {
        this.contextRunner
                .withPropertyValues(
                        "spring.cloud.gcp.language.language-service.enabled=true",
                        "spring.cloud.gcp.language.language-service.retry.retry-delay-multiplier="
                                + "2",
                        "spring.cloud.gcp.language.language-service.retry.initial-retry-delay="
                                + "PT0.5S"
                )
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);

                            RetrySettings annotateTextRetrySettings =
                                    client.getSettings().annotateTextSettings().getRetrySettings();
                            assertThat(annotateTextRetrySettings.getRetryDelayMultiplier()).isEqualTo(2);
                            assertThat(annotateTextRetrySettings.getInitialRetryDelay()).isEqualTo(Duration.ofMillis(500));
                            assertThat(annotateTextRetrySettings.getMaxRetryDelay()).isEqualTo(Duration.ofMinutes(1)); // default

                            RetrySettings analyzeSentimentRetrySettings =
                                    client.getSettings().analyzeSentimentSettings().getRetrySettings();
                            assertThat(analyzeSentimentRetrySettings.getRetryDelayMultiplier()).isEqualTo(2);
                            assertThat(analyzeSentimentRetrySettings.getInitialRetryDelay()).isEqualTo(Duration.ofMillis(500));
                            assertThat(analyzeSentimentRetrySettings.getMaxRetryDelay()).isEqualTo(Duration.ofMinutes(1)); // default
                        });
    }

    @Test
    void testRetrySettingsFromCustomBean() {
      Retry customRetry = new Retry();
      customRetry.setInitialRetryDelay(java.time.Duration.ofMillis(100L));

//        RetrySettings mockRetrySettings = mock(RetrySettings.class);
//        RetrySettings.Builder mockRetrySettingsBuilder = mock(RetrySettings.Builder.class);
//        when(mockRetrySettings.toBuilder()).thenReturn(mockRetrySettingsBuilder);
//        when(mockRetrySettingsBuilder.build()).thenReturn(mockRetrySettings);
//        when(mockRetrySettings.getInitialRetryDelay()).thenReturn(Duration.ofMillis(100L));

        contextRunner
                .withPropertyValues(
                        "spring.cloud.gcp.language.language-service.enabled=true"
                )
                .withBean("languageRetrySettings", Retry.class, () -> customRetry)
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings().annotateTextSettings().getRetrySettings().getInitialRetryDelay()).isEqualTo(Duration.ofMillis(100L));
                            // if bean only overrides certain retry settings, then the others still take on client library defaults
                            assertThat(client.getSettings().annotateTextSettings().getRetrySettings().getRetryDelayMultiplier()).isEqualTo(1.3);
                        });
    }

}