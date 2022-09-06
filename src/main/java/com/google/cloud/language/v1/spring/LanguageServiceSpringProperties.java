/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.language.v1.spring;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.threeten.bp.Duration;

@ConfigurationProperties("spring.cloud.gcp.autoconfig.language.language-service")
public class LanguageServiceSpringProperties implements CredentialsSupplier {
  private final Credentials credentials =
      new Credentials(
          "https://www.googleapis.com/auth/cloud-language",
          "https://www.googleapis.com/auth/cloud-platform");
  private String quotaProjectId;
  private Integer executorThreadCount;
  private boolean useRest;
  private Duration analyzeSentimentInitialRetryDelay = Duration.ofMillis(100L);
  private double analyzeSentimentRetryDelayMultiplier = 1.3;
  private Duration analyzeSentimentMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration analyzeSentimentInitialRpcTimeout = Duration.ofMillis(600000L);
  private double analyzeSentimentRpcTimeoutMultiplier = 1.0;
  private Duration analyzeSentimentMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration analyzeSentimentTotalTimeout = Duration.ofMillis(600000L);
  private Duration analyzeEntitiesInitialRetryDelay = Duration.ofMillis(100L);
  private double analyzeEntitiesRetryDelayMultiplier = 1.3;
  private Duration analyzeEntitiesMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration analyzeEntitiesInitialRpcTimeout = Duration.ofMillis(600000L);
  private double analyzeEntitiesRpcTimeoutMultiplier = 1.0;
  private Duration analyzeEntitiesMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration analyzeEntitiesTotalTimeout = Duration.ofMillis(600000L);
  private Duration analyzeEntitySentimentInitialRetryDelay = Duration.ofMillis(100L);
  private double analyzeEntitySentimentRetryDelayMultiplier = 1.3;
  private Duration analyzeEntitySentimentMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration analyzeEntitySentimentInitialRpcTimeout = Duration.ofMillis(600000L);
  private double analyzeEntitySentimentRpcTimeoutMultiplier = 1.0;
  private Duration analyzeEntitySentimentMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration analyzeEntitySentimentTotalTimeout = Duration.ofMillis(600000L);
  private Duration analyzeSyntaxInitialRetryDelay = Duration.ofMillis(100L);
  private double analyzeSyntaxRetryDelayMultiplier = 1.3;
  private Duration analyzeSyntaxMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration analyzeSyntaxInitialRpcTimeout = Duration.ofMillis(600000L);
  private double analyzeSyntaxRpcTimeoutMultiplier = 1.0;
  private Duration analyzeSyntaxMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration analyzeSyntaxTotalTimeout = Duration.ofMillis(600000L);
  private Duration classifyTextInitialRetryDelay = Duration.ofMillis(100L);
  private double classifyTextRetryDelayMultiplier = 1.3;
  private Duration classifyTextMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration classifyTextInitialRpcTimeout = Duration.ofMillis(600000L);
  private double classifyTextRpcTimeoutMultiplier = 1.0;
  private Duration classifyTextMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration classifyTextTotalTimeout = Duration.ofMillis(600000L);
  private Duration annotateTextInitialRetryDelay = Duration.ofMillis(100L);
  private double annotateTextRetryDelayMultiplier = 1.3;
  private Duration annotateTextMaxRetryDelay = Duration.ofMillis(60000L);
  private Duration annotateTextInitialRpcTimeout = Duration.ofMillis(600000L);
  private double annotateTextRpcTimeoutMultiplier = 1.0;
  private Duration annotateTextMaxRpcTimeout = Duration.ofMillis(600000L);
  private Duration annotateTextTotalTimeout = Duration.ofMillis(600000L);

  @Override
  public Credentials getCredentials() {
    return this.credentials;
  }

  public String getQuotaProjectId() {
    return this.quotaProjectId;
  }

  public void setQuotaProjectId(String quotaProjectId) {
    this.quotaProjectId = quotaProjectId;
  }

