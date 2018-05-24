package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.DialogTransactionRequest;
import com.speechpro.biometric.platform.api.data.RawData;
import com.speechpro.biometric.platform.api.data.ReplaceExisting;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.transaction.EnrollmentTransaction;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import java.io.File;
import java.net.URISyntaxException;

/**
 * {@link PersonEnroll} provides enrollment transaction for person
 * Not all REST API methods wrapped into this client library,
 * consult REST API Swagger documentation to learn other methods
 * Author : bedash
 * Date   : 10.06.16
 */
public final class PersonEnroll extends AbstractTransaction {
    /**
     * Person enroll in monologue mode
     */
    PersonEnroll(Credentials credentials, Client client, Context context,
                 String personId, String sessionId, TransactionRequest request) throws URISyntaxException {
        super(credentials, client, context, personId, sessionId, request, PERSON_MODEL_RESOURCE);
    }

    /**
     * Person enroll in dialogue mode
     */
    PersonEnroll(Credentials credentials, Client client, Context context,
                 String personId, String sessionId, DialogTransactionRequest request) throws URISyntaxException {
        super(credentials, client, context, personId, sessionId, request, PERSON_MODEL_RESOURCE);
    }

    /**
     * sends raw bytes (with no header) to create voice model
     * if data is sent in one transaction several times all data will be added to one model,
     * i.e. model can be created in several steps
     * @param data
     * @return status code
     * @throws URISyntaxException
     */
    public int putModelSound(RawData data) throws URISyntaxException {
        String soundPath = String.format(PERSON_SOUND_RESOURCE, personId);
        return put(soundPath, data).getStatus();
    }

    /**
     * saves person's model in biometric system if there is enough speech in model
     * @param replace if person already has model, it will be replace by new one
     * @return
     * @throws URISyntaxException
     * @throws WebApplicationException if model contains less SPEECH than required according
     * to system configuration (40 seconds by default)
     */
    public int saveModel(ReplaceExisting replace) throws URISyntaxException {
        String modelPath = String.format(PERSON_MODEL_RESOURCE, personId);
        return put(modelPath, replace).getStatus();
    }

    /**
     * deletes model created in enrollment transaction
     * @return status code
     * @throws URISyntaxException
     */
    public int deleteModel() throws URISyntaxException {
        String modelPath = String.format(PERSON_MODEL_RESOURCE, personId);
        return delete(modelPath).getStatus();
    }

    /**
     * gets characteristics of model created in enrollment transaction
     * @return EnrollmentTransaction
     * @throws URISyntaxException
     */
    public EnrollmentTransaction getModelStatus() throws URISyntaxException {
        String modelPath = String.format(PERSON_MODEL_RESOURCE, personId);
        return get(modelPath).readEntity(EnrollmentTransaction.class);
    }

    /**
     * sends sound file to create voice model
     * if data is sent several times in one transaction all data will be added to one model,
     * i.e. model can be created in several steps
     * @param file
     * @return status code
     * @throws URISyntaxException
     */
    public int putFile(File file) throws URISyntaxException {
        String modelPath = String.format(PERSON_MODEL_FILE, personId);
        return put(modelPath, file).getStatus();
    }

    static String getPersonPath(String personId) {
        return String.format(PERSON_RESOURCE, personId);
    }
}
