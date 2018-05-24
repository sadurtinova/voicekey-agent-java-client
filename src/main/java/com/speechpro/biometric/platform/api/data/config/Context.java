package com.speechpro.biometric.platform.api.data.config;

/**
 * Author : bedash
 * Date   : 10.06.16
 */
public final class Context {
    private final String agentUrl;

    public Context(String agentUrl){
        this.agentUrl = agentUrl;
    }

    public String getAgentUrl() {
        return agentUrl;
    }

}
