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
    return LanguageServiceClient.create(clientSettingsBuilder.build());
  }
}
