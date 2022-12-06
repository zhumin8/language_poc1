package com.sample.shared;

import org.threeten.bp.Duration;

/** Retry settings. */
public class RetryProperties {

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

}
