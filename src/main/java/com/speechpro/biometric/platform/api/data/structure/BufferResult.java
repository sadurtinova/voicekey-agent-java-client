package com.speechpro.biometric.platform.api.data.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author : bedash
 * Date   : 14.06.16
 */
public final class BufferResult {
    private final String elementId;

    @JsonCreator
    public BufferResult(
            @JsonProperty("element_id") String elementId
    ){
        this.elementId = elementId;
    }

    public String getElementId() {
        return elementId;
    }
}
