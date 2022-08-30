package com.sample.autoconfig;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.threeten.bp.Duration;

@ConfigurationProperties("spring.cloud.gcp.language") // branding considerations: google.cloud.spring.autoconfig.language?
public class LanguageProperties implements CredentialsSupplier {

  // Overrides the GCP OAuth2 credentials specified in the Core module.
  @NestedConfigurationProperty
  private final Credentials credentials = new Credentials("https://www.googleapis.com/auth/cloud-language");

  private String quotaProjectId;

  private Integer executorThreadCount;

  private boolean useRest = false;

  // retry settings: for each method, set *relevant* retry settings directly to its default value
  // only writing out retry settings for one method (AnnotateText) as example
  private Duration annotateTextInitialRetryDelay = Duration.ofMillis(100L); // do defaults here, or in autoconfig
  private Double annotateTextRetryDelayMultiplier = 1.3;
  private Duration annotateTextMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration annotateTextInitialRpcTimeout = Duration.ofMillis(600000L);
  private Double annotateTextRpcTimeoutMultiplier = 1.0;
  private Duration annotateTextMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration annotateTextTotalTimeout = Duration.ofMillis(600000L);

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

  // getter and setters
  public Duration getAnnotateTextInitialRetryDelay() {
    return annotateTextInitialRetryDelay;
  }

  public Double getAnnotateTextRetryDelayMultiplier() {
    return annotateTextRetryDelayMultiplier;
  }

  public Duration getAnnotateTextMaxRetryDelay() {
    return annotateTextMaxRetryDelay;
  }

  public Duration getAnnotateTextInitialRpcTimeout() {
    return annotateTextInitialRpcTimeout;
  }

  public Double getAnnotateTextRpcTimeoutMultiplier() {
    return annotateTextRpcTimeoutMultiplier;
  }

  public Duration getAnnotateTextMaxRpcTimeout() {
    return annotateTextMaxRpcTimeout;
  }

  public Duration getAnnotateTextTotalTimeout() {
    return annotateTextTotalTimeout;
  }

  public void setAnnotateTextInitialRetryDelay(Duration annotateTextInitialRetryDelay) {
    this.annotateTextInitialRetryDelay = annotateTextInitialRetryDelay;
  }

  public void setAnnotateTextRetryDelayMultiplier(Double annotateTextRetryDelayMultiplier) {
    this.annotateTextRetryDelayMultiplier = annotateTextRetryDelayMultiplier;
  }

  public void setAnnotateTextMaxRetryDelay(Duration annotateTextMaxRetryDelay) {
    this.annotateTextMaxRetryDelay = annotateTextMaxRetryDelay;
  }

  public void setAnnotateTextInitialRpcTimeout(Duration annotateTextInitialRpcTimeout) {
    this.annotateTextInitialRpcTimeout = annotateTextInitialRpcTimeout;
  }

  public void setAnnotateTextRpcTimeoutMultiplier(Double annotateTextRpcTimeoutMultiplier) {
    this.annotateTextRpcTimeoutMultiplier = annotateTextRpcTimeoutMultiplier;
  }

  public void setAnnotateTextMaxRpcTimeout(Duration annotateTextMaxRpcTimeout) {
    this.annotateTextMaxRpcTimeout = annotateTextMaxRpcTimeout;
  }

  public void setAnnotateTextTotalTimeout(Duration annotateTextTotalTimeout) {
    this.annotateTextTotalTimeout = annotateTextTotalTimeout;
  }
}
