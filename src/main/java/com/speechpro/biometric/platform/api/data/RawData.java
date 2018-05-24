package com.speechpro.biometric.platform.api.data;

/**
 * Author : bedash
 * Date   : 13.06.16
 */
public final class RawData {
    private final byte[] data;

    public RawData(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

}
