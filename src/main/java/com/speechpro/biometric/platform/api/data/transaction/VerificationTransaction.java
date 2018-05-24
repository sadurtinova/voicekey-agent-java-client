package com.speechpro.biometric.platform.api.data.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.speechpro.biometric.platform.data.Gender;
import com.speechpro.biometric.platform.data.Quality;
import com.speechpro.biometric.platform.data.SpoofingDecision;

/**
 * Created by sadurtinova on 10.05.2018.
 */
public class VerificationTransaction extends EnrollmentTransaction{

    public final float score;

    public VerificationTransaction(@JsonProperty(value = "gender") Gender gender,
                                   @JsonProperty(value = "spoofing_decision") SpoofingDecision spoofingDecision,
                                   @JsonProperty(value = "quality") Quality quality,
                                   @JsonProperty(value = "speech_required") float speechRequired,
                                   @JsonProperty(value = "speech_collected") float speechCollected,
                                   @JsonProperty(value = "score") float score) {
        super(gender, spoofingDecision, quality, speechRequired, speechCollected);
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(super.toString())
                .add("score", score)
                .toString();
    }
}
