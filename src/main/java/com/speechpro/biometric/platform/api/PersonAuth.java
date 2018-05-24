package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.DialogTransactionRequest;
import com.speechpro.biometric.platform.api.data.RawData;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.transaction.VerificationTransaction;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import java.net.URISyntaxException;

/**
 * {@link PersonAuth} provides authentication transaction for person
 * Not all REST API methods wrapped into this client library,
 * consult REST API Swagger documentation to learn other methods
 * Author : bedash
 * Date   : 13.06.16
 */
public final class PersonAuth extends AbstractTransaction {
    /**
     * Person auth in monologue mode
     */
    PersonAuth(Credentials credentials, Client client, Context context,
               String personId, String sessionId, TransactionRequest request) throws URISyntaxException {
        super(credentials, client, context, personId, sessionId, request, PERSON_AUTH_RESOURCE);
    }

    /**
     * Person auth in dialog mode
     */
    PersonAuth(Credentials credentials, Client client, Context context,
               String personId, String sessionId, DialogTransactionRequest request) throws URISyntaxException {
        super(credentials, client, context, personId, sessionId, request, PERSON_AUTH_RESOURCE);
    }

    /**
     * sends raw bytes (with no header) to create voice model for verification
     * this model will be compared with model in biometric system if there is enough speech in this data
     *
     * @param data
     * @return status code
     * @throws URISyntaxException
     * @throws WebApplicationException if personId is not found in biometric system
     */
    public int putAuthenticationSound(RawData data) throws URISyntaxException {
        String soundPath = String.format(PERSON_AUTH_SOUND_RESOURCE, personId);
        return put(soundPath, data).getStatus();
    }

    /**
     * @return VerificationTransaction - information about verification transaction
     * if verification model had less speech than required by biometric system
     * score field will be 0, i.e there will be no verification result
     * @throws URISyntaxException
     * @throws WebApplicationException if personId is not found in biometric system
     */
    public VerificationTransaction getAuthenticationState() throws URISyntaxException {
        String modelPath = String.format(PERSON_AUTH_RESOURCE, personId);
        return get(modelPath).readEntity(VerificationTransaction.class);
    }

    /**
     * deletes model created during verification transaction
     *
     * @return status code
     * @throws URISyntaxException
     * @throws WebApplicationException if personId is not found in biometric system
     */
    public int deleteAuthModel() throws URISyntaxException {
        String modelPath = String.format(PERSON_AUTH_RESOURCE, personId);
        return delete(modelPath).getStatus();
    }
}
