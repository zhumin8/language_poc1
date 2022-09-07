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

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.cloud.language.v1.AnnotateTextRequest;
import com.google.cloud.language.v1.AnnotateTextResponse;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import javax.annotation.Generated;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

@Generated("by gapic-generator-java")
@Configuration("proxyBeanMethods = false")
@ConditionalOnClass(LanguageServiceClient.class)
@ConditionalOnProperty(
    value = "spring.cloud.gcp.autoconfig.language.language-service.enabled", matchIfMissing = false)
@EnableConfigurationProperties(LanguageServiceSpringProperties.class)
public class LanguageServiceSpringAutoConfiguration {
  private final LanguageServiceSpringProperties clientProperties;
  private static final ImmutableMap<String, RetrySettings> RETRY_PARAM_DEFINITIONS;

  static {
    ImmutableMap.Builder<String, RetrySettings> definitions = ImmutableMap.builder();
    RetrySettings settings = null;
    settings =
        RetrySettings.newBuilder()
            .setInitialRetryDelay(Duration.ofMillis(100L))
            .setRetryDelayMultiplier(1.3)
            .setMaxRetryDelay(Duration.ofMillis(60000L))
            .setInitialRpcTimeout(Duration.ofMillis(600000L))
            .setRpcTimeoutMultiplier(1.0)
            .setMaxRpcTimeout(Duration.ofMillis(600000L))
            .setTotalTimeout(Duration.ofMillis(600000L))
            .build();
    definitions.put("retry_policy_0_params", settings);
    RETRY_PARAM_DEFINITIONS = definitions.build();
  }

  protected LanguageServiceSpringAutoConfiguration(
      LanguageServiceSpringProperties clientProperties) {
    this.clientProperties = clientProperties;
  }

  @Bean
  @ConditionalOnMissingBean
  public CredentialsProvider googleCredentials() throws IOException {
    return ((CredentialsProvider) new DefaultCredentialsProvider(this.clientProperties));
  }

