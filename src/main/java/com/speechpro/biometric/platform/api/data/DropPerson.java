package com.speechpro.biometric.platform.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author : bedash
 * Date   : 12.06.16
 */
public final class DropPerson {
    @JsonProperty("drop_person_id")
    private final String dropPersonId;

    public DropPerson(String dropPersonId){
        this.dropPersonId = dropPersonId;
    }

    public String getDropPersonId() {
        return dropPersonId;
    }
}