  public boolean getUseRest() {
    return this.useRest;
  }

  public Integer getExecutorThreadCount() {
    return this.executorThreadCount;
  }

  public void setExecutorThreadCount(Integer executorThreadCount) {
    this.executorThreadCount = executorThreadCount;
  }

  public Duration getAnalyzeSentimentInitialRetryDelay() {
    return this.analyzeSentimentInitialRetryDelay;
  }

  public void setAnalyzeSentimentInitialRetryDelay(Duration analyzeSentimentInitialRetryDelay) {
    this.analyzeSentimentInitialRetryDelay = analyzeSentimentInitialRetryDelay;
  }

  public double getAnalyzeSentimentRetryDelayMultiplier() {
    return this.analyzeSentimentRetryDelayMultiplier;
  }

  public void setAnalyzeSentimentRetryDelayMultiplier(double analyzeSentimentRetryDelayMultiplier) {
    this.analyzeSentimentRetryDelayMultiplier = analyzeSentimentRetryDelayMultiplier;
  }

  public Duration getAnalyzeSentimentMaxRetryDelay() {
    return this.analyzeSentimentMaxRetryDelay;
  }

  public void setAnalyzeSentimentMaxRetryDelay(Duration analyzeSentimentMaxRetryDelay) {
    this.analyzeSentimentMaxRetryDelay = analyzeSentimentMaxRetryDelay;
  }

  public Duration getAnalyzeSentimentInitialRpcTimeout() {
    return this.analyzeSentimentInitialRpcTimeout;
  }

  public void setAnalyzeSentimentInitialRpcTimeout(Duration analyzeSentimentInitialRpcTimeout) {
    this.analyzeSentimentInitialRpcTimeout = analyzeSentimentInitialRpcTimeout;
  }

  public double getAnalyzeSentimentRpcTimeoutMultiplier() {
    return this.analyzeSentimentRpcTimeoutMultiplier;
  }

  public void setAnalyzeSentimentRpcTimeoutMultiplier(double analyzeSentimentRpcTimeoutMultiplier) {
    this.analyzeSentimentRpcTimeoutMultiplier = analyzeSentimentRpcTimeoutMultiplier;
  }

  public Duration getAnalyzeSentimentMaxRpcTimeout() {
    return this.analyzeSentimentMaxRpcTimeout;
  }

  public void setAnalyzeSentimentMaxRpcTimeout(Duration analyzeSentimentMaxRpcTimeout) {
    this.analyzeSentimentMaxRpcTimeout = analyzeSentimentMaxRpcTimeout;
  }

  public Duration getAnalyzeSentimentTotalTimeout() {
    return this.analyzeSentimentTotalTimeout;
  }

  public void setAnalyzeSentimentTotalTimeout(Duration analyzeSentimentTotalTimeout) {
    this.analyzeSentimentTotalTimeout = analyzeSentimentTotalTimeout;
  }

  public Duration getAnalyzeEntitiesInitialRetryDelay() {
    return this.analyzeEntitiesInitialRetryDelay;
  }

  public void setAnalyzeEntitiesInitialRetryDelay(Duration analyzeEntitiesInitialRetryDelay) {
    this.analyzeEntitiesInitialRetryDelay = analyzeEntitiesInitialRetryDelay;
  }

  public double getAnalyzeEntitiesRetryDelayMultiplier() {
    return this.analyzeEntitiesRetryDelayMultiplier;
  }

  public void setAnalyzeEntitiesRetryDelayMultiplier(double analyzeEntitiesRetryDelayMultiplier) {
    this.analyzeEntitiesRetryDelayMultiplier = analyzeEntitiesRetryDelayMultiplier;
  }

  public Duration getAnalyzeEntitiesMaxRetryDelay() {
    return this.analyzeEntitiesMaxRetryDelay;
  }

  public void setAnalyzeEntitiesMaxRetryDelay(Duration analyzeEntitiesMaxRetryDelay) {
    this.analyzeEntitiesMaxRetryDelay = analyzeEntitiesMaxRetryDelay;
  }

