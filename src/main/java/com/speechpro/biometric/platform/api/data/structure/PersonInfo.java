package com.speechpro.biometric.platform.api.data.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author : bedash
 * Date   : 10.06.16
 */
public final class PersonInfo {
    private final String personId;
    private final Boolean hasModel;
    private final int modelAge;
    private final float speechLength;

    @JsonCreator
    public PersonInfo(
            @JsonProperty("person_id") String personId,
            @JsonProperty("has_model") Boolean hasModel,
            @JsonProperty("model_age")     int modelAge,
            @JsonProperty("speech_length") float speechLength
    ){
        this.personId = personId;
        this.hasModel = hasModel;
        this.modelAge = modelAge;
        this.speechLength = speechLength;

    }

    public String getPersonId() {
        return personId;
    }

    public Boolean getHasModel() {
        return hasModel;
    }

    public int getModelAge() {
        return modelAge;
    }

    public float getSpeechLength() {
        return speechLength;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "personId='" + personId + '\'' +
                ", hasModel=" + hasModel +
                ", modelAge=" + modelAge +
                ", speechLength=" + speechLength +
                '}';
    }
}
