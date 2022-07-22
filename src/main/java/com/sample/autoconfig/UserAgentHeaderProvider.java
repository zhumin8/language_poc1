package com.sample.autoconfig;

import com.google.api.gax.rpc.HeaderProvider;
import java.util.Collections;
import java.util.Map;

public class UserAgentHeaderProvider implements HeaderProvider {
  private String userAgent;

  private final Map<String, String> headers;

  public UserAgentHeaderProvider(Class<?> clazz) {
    this.userAgent = computeUserAgent(clazz);
    this.headers = Collections.singletonMap("user-agent", this.userAgent);
  }

  /**
   * Returns the "user-agent" header whose value should be added to the google-cloud-java REST API
   * calls. e.g., {@code user-agent: Spring/1.0.0.RELEASE spring-cloud-gcp-pubsub/1.0.0.RELEASE}.
   */
  @Override
  public Map<String, String> getHeaders() {
    return this.headers;
  }

  /**
   * Returns the "user-agent" header value which should be added to the google-cloud-java REST API
   * calls. e.g., {@code Spring/1.0.0.RELEASE spring-cloud-gcp-pubsub/1.0.0.RELEASE}.
   *
   * @return the user agent string.
   */
  public String getUserAgent() {
    return this.userAgent;
  }

  private String computeUserAgent(Class<?> clazz) {
    String[] packageTokens = clazz.getPackage().getName().split("\\.");
    String springLibrary = "spring-cloud-autogen-config-" + packageTokens[packageTokens.length - 1];
    String version = this.getClass().getPackage().getImplementationVersion();

    return "Spring-autoconfig/" + version + " " + springLibrary + "/" + version;
  }
}