  public Duration getAnalyzeEntitiesInitialRpcTimeout() {
    return this.analyzeEntitiesInitialRpcTimeout;
  }

  public void setAnalyzeEntitiesInitialRpcTimeout(Duration analyzeEntitiesInitialRpcTimeout) {
    this.analyzeEntitiesInitialRpcTimeout = analyzeEntitiesInitialRpcTimeout;
  }

  public double getAnalyzeEntitiesRpcTimeoutMultiplier() {
    return this.analyzeEntitiesRpcTimeoutMultiplier;
  }

  public void setAnalyzeEntitiesRpcTimeoutMultiplier(double analyzeEntitiesRpcTimeoutMultiplier) {
    this.analyzeEntitiesRpcTimeoutMultiplier = analyzeEntitiesRpcTimeoutMultiplier;
  }

  public Duration getAnalyzeEntitiesMaxRpcTimeout() {
    return this.analyzeEntitiesMaxRpcTimeout;
  }

  public void setAnalyzeEntitiesMaxRpcTimeout(Duration analyzeEntitiesMaxRpcTimeout) {
    this.analyzeEntitiesMaxRpcTimeout = analyzeEntitiesMaxRpcTimeout;
  }

  public Duration getAnalyzeEntitiesTotalTimeout() {
    return this.analyzeEntitiesTotalTimeout;
  }

  public void setAnalyzeEntitiesTotalTimeout(Duration analyzeEntitiesTotalTimeout) {
    this.analyzeEntitiesTotalTimeout = analyzeEntitiesTotalTimeout;
  }

  public Duration getAnalyzeEntitySentimentInitialRetryDelay() {
    return this.analyzeEntitySentimentInitialRetryDelay;
  }

  public void setAnalyzeEntitySentimentInitialRetryDelay(
      Duration analyzeEntitySentimentInitialRetryDelay) {
    this.analyzeEntitySentimentInitialRetryDelay = analyzeEntitySentimentInitialRetryDelay;
  }

  public double getAnalyzeEntitySentimentRetryDelayMultiplier() {
    return this.analyzeEntitySentimentRetryDelayMultiplier;
  }

  public void setAnalyzeEntitySentimentRetryDelayMultiplier(
      double analyzeEntitySentimentRetryDelayMultiplier) {
    this.analyzeEntitySentimentRetryDelayMultiplier = analyzeEntitySentimentRetryDelayMultiplier;
  }

  public Duration getAnalyzeEntitySentimentMaxRetryDelay() {
    return this.analyzeEntitySentimentMaxRetryDelay;
  }

  public void setAnalyzeEntitySentimentMaxRetryDelay(Duration analyzeEntitySentimentMaxRetryDelay) {
    this.analyzeEntitySentimentMaxRetryDelay = analyzeEntitySentimentMaxRetryDelay;
  }

  public Duration getAnalyzeEntitySentimentInitialRpcTimeout() {
    return this.analyzeEntitySentimentInitialRpcTimeout;
  }

  public void setAnalyzeEntitySentimentInitialRpcTimeout(
      Duration analyzeEntitySentimentInitialRpcTimeout) {
    this.analyzeEntitySentimentInitialRpcTimeout = analyzeEntitySentimentInitialRpcTimeout;
  }

  public double getAnalyzeEntitySentimentRpcTimeoutMultiplier() {
    return this.analyzeEntitySentimentRpcTimeoutMultiplier;
  }

  public void setAnalyzeEntitySentimentRpcTimeoutMultiplier(
      double analyzeEntitySentimentRpcTimeoutMultiplier) {
    this.analyzeEntitySentimentRpcTimeoutMultiplier = analyzeEntitySentimentRpcTimeoutMultiplier;
  }

  public Duration getAnalyzeEntitySentimentMaxRpcTimeout() {
    return this.analyzeEntitySentimentMaxRpcTimeout;
  }

