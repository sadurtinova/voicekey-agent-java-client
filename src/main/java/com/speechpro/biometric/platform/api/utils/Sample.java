package com.speechpro.biometric.platform.api.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Author : bedash
 * Date   : 21.06.16
 */
public final class Sample {
    @JsonProperty("data")
    public final byte[] data;

    @JsonProperty("sampling_rate")
    public int samplingRate;
    public Sample(final byte[] data) {
        this(data, 8000);
    }

    @JsonCreator
    public Sample(
            @JsonProperty("data") final byte[] data,
            @JsonProperty("sampling_rate") final int samplingRate
    ) {
        this.data = data;
        this.samplingRate = samplingRate;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
