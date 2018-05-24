package com.speechpro.biometric.platform.api.data.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author : bedash
 * Date   : 10.06.16
 */

public class Credentials {
    private final String user;
    private final String password;

    @JsonProperty("domain_id")
    private String domainId;

    public Credentials(String user, String password, String domainId) {
        this.user = user;
        this.password = password;
        this.domainId = domainId;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDomainId() {
        return domainId;
    }
}