  public void setAnalyzeEntitySentimentMaxRpcTimeout(Duration analyzeEntitySentimentMaxRpcTimeout) {
    this.analyzeEntitySentimentMaxRpcTimeout = analyzeEntitySentimentMaxRpcTimeout;
  }

  public Duration getAnalyzeEntitySentimentTotalTimeout() {
    return this.analyzeEntitySentimentTotalTimeout;
  }

  public void setAnalyzeEntitySentimentTotalTimeout(Duration analyzeEntitySentimentTotalTimeout) {
    this.analyzeEntitySentimentTotalTimeout = analyzeEntitySentimentTotalTimeout;
  }

  public Duration getAnalyzeSyntaxInitialRetryDelay() {
    return this.analyzeSyntaxInitialRetryDelay;
  }

  public void setAnalyzeSyntaxInitialRetryDelay(Duration analyzeSyntaxInitialRetryDelay) {
    this.analyzeSyntaxInitialRetryDelay = analyzeSyntaxInitialRetryDelay;
  }

  public double getAnalyzeSyntaxRetryDelayMultiplier() {
    return this.analyzeSyntaxRetryDelayMultiplier;
  }

  public void setAnalyzeSyntaxRetryDelayMultiplier(double analyzeSyntaxRetryDelayMultiplier) {
    this.analyzeSyntaxRetryDelayMultiplier = analyzeSyntaxRetryDelayMultiplier;
  }

  public Duration getAnalyzeSyntaxMaxRetryDelay() {
    return this.analyzeSyntaxMaxRetryDelay;
  }

  public void setAnalyzeSyntaxMaxRetryDelay(Duration analyzeSyntaxMaxRetryDelay) {
    this.analyzeSyntaxMaxRetryDelay = analyzeSyntaxMaxRetryDelay;
  }

  public Duration getAnalyzeSyntaxInitialRpcTimeout() {
    return this.analyzeSyntaxInitialRpcTimeout;
  }

  public void setAnalyzeSyntaxInitialRpcTimeout(Duration analyzeSyntaxInitialRpcTimeout) {
    this.analyzeSyntaxInitialRpcTimeout = analyzeSyntaxInitialRpcTimeout;
  }

  public double getAnalyzeSyntaxRpcTimeoutMultiplier() {
    return this.analyzeSyntaxRpcTimeoutMultiplier;
  }

  public void setAnalyzeSyntaxRpcTimeoutMultiplier(double analyzeSyntaxRpcTimeoutMultiplier) {
    this.analyzeSyntaxRpcTimeoutMultiplier = analyzeSyntaxRpcTimeoutMultiplier;
  }

  public Duration getAnalyzeSyntaxMaxRpcTimeout() {
    return this.analyzeSyntaxMaxRpcTimeout;
  }

  public void setAnalyzeSyntaxMaxRpcTimeout(Duration analyzeSyntaxMaxRpcTimeout) {
    this.analyzeSyntaxMaxRpcTimeout = analyzeSyntaxMaxRpcTimeout;
  }

  public Duration getAnalyzeSyntaxTotalTimeout() {
    return this.analyzeSyntaxTotalTimeout;
  }

  public void setAnalyzeSyntaxTotalTimeout(Duration analyzeSyntaxTotalTimeout) {
    this.analyzeSyntaxTotalTimeout = analyzeSyntaxTotalTimeout;
  }

  public Duration getClassifyTextInitialRetryDelay() {
    return this.classifyTextInitialRetryDelay;
  }

  public void setClassifyTextInitialRetryDelay(Duration classifyTextInitialRetryDelay) {
    this.classifyTextInitialRetryDelay = classifyTextInitialRetryDelay;
  }

  public double getClassifyTextRetryDelayMultiplier() {
    return this.classifyTextRetryDelayMultiplier;
  }

  public void setClassifyTextRetryDelayMultiplier(double classifyTextRetryDelayMultiplier) {
    this.classifyTextRetryDelayMultiplier = classifyTextRetryDelayMultiplier;
  }

