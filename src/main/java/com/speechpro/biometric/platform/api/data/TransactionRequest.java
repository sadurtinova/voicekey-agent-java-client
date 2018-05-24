package com.speechpro.biometric.platform.api.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.speechpro.biometric.platform.api.data.structure.AudioSource;

/**
 * Author : bedash
 * Date   : 12.06.16
 */
public class TransactionRequest {
    @JsonProperty("extension")
    private final String extension;

    @JsonProperty("call_id")
    private int callId;

    @JsonProperty("reset_sound")
    private final Boolean resetSound;

    @JsonProperty("audio_source")
    private final AudioSource audioSource;

    public TransactionRequest(
            String extension,
            Boolean resetSound,
            AudioSource audioSource
    ){
        this.extension = extension;
        this.resetSound = resetSound;
        this.audioSource = audioSource;
    }

    @JsonCreator
    public TransactionRequest(String extension,
                              Boolean resetSound,
                              AudioSource audioSource,
                              int callId){
        this(extension, resetSound,audioSource);
        this.callId = callId;
    }

    public String getExtension() {
        return extension;
    }

    public int getCallId() {
        return callId;
    }

    public Boolean getResetSound() {
        return resetSound;
    }

    public AudioSource getAudioSource() {
        return audioSource;
    }
}
