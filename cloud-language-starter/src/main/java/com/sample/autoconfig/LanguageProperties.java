package com.sample.autoconfig;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.sample.shared.Retry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("spring.cloud.gcp.language.language-service")
// branding considerations: google.cloud.spring.autoconfig.language-service
// [brand.name].autoconfig.[lib-name].[service-name]
// This needs to be consistent with com/sample/autoconfig/LanguageAutoConfig.java:34
public class LanguageProperties implements CredentialsSupplier {

  // Overrides the GCP OAuth2 credentials specified in the Core module.
  @NestedConfigurationProperty
  private final Credentials credentials = new Credentials("https://www.googleapis.com/auth/cloud-language");

  private String quotaProjectId;

  private Integer executorThreadCount;

  private boolean useRest = false;

  // Configurable properties for overriding RetrySettings
  // E.g. [prefix].language-service.service-retry-settings.initial-retry-delay=PT0.5S
  private Retry serviceRetrySettings;

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

  public boolean isUseRest() {
    return useRest;
  }

  public Retry getServiceRetrySettings() {
    return serviceRetrySettings;
  }

  public void setServiceRetrySettings(Retry serviceRetrySettings) {
    this.serviceRetrySettings = serviceRetrySettings;
  }

}
