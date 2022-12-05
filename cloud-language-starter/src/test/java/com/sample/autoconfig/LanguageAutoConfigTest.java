package com.sample.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class LanguageAutoConfigTest {

  private static final String SERVICE_CREDENTIAL_LOCATION = "src/test/resources/fake-credential-key.json";
  private static final String SERVICE_CREDENTIAL_CLIENT_ID = "45678";
  private static final String SERVICE_CREDENTIAL_LOCATION_2 = "src/test/resources/fake-credential-key-2.json";

  @Mock private TransportChannel mockTransportChannel;
  @Mock private ApiCallContext mockApiCallContext;
  @Mock private TransportChannelProvider mockTransportChannelProvider;
  @Mock private CredentialsProvider mockCredentialsProvider;

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
      contextRunner
              .withBean("languageServiceCredentials", CredentialsProvider.class, () -> mockCredentialsProvider)
              .run(
                      ctx -> {
                          LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                          assertThat(client.getSettings().getCredentialsProvider()).isSameAs(mockCredentialsProvider);
                      });
  }

  @Test
  void customTransportChannelProviderUsedWhenProvided() throws IOException {
      when(mockTransportChannelProvider.getTransportName()).thenReturn("grpc");
      when(mockTransportChannelProvider.getTransportChannel()).thenReturn(mockTransportChannel);
      when(mockTransportChannel.getEmptyCallContext()).thenReturn(mockApiCallContext);
      when(mockApiCallContext.withCredentials(any())).thenReturn(mockApiCallContext);
      when(mockApiCallContext.withTransportChannel(any())).thenReturn(mockApiCallContext);

      contextRunner
              .withBean("defaultLanguageTransportChannelProvider", TransportChannelProvider.class, () -> mockTransportChannelProvider)
              .run(
                      ctx -> {
                          LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                          assertThat(client.getSettings().getTransportChannelProvider()).isSameAs(mockTransportChannelProvider);
                      });
  }

  @Test
  void customServiceSettingsUsedWhenProvided() throws IOException {
      String mockQuotaProjectId = "mockProject";
      LanguageServiceSettings customLanguageServiceSettings =
              LanguageServiceSettings.newBuilder()
                      .setCredentialsProvider(mockCredentialsProvider)
                      .setQuotaProjectId(mockQuotaProjectId)
                      .build();
      contextRunner
              .withBean("languageServiceSettings", LanguageServiceSettings.class, () -> customLanguageServiceSettings)
              .run(
                      ctx -> {
                          LanguageServiceClient client = ctx.getBean(LanguageServiceClient.class);
                          assertThat(client.getSettings().getCredentialsProvider()).isSameAs(mockCredentialsProvider);
                          assertThat(client.getSettings().getQuotaProjectId()).isSameAs(mockQuotaProjectId);
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