  public Duration getClassifyTextMaxRetryDelay() {
    return this.classifyTextMaxRetryDelay;
  }

  public void setClassifyTextMaxRetryDelay(Duration classifyTextMaxRetryDelay) {
    this.classifyTextMaxRetryDelay = classifyTextMaxRetryDelay;
  }

  public Duration getClassifyTextInitialRpcTimeout() {
    return this.classifyTextInitialRpcTimeout;
  }

  public void setClassifyTextInitialRpcTimeout(Duration classifyTextInitialRpcTimeout) {
    this.classifyTextInitialRpcTimeout = classifyTextInitialRpcTimeout;
  }

  public double getClassifyTextRpcTimeoutMultiplier() {
    return this.classifyTextRpcTimeoutMultiplier;
  }

  public void setClassifyTextRpcTimeoutMultiplier(double classifyTextRpcTimeoutMultiplier) {
    this.classifyTextRpcTimeoutMultiplier = classifyTextRpcTimeoutMultiplier;
  }

  public Duration getClassifyTextMaxRpcTimeout() {
    return this.classifyTextMaxRpcTimeout;
  }

  public void setClassifyTextMaxRpcTimeout(Duration classifyTextMaxRpcTimeout) {
    this.classifyTextMaxRpcTimeout = classifyTextMaxRpcTimeout;
  }

  public Duration getClassifyTextTotalTimeout() {
    return this.classifyTextTotalTimeout;
  }

  public void setClassifyTextTotalTimeout(Duration classifyTextTotalTimeout) {
    this.classifyTextTotalTimeout = classifyTextTotalTimeout;
  }

  public Duration getAnnotateTextInitialRetryDelay() {
    return this.annotateTextInitialRetryDelay;
  }

  public void setAnnotateTextInitialRetryDelay(Duration annotateTextInitialRetryDelay) {
    this.annotateTextInitialRetryDelay = annotateTextInitialRetryDelay;
  }

  public double getAnnotateTextRetryDelayMultiplier() {
    return this.annotateTextRetryDelayMultiplier;
  }

  public void setAnnotateTextRetryDelayMultiplier(double annotateTextRetryDelayMultiplier) {
    this.annotateTextRetryDelayMultiplier = annotateTextRetryDelayMultiplier;
  }

  public Duration getAnnotateTextMaxRetryDelay() {
    return this.annotateTextMaxRetryDelay;
  }

  public void setAnnotateTextMaxRetryDelay(Duration annotateTextMaxRetryDelay) {
    this.annotateTextMaxRetryDelay = annotateTextMaxRetryDelay;
  }

  public Duration getAnnotateTextInitialRpcTimeout() {
    return this.annotateTextInitialRpcTimeout;
  }

  public void setAnnotateTextInitialRpcTimeout(Duration annotateTextInitialRpcTimeout) {
    this.annotateTextInitialRpcTimeout = annotateTextInitialRpcTimeout;
  }

  public double getAnnotateTextRpcTimeoutMultiplier() {
    return this.annotateTextRpcTimeoutMultiplier;
  }

  public void setAnnotateTextRpcTimeoutMultiplier(double annotateTextRpcTimeoutMultiplier) {
    this.annotateTextRpcTimeoutMultiplier = annotateTextRpcTimeoutMultiplier;
  }

  public Duration getAnnotateTextMaxRpcTimeout() {
    return this.annotateTextMaxRpcTimeout;
  }

  public void setAnnotateTextMaxRpcTimeout(Duration annotateTextMaxRpcTimeout) {
    this.annotateTextMaxRpcTimeout = annotateTextMaxRpcTimeout;
  }

  public Duration getAnnotateTextTotalTimeout() {
    return this.annotateTextTotalTimeout;
  }

  public void setAnnotateTextTotalTimeout(Duration annotateTextTotalTimeout) {
    this.annotateTextTotalTimeout = annotateTextTotalTimeout;
  }
}