  @Bean
  @ConditionalOnMissingBean
  public TransportChannelProvider defaultLanguageServiceTransportChannelProvider() {
    return LanguageServiceSettings.defaultTransportChannelProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public LanguageServiceClient languageServiceClient(
      CredentialsProvider credentialsProvider,
      TransportChannelProvider defaultTransportChannelProvider)
      throws IOException {
    LanguageServiceSettings.Builder clientSettingsBuilder =
        LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(credentialsProvider)
            .setTransportChannelProvider(defaultTransportChannelProvider);
            // .setHeaderProvider();
    if (this.clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(this.clientProperties.getQuotaProjectId());
    }
    if (this.clientProperties.getExecutorThreadCount() != null) {
      ExecutorProvider executorProvider =
          LanguageServiceSettings.defaultExecutorProviderBuilder()
              .setExecutorThreadCount(this.clientProperties.getExecutorThreadCount())
              .build();
      clientSettingsBuilder.setBackgroundExecutorProvider(executorProvider);
    }
    if (this.clientProperties.getUseRest()) {
      clientSettingsBuilder.setTransportChannelProvider(
          LanguageServiceSettings.defaultHttpJsonTransportProviderBuilder().build());
    }

    RetrySettings.Builder analyzeSentimentRetrySettingBuilder =
        clientSettingsBuilder.analyzeSentimentSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getAnalyzeSentimentInitialRetryDelay() != null) {
      analyzeSentimentRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getAnalyzeSentimentInitialRetryDelay());
    }
    if (this.clientProperties.getAnalyzeSentimentRetryDelayMultiplier() != null) {
      analyzeSentimentRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getAnalyzeSentimentRetryDelayMultiplier());
    }
    if (this.clientProperties.getAnalyzeSentimentMaxRetryDelay() != null) {
      analyzeSentimentRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getAnalyzeSentimentMaxRetryDelay());
    }
    if (this.clientProperties.getAnalyzeSentimentInitialRpcTimeout() != null) {
      analyzeSentimentRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getAnalyzeSentimentInitialRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeSentimentRpcTimeoutMultiplier() != null) {
      analyzeSentimentRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getAnalyzeSentimentRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getAnalyzeSentimentMaxRpcTimeout() != null) {
      analyzeSentimentRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getAnalyzeSentimentMaxRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeSentimentTotalTimeout() != null) {
      analyzeSentimentRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getAnalyzeSentimentTotalTimeout());
    }
    clientSettingsBuilder
        .analyzeSentimentSettings()
        .setRetrySettings(analyzeSentimentRetrySettingBuilder.build());
    RetrySettings.Builder analyzeEntitiesRetrySettingBuilder =
        clientSettingsBuilder.analyzeEntitiesSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getAnalyzeEntitiesInitialRetryDelay() != null) {
      analyzeEntitiesRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getAnalyzeEntitiesInitialRetryDelay());
    }
    if (this.clientProperties.getAnalyzeEntitiesRetryDelayMultiplier() != null) {
      analyzeEntitiesRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getAnalyzeEntitiesRetryDelayMultiplier());
    }
    if (this.clientProperties.getAnalyzeEntitiesMaxRetryDelay() != null) {
      analyzeEntitiesRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getAnalyzeEntitiesMaxRetryDelay());
    }
    if (this.clientProperties.getAnalyzeEntitiesInitialRpcTimeout() != null) {
      analyzeEntitiesRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getAnalyzeEntitiesInitialRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeEntitiesRpcTimeoutMultiplier() != null) {
      analyzeEntitiesRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getAnalyzeEntitiesRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getAnalyzeEntitiesMaxRpcTimeout() != null) {
      analyzeEntitiesRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getAnalyzeEntitiesMaxRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeEntitiesTotalTimeout() != null) {
      analyzeEntitiesRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getAnalyzeEntitiesTotalTimeout());
    }
    clientSettingsBuilder
        .analyzeEntitiesSettings()
        .setRetrySettings(analyzeEntitiesRetrySettingBuilder.build());
    RetrySettings.Builder analyzeEntitySentimentRetrySettingBuilder =
        clientSettingsBuilder.analyzeEntitySentimentSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getAnalyzeEntitySentimentInitialRetryDelay() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getAnalyzeEntitySentimentInitialRetryDelay());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentRetryDelayMultiplier() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getAnalyzeEntitySentimentRetryDelayMultiplier());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentMaxRetryDelay() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getAnalyzeEntitySentimentMaxRetryDelay());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentInitialRpcTimeout() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getAnalyzeEntitySentimentInitialRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentRpcTimeoutMultiplier() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getAnalyzeEntitySentimentRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentMaxRpcTimeout() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getAnalyzeEntitySentimentMaxRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeEntitySentimentTotalTimeout() != null) {
      analyzeEntitySentimentRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getAnalyzeEntitySentimentTotalTimeout());
    }
    clientSettingsBuilder
        .analyzeEntitySentimentSettings()
        .setRetrySettings(analyzeEntitySentimentRetrySettingBuilder.build());
    RetrySettings.Builder analyzeSyntaxRetrySettingBuilder =
        clientSettingsBuilder.analyzeSyntaxSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getAnalyzeSyntaxInitialRetryDelay() != null) {
      analyzeSyntaxRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getAnalyzeSyntaxInitialRetryDelay());
    }
    if (this.clientProperties.getAnalyzeSyntaxRetryDelayMultiplier() != null) {
      analyzeSyntaxRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getAnalyzeSyntaxRetryDelayMultiplier());
    }
    if (this.clientProperties.getAnalyzeSyntaxMaxRetryDelay() != null) {
      analyzeSyntaxRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getAnalyzeSyntaxMaxRetryDelay());
    }
    if (this.clientProperties.getAnalyzeSyntaxInitialRpcTimeout() != null) {
      analyzeSyntaxRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getAnalyzeSyntaxInitialRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeSyntaxRpcTimeoutMultiplier() != null) {
      analyzeSyntaxRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getAnalyzeSyntaxRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getAnalyzeSyntaxMaxRpcTimeout() != null) {
      analyzeSyntaxRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getAnalyzeSyntaxMaxRpcTimeout());
    }
    if (this.clientProperties.getAnalyzeSyntaxTotalTimeout() != null) {
      analyzeSyntaxRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getAnalyzeSyntaxTotalTimeout());
    }
    clientSettingsBuilder
        .analyzeSyntaxSettings()
        .setRetrySettings(analyzeSyntaxRetrySettingBuilder.build());
    RetrySettings.Builder classifyTextRetrySettingBuilder =
        clientSettingsBuilder.classifyTextSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getClassifyTextInitialRetryDelay() != null) {
      classifyTextRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getClassifyTextInitialRetryDelay());
    }
    if (this.clientProperties.getClassifyTextRetryDelayMultiplier() != null) {
      classifyTextRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getClassifyTextRetryDelayMultiplier());
    }
    if (this.clientProperties.getClassifyTextMaxRetryDelay() != null) {
      classifyTextRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getClassifyTextMaxRetryDelay());
    }
    if (this.clientProperties.getClassifyTextInitialRpcTimeout() != null) {
      classifyTextRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getClassifyTextInitialRpcTimeout());
    }
    if (this.clientProperties.getClassifyTextRpcTimeoutMultiplier() != null) {
      classifyTextRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getClassifyTextRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getClassifyTextMaxRpcTimeout() != null) {
      classifyTextRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getClassifyTextMaxRpcTimeout());
    }
    if (this.clientProperties.getClassifyTextTotalTimeout() != null) {
      classifyTextRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getClassifyTextTotalTimeout());
    }
    clientSettingsBuilder
        .classifyTextSettings()
        .setRetrySettings(classifyTextRetrySettingBuilder.build());
    RetrySettings.Builder annotateTextRetrySettingBuilder =
        clientSettingsBuilder.annotateTextSettings().getRetrySettings().toBuilder();
    if (this.clientProperties.getAnnotateTextInitialRetryDelay() != null) {
      annotateTextRetrySettingBuilder.setInitialRetryDelay(
          this.clientProperties.getAnnotateTextInitialRetryDelay());
    }
    if (this.clientProperties.getAnnotateTextRetryDelayMultiplier() != null) {
      annotateTextRetrySettingBuilder.setRetryDelayMultiplier(
          this.clientProperties.getAnnotateTextRetryDelayMultiplier());
    }
    if (this.clientProperties.getAnnotateTextMaxRetryDelay() != null) {
      annotateTextRetrySettingBuilder.setMaxRetryDelay(
          this.clientProperties.getAnnotateTextMaxRetryDelay());
    }
    if (this.clientProperties.getAnnotateTextInitialRpcTimeout() != null) {
      annotateTextRetrySettingBuilder.setInitialRpcTimeout(
          this.clientProperties.getAnnotateTextInitialRpcTimeout());
    }
    if (this.clientProperties.getAnnotateTextRpcTimeoutMultiplier() != null) {
      annotateTextRetrySettingBuilder.setRpcTimeoutMultiplier(
          this.clientProperties.getAnnotateTextRpcTimeoutMultiplier());
    }
    if (this.clientProperties.getAnnotateTextMaxRpcTimeout() != null) {
      annotateTextRetrySettingBuilder.setMaxRpcTimeout(
          this.clientProperties.getAnnotateTextMaxRpcTimeout());
    }
    if (this.clientProperties.getAnnotateTextTotalTimeout() != null) {
      annotateTextRetrySettingBuilder.setTotalTimeout(
          this.clientProperties.getAnnotateTextTotalTimeout());
    }
    clientSettingsBuilder
        .annotateTextSettings()
        .setRetrySettings(annotateTextRetrySettingBuilder.build());
    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }
}
