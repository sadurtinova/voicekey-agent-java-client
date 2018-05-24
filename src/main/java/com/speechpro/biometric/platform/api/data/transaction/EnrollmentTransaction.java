package com.speechpro.biometric.platform.api.data.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.speechpro.biometric.platform.data.Gender;
import com.speechpro.biometric.platform.data.Quality;
import com.speechpro.biometric.platform.data.SpoofingDecision;

/**
 * Created by sadurtinova on 10.05.2018.
 */
public class EnrollmentTransaction {

    protected final Gender gender;
    protected final SpoofingDecision spoofingDecision;
    protected final Quality quality;
    protected final float speech_required;
    protected final float speech_collected;

    public Gender getGender() {
        return gender;
    }

    public SpoofingDecision getSpoofingDecision() {
        return spoofingDecision;
    }

    public Quality getQuality() {
        return quality;
    }

    public float getSpeech_required() {
        return speech_required;
    }

    public float getSpeech_collected() {
        return speech_collected;
    }

    @JsonCreator
    public EnrollmentTransaction(@JsonProperty(value = "gender") Gender gender,
                                 @JsonProperty(value = "spoofing_decision") SpoofingDecision spoofingDecision,
                                 @JsonProperty(value = "quality") Quality quality,
                                 @JsonProperty(value = "speech_required") float speechRequired,
                                 @JsonProperty(value = "speech_collected") float speechCollected) {

        this.gender = gender;
        this.spoofingDecision = spoofingDecision;
        this.quality = quality;
        this.speech_required = speechRequired;
        this.speech_collected = speechCollected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentTransaction that = (EnrollmentTransaction) o;
        return Float.compare(that.speech_required, speech_required) == 0 &&
                Float.compare(that.speech_collected, speech_collected) == 0 &&
                gender == that.gender &&
                spoofingDecision == that.spoofingDecision &&
                Objects.equal(quality, that.quality);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gender, spoofingDecision, quality, speech_required, speech_collected);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("gender", gender)
                .add("spoofingDecision", spoofingDecision)
                .add("quality", quality)
                .add("speech_required", speech_required)
                .add("speech_collected", speech_collected)
                .toString();
    }
}
