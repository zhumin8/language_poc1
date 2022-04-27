package com.sample.autoconfig;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.spring.autoconfigure.vision.CloudVisionAutoConfiguration;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.spring.core.UserAgentHeaderProvider;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LanguageServiceClient.class)
@ConditionalOnProperty(value = "spring.cloud.gcp.language.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LanguageProperties.class)
public class LanguageAutoConfig {

  private final LanguageProperties clientProperties;
  private final CredentialsProvider credentialsProvider;
  private final String projectId;

  public LanguageAutoConfig(LanguageProperties properties,
      CredentialsProvider credentialsProvider,
      GcpProjectIdProvider projectIdProvider)
      throws IOException {
    this.clientProperties = properties;
    if (properties.getCredentials().hasKey()) {
      this.credentialsProvider = new DefaultCredentialsProvider(properties);
    } else {
      this.credentialsProvider = credentialsProvider;
    }
    if (properties.getProjectId() != null) {
      this.projectId =
          properties.getProjectId();
    } else {
      this.projectId =
          projectIdProvider.getProjectId();
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient() throws IOException {

    LanguageServiceSettings clientSettings =
        LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(this.credentialsProvider)
            .setQuotaProjectId(this.projectId)
            // .setEndpoint("language.googleapis.com:443")
        .build();
     return LanguageServiceClient.create(clientSettings);
  }
}
