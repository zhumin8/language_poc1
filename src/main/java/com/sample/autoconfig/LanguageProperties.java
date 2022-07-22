package com.sample.autoconfig;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("spring.cloud.gcp.language")
public class LanguageProperties implements CredentialsSupplier {

  // Overrides the GCP OAuth2 credentials specified in the Core module.
  @NestedConfigurationProperty
  private final Credentials credentials = new Credentials("https://www.googleapis.com/auth/cloud-language");

  private String quotaProjectId;

  private Integer executorThreadCount;

  @Override
  public Credentials getCredentials() {
    return this.credentials;
  }

  public String getQuotaProjectId() {
    return quotaProjectId;
  }

  public void setQuotaProjectId(String quotaProjectId) {
    this.quotaProjectId = quotaProjectId;
  }

  public Integer getExecutorThreadCount() {
    return executorThreadCount;
  }

  public void setExecutorThreadCount(int executorThreadCount) {
    this.executorThreadCount = executorThreadCount;
  }
}
