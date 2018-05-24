package com.speechpro.biometric.platform.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sadurtinova on 09.05.2018.
 */
public class ReplaceExisting {

    @JsonProperty("replace_existing")
    boolean replace;

    public ReplaceExisting(boolean replace) {
        this.replace = replace;
    }
}
