package com.sample.shared;

// TODO: move this class to spring-cloud-gcp-core

import com.google.api.gax.retrying.RetrySettings;
import org.threeten.bp.Duration;

/** Retry settings configuration.
 *  Ref: https://github.com/googleapis/gax-java/blob/main/gax/src/main/java/com/google/api/gax/retrying/RetrySettings.java
 * */
public class Retry {

    private Duration totalTimeout;
    private Duration initialRetryDelay;
    private Double retryDelayMultiplier;
    private Duration maxRetryDelay;
    private Integer maxAttempts;
    private Duration initialRpcTimeout;
    private Double rpcTimeoutMultiplier;
    private Duration maxRpcTimeout;

    public Duration getTotalTimeout() {
        return totalTimeout;
    }

    public void setTotalTimeout(java.time.Duration totalTimeout) {
        this.totalTimeout = Duration.parse(totalTimeout.toString());
    }

    public Duration getInitialRetryDelay() {
        return initialRetryDelay;
    }

    public void setInitialRetryDelay(java.time.Duration initialRetryDelay) {
        this.initialRetryDelay = Duration.parse(initialRetryDelay.toString());
    }

    public Double getRetryDelayMultiplier() {
        return retryDelayMultiplier;
    }

    public void setRetryDelayMultiplier(Double retryDelayMultiplier) {
        this.retryDelayMultiplier = retryDelayMultiplier;
    }

    public Duration getMaxRetryDelay() {
        return maxRetryDelay;
    }

    public void setMaxRetryDelay(java.time.Duration maxRetryDelay) {
        this.maxRetryDelay = Duration.parse(maxRetryDelay.toString());
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Duration getInitialRpcTimeout() {
        return initialRpcTimeout;
    }

    public void setInitialRpcTimeout(java.time.Duration initialRpcTimeout) {
        this.initialRpcTimeout = Duration.parse(initialRpcTimeout.toString());
    }

    public Double getRpcTimeoutMultiplier() {
        return rpcTimeoutMultiplier;
    }

    public void setRpcTimeoutMultiplier(Double rpcTimeoutMultiplier) {
        this.rpcTimeoutMultiplier = rpcTimeoutMultiplier;
    }

    public Duration getMaxRpcTimeout() {
        return maxRpcTimeout;
    }

    public void setMaxRpcTimeout(java.time.Duration maxRpcTimeout) {
        this.maxRpcTimeout = Duration.parse(maxRpcTimeout.toString());
    }

    public RetrySettings buildRetrySettingsFrom(RetrySettings clientRetrySettings){
        // in case of version mismatch b/w spring-autoconfig & client lib
        // do not set defaults in properties, only modify value if property is set.
        RetrySettings.Builder builder = clientRetrySettings.toBuilder();
        if (totalTimeout != null) {
            builder.setTotalTimeout(totalTimeout);
        }
        if (initialRetryDelay != null) {
            builder.setInitialRetryDelay(initialRetryDelay);
        }
        if (retryDelayMultiplier != null) {
            builder.setRetryDelayMultiplier(retryDelayMultiplier);
        }
        if (maxRetryDelay != null) {
            builder.setMaxRetryDelay(maxRetryDelay);
        }
        if (maxAttempts != null) {
            builder.setMaxAttempts(maxAttempts);
        }
        if (initialRpcTimeout != null) {
            builder.setInitialRpcTimeout(initialRpcTimeout);
        }
        if (rpcTimeoutMultiplier != null) {
            builder.setRpcTimeoutMultiplier(rpcTimeoutMultiplier);
        }
        if (maxRpcTimeout != null) {
            builder.setMaxRpcTimeout(maxRpcTimeout);
        }

        return builder.build();
    }
}
