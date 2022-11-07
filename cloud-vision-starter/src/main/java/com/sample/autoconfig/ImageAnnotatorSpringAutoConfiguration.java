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

package com.sample.autoconfig;

import com.google.api.core.BetaApi;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import java.io.IOException;
import java.util.Collections;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.sample.shared.SharedProperties;

@BetaApi
@Generated("by gapic-generator-java")
@AutoConfiguration
@ConditionalOnClass(value = ImageAnnotatorClient.class)
@ConditionalOnProperty(
    value = "spring.cloud.gcp.autoconfig.vision.image-annotator.enabled", matchIfMissing = false)
@EnableConfigurationProperties({ImageAnnotatorSpringProperties.class, SharedProperties.class})
public class ImageAnnotatorSpringAutoConfiguration {
  private final ImageAnnotatorSpringProperties clientProperties;
  private final SharedProperties sharedProperties;

  protected ImageAnnotatorSpringAutoConfiguration(ImageAnnotatorSpringProperties clientProperties,
      SharedProperties sharedProperties) {
    this.clientProperties = clientProperties;
    this.sharedProperties = sharedProperties;
  }

  @Bean
  @ConditionalOnMissingBean
  public CredentialsProvider imageAnnotatorCredentials() throws IOException {
    if (this.clientProperties.getCredentials().hasKey()) {
      return new DefaultCredentialsProvider(this.clientProperties);
    }
    return new DefaultCredentialsProvider(this.sharedProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public TransportChannelProvider defaultImageAnnotatorTransportChannelProvider() {
    return ImageAnnotatorSettings.defaultTransportChannelProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public ImageAnnotatorClient imageAnnotatorClient(
      @Qualifier("imageAnnotatorCredentials") CredentialsProvider credentialsProvider,
      @Qualifier("defaultImageAnnotatorTransportChannelProvider") TransportChannelProvider defaultTransportChannelProvider)
      throws IOException {
    ImageAnnotatorSettings.Builder clientSettingsBuilder =
        ImageAnnotatorSettings.newBuilder()
            .setCredentialsProvider(credentialsProvider)
            .setTransportChannelProvider(defaultTransportChannelProvider)
            .setHeaderProvider(userAgentHeaderProvider());
    if (this.clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(this.clientProperties.getQuotaProjectId());
    }
    if (this.clientProperties.getExecutorThreadCount() != null) {
      ExecutorProvider executorProvider =
          ImageAnnotatorSettings.defaultExecutorProviderBuilder()
              .setExecutorThreadCount(this.clientProperties.getExecutorThreadCount())
              .build();
      clientSettingsBuilder.setBackgroundExecutorProvider(executorProvider);
    }
    if (this.clientProperties.getUseRest()) {
      clientSettingsBuilder.setTransportChannelProvider(
          ImageAnnotatorSettings.defaultHttpJsonTransportProviderBuilder().build());
    }

    return ImageAnnotatorClient.create(clientSettingsBuilder.build());
  }

  // custom user agent header provider.
  private HeaderProvider userAgentHeaderProvider() {
    // String springLibrary = "spring-cloud-gcp-vision";
    String springLibrary = "spring-autogen-vision"; // get service name directly
    String version = this.getClass().getPackage().getImplementationVersion(); // META-INF/MANIFEST.MF

    // see concord tools.yaml google3/cloud/analysis/concord/configs/api/attribution-prod/tools.yaml?rcl=469347651&l=428
    return () -> Collections.singletonMap("user-agent", springLibrary + "/" + version);

    // return () -> Collections.singletonMap("user-agent", "Spring/" + version + " " + springLibrary + "/" + version);
  }
}
