package com.sample.autoconfig;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.sample.shared.RetryProperties;
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

  // retry settings: properties as nested class
  // in properties file: <prefix>.languageService.annotateTextRetry.initialRetryDelay=PT0.5S
  // writing out for two methods here, as example
  private RetryProperties annotateTextRetry = new RetryProperties();
  private RetryProperties analyzeSentimentRetry = new RetryProperties();

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

  public RetryProperties getAnnotateTextRetry() {
    return annotateTextRetry;
  }

  public void setAnnotateTextRetry(RetryProperties annotateTextRetry) {
    this.annotateTextRetry = annotateTextRetry;
  }

  public RetryProperties getAnalyzeSentimentRetry() {
    return analyzeSentimentRetry;
  }

  public void setAnalyzeSentimentRetry(RetryProperties analyzeSentimentRetry) {
    this.analyzeSentimentRetry = analyzeSentimentRetry;
  }
}
