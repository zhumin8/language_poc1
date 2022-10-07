package com.sample.autoconfig;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.threeten.bp.Duration;

@ConfigurationProperties("spring.cloud.gcp.autoconfig.vision.image-annotator")
// branding considerations: google.cloud.spring.autoconfig.language-service
// [brand.name].autoconfig.[lib-name].[service-name]
// This needs to be consistent with com/sample/autoconfig/LanguageAutoConfig.java:34
public class ImageAnnotatorSpringProperties implements CredentialsSupplier {

  // Overrides the GCP OAuth2 credentials specified in the Core module.
  @NestedConfigurationProperty
  private final Credentials credentials = new Credentials("https://www.googleapis.com/auth/cloud-vision");

  private String quotaProjectId;

  private Integer executorThreadCount;

  private boolean useRest = false;

  // retry settings: for each method, set *relevant* retry settings directly to its default value
  // only writing out retry settings for one method (AnnotateText) as example
  private Duration annotateTextInitialRetryDelay; // do defaults here, or in autoconfig
  private Double annotateTextRetryDelayMultiplier;
  private Duration annotateTextMaxRetryDelay;
  private Duration annotateTextInitialRpcTimeout;
  private Double annotateTextRpcTimeoutMultiplier ;
  private Duration annotateTextMaxRpcTimeout ;
  private Duration annotateTextTotalTimeout ;

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

  public boolean getUseRest() {
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
