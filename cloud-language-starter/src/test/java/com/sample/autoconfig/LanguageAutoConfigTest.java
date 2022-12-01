package com.sample.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

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
  void testServiceClientCreated() {
    try (ConfigurableApplicationContext c = applicationBuilder.run()) {
      LanguageServiceClient client = c.getBean(LanguageServiceClient.class);
      assertThat(client).isNotNull();
    }
  }

    @Test
    void customCredentialsProviderUsedWhenProvided() {
        CredentialsProvider customCredentialsProvider = mock(CredentialsProvider.class);
        contextRunner
                .withBean("languageServiceCredentials", CredentialsProvider.class, () -> customCredentialsProvider)
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings().getCredentialsProvider()).isSameAs(customCredentialsProvider);
                        });
    }

    // TODO: fix
    @Test
    void customTransportChannelProviderUsedWhenProvided() throws IOException {
        TransportChannelProvider customTransportChannelProvider = mock(TransportChannelProvider.class);
        //        when(customTransportChannelProvider.getTransportName()).thenReturn("grpc");
        contextRunner
                .withBean("defaultLanguageTransportChannelProvider", TransportChannelProvider.class, () -> customTransportChannelProvider)
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings().getTransportChannelProvider()).isSameAs(customTransportChannelProvider);
                        });
    }

    // TODO: fix
    @Test
    void customServiceSettingsUsedWhenProvided() {
        LanguageServiceSettings customLanguageServiceSettings = mock(LanguageServiceSettings.class);
        contextRunner
                .withBean("languageServiceSettings", LanguageServiceSettings.class, () -> customLanguageServiceSettings)
                .run(
                        ctx -> {
                            LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                            assertThat(client.getSettings()).isSameAs(customLanguageServiceSettings);
                        });
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

}