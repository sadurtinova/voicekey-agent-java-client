package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.DialogTransactionRequest;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.structure.AudioSource;
import com.speechpro.biometric.platform.api.data.structure.BufferResult;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.xml.transform.stream.StreamResult;
import java.net.URISyntaxException;

/**
 * Author : bedash
 * Date   : 14.06.16
 */
abstract class AbstractTransaction extends AbstractRestClient {
    static final String PERSON_ROOT                = "/person";
    static final String PERSON_RESOURCE            = "/person/%s";
    static final String PERSON_MODEL_RESOURCE      = "/person/%s/model";
    static final String PERSON_SOUND_RESOURCE      = "/person/%s/model/sound";
    static final String PERSON_AUTH_RESOURCE       = "/person/%s/authentication";
    static final String PERSON_AUTH_SOUND_RESOURCE = "/person/%s/authentication/sound";

    static final String PERSON_MODEL_FILE = "/person/%s/model/file";

    final String personId;
    private final AudioSource audioSource;

    private int statusCode;
    private BufferResult soundBuffer;
    private StreamResult streamResult;

    private AbstractTransaction (Credentials credentials, Client client, Context context, String personId,
                                 String sessionId, TransactionRequest request){
        super(credentials, client, context);
        this.personId = personId;
        this.audioSource = request.getAudioSource();
        headers(SESSION_HEADER_KEY,sessionId);
    }

    AbstractTransaction(Credentials credentials, Client client, Context context, String personId,
                        String sessionId, DialogTransactionRequest request, String tranPath) throws URISyntaxException {
        this(credentials,client,context,personId,sessionId,request);
        String transactionPath = String.format(tranPath,personId);
        Response response = post(transactionPath,request);
        setResult(response);
    }

    AbstractTransaction(Credentials credentials, Client client, Context context, String personId,
                        String sessionId, TransactionRequest request, String tranPath) throws URISyntaxException {
        this(credentials,client,context,personId,sessionId,request);
        String transactionPath = String.format(tranPath,personId);
        Response response = post(transactionPath,request);
        setResult(response);
    }

    private void setResult(Response response){
        String transactionId = (String) response.getHeaders().getFirst(TRANSACTION_HEADER_KEY);
        headers(TRANSACTION_HEADER_KEY, transactionId);

        switch (this.audioSource){
            case TELEPHONY:
                if(response.getStatus()!= Response.Status.OK.getStatusCode())
                    throw new RuntimeException(String.format("Failed : HTTP error code : %s: %s", response.getStatus(), response.readEntity(String.class)));
                this.statusCode = response.getStatus();
                break;

            case SAMPLE:
                this.soundBuffer = response.readEntity(BufferResult.class);
                break;

            case STREAM:
                this.streamResult = response.readEntity(StreamResult.class);
                break;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getPersonId(){
        return  personId;
    }

    public BufferResult getSoundBuffer() {
        return soundBuffer;
    }

    public StreamResult getStreamResult() {
        return streamResult;
    }

    public AudioSource getAudioSource() {
        return audioSource;
    }

    public String getTransactionId(){
        return headersGet(TRANSACTION_HEADER_KEY);
    }
}
