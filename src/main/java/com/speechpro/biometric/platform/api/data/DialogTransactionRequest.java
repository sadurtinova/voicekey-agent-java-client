package com.speechpro.biometric.platform.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for dialog transaction
 * Author : bedash
 * Date   : 17.06.16
 */
public final class DialogTransactionRequest extends TransactionRequest {
    @JsonProperty("split_speakers")
    private final Boolean splitSpeakers = Boolean.TRUE;

    @JsonProperty("agent_id")
    private final String agentId;

    public DialogTransactionRequest(
            TransactionRequest request,
            String agentId
    ) {
        super(request.getExtension(),request.getResetSound(),request.getAudioSource());
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    public Boolean getSplitSpeakers() {
        return splitSpeakers;
    }
}
