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

  @Override
  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public String getUserAgent() {
    return this.userAgent;
  }

  // copied from core, altered header content to track this project.
  // could also just use the core one, depending on where to locate this project
  private String computeUserAgent(Class<?> clazz) {
    String[] packageTokens = clazz.getPackage().getName().split("\\.");
    String springLibrary = "spring-cloud-autogen-config-" + packageTokens[packageTokens.length - 1];
    String version = this.getClass().getPackage().getImplementationVersion();

    return "Spring-autoconfig/" + version + " " + springLibrary + "/" + version;
  }
}
