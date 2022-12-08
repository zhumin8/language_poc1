package com.sample.autoconfig;

import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.threeten.bp.Duration;

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

  /** Retry settings. */
  public static class RetryProperties {

    private Duration initialRetryDelay;
    private Double retryDelayMultiplier;
    private Duration maxRetryDelay;
    private Duration initialRpcTimeout;
    private Duration maxRpcTimeout;
    private Double rpcTimeoutMultiplier;
    private Duration totalTimeout;

    public Duration getInitialRetryDelay() {
      return this.initialRetryDelay;
    }

    public void setInitialRetryDelay(
            java.time.Duration initialRetryDelay) {
      this.initialRetryDelay =
              Duration.parse(initialRetryDelay.toString());
    }

    public Double getRetryDelayMultiplier() {
      return this.retryDelayMultiplier;
    }

    public void setRetryDelayMultiplier(Double retryDelayMultiplier) {
      this.retryDelayMultiplier = retryDelayMultiplier;
    }

    public Duration getMaxRetryDelay() {
      return this.maxRetryDelay;
    }

    public void setMaxRetryDelay(java.time.Duration maxRetryDelay) {
      this.maxRetryDelay = Duration.parse(maxRetryDelay.toString());
    }

    public Duration getInitialRpcTimeout() {
      return this.initialRpcTimeout;
    }

    public void setInitialRpcTimeout(
            java.time.Duration initialRpcTimeout) {
      this.initialRpcTimeout =
              Duration.parse(initialRpcTimeout.toString());
    }

    public Double getRpcTimeoutMultiplier() {
      return this.rpcTimeoutMultiplier;
    }

    public void setRpcTimeoutMultiplier(Double rpcTimeoutMultiplier) {
      this.rpcTimeoutMultiplier = rpcTimeoutMultiplier;
    }

    public Duration getMaxRpcTimeout() {
      return this.maxRpcTimeout;
    }

    public void setMaxRpcTimeout(java.time.Duration maxRpcTimeout) {
      this.maxRpcTimeout = Duration.parse(maxRpcTimeout.toString());
    }

    public Duration getTotalTimeout() {
      return this.totalTimeout;
    }

    public void setTotalTimeout(java.time.Duration totalTimeout) {
      this.totalTimeout = Duration.parse(totalTimeout.toString());
    }

    public RetrySettings buildRetrySettingsFrom(RetrySettings clientRetrySettings){
      // in case of version mismatch b/w spring-autoconfig & client lib
      // do not set defaults in properties, only modify value if property is set.
      RetrySettings.Builder builder = clientRetrySettings.toBuilder();
      if (initialRetryDelay != null) {
        builder.setInitialRetryDelay(initialRetryDelay);
      }
      if (maxRetryDelay != null) {
        builder.setMaxRetryDelay(maxRetryDelay);
      }
      if (retryDelayMultiplier != null) {
        builder.setRetryDelayMultiplier(retryDelayMultiplier);
      }
      if (initialRpcTimeout != null) {
        builder.setInitialRpcTimeout(initialRpcTimeout);
      }
      if (maxRpcTimeout != null) {
        builder.setMaxRpcTimeout(maxRpcTimeout);
      }
      if (rpcTimeoutMultiplier != null) {
        builder.setRpcTimeoutMultiplier(rpcTimeoutMultiplier);
      }
      if (totalTimeout != null) {
        builder.setTotalTimeout(totalTimeout);
      }
      return builder.build();
    }

  }
}
