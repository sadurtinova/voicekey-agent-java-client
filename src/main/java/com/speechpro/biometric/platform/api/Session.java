package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.DialogTransactionRequest;
import com.speechpro.biometric.platform.api.data.DropPerson;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.structure.PersonInfo;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.speechpro.biometric.platform.api.PersonEnroll.getPersonPath;


/**
 * Session provides authentication/enrollment transactions, person management
 * Author : bedash
 * Date   : 10.06.16
 */
public final class Session extends AbstractRestClient {
    private static final String SESSION_RESOURCE = "/session";

    Session(Credentials credentials, Client client, Context context) throws URISyntaxException {
        super(credentials, client, context);
        String sessionId = (String) post(SESSION_RESOURCE, getCredentials()).getHeaders().getFirst(SESSION_HEADER_KEY);
        headers(SESSION_HEADER_KEY, sessionId);
    }

    /**
     * checks if session is valid
     * @return status code
     * @throws URISyntaxException,
     * @throws WebApplicationException if session is not valid
     */
    public int status() throws URISyntaxException {
        return get(SESSION_RESOURCE).getStatus();
    }

    /**
     * returns information about person with personId from biometric system
     * @param personId person's identifier
     * @return status code
     * @throws URISyntaxException
     * @throws WebApplicationException if personId is not found in biometric system
     */
    public PersonInfo getPersonInfo(String personId) throws URISyntaxException {
        return get(getPersonPath(personId)).readEntity(PersonInfo.class);
    }

    /**
     * deletes person with identifier personId from biometric system
     * @param personId person's identifier
     * @return status code
     * @throws URISyntaxException
     * @throws WebApplicationException if personId is not found in biometric system
     */
    public int deletePerson(String personId) throws URISyntaxException {
        return delete(getPersonPath(personId)).getStatus();
    }

    /**
     * gets all person in group
     * @param groupId
     * @return list of persons in group
     * @throws URISyntaxException
     * @throws WebApplicationException if groupId is not found in biometric system
     */
    public List<PersonInfo> getPersonInfoByGroup(String groupId) throws URISyntaxException {
        Map<String, String> qParam = new HashMap<String, String>() {{
            put("group_id", groupId);
        }};
        return get(AbstractTransaction.PERSON_ROOT, qParam).readEntity(new GenericType<List<PersonInfo>>() {
        });
    }

    /**
     * closes session
     * @return status code
     * @throws URISyntaxException
     */
    public int close() throws URISyntaxException {
        return delete(SESSION_RESOURCE).getStatus();
    }

    /**
     * merges two persons, one is deleted from the system
     * @param personId person to be merged
     * @param dropPersonId person to be merged and then deleted
     * @return status code
     * @throws URISyntaxException
     * @throws WebApplicationException if personId or dropPersonId is not found in biometric system
     */
    public int personMerge(String personId, String dropPersonId) throws URISyntaxException {
        DropPerson dropPerson = new DropPerson(dropPersonId);
        return put(getPersonPath(personId), dropPerson).getStatus();
    }

    /**
     * @return session identificator
     * @throws URISyntaxException
     */
    public String getSessionId() {
        return headersGet(SESSION_HEADER_KEY);
    }

    /**
     * @param personId
     * @param sessionId
     * @param request
     * @return PersonEnroll an object for enrollment (biometric registration) manipulations
     * @throws URISyntaxException
     * @throws WebApplicationException if personId or sessionId is not found in biometric system
     */
    public PersonEnroll getPersonEnroll(String personId, String sessionId, TransactionRequest request) throws URISyntaxException {
        return new PersonEnroll(super.getCredentials(), super.getClient(), super.getContext(), personId, sessionId, request);
    }

    /**
     * @param personId
     * @param sessionId
     * @param request
     * @return PersonEnroll an object for enrollment (biometric registration) manipulations for dialog speech
     * @throws URISyntaxException
     * @throws WebApplicationException if personId or sessionId is not found in biometric system
     */
    public PersonEnroll getPersonEnroll(String personId, String sessionId, DialogTransactionRequest request) throws URISyntaxException {
        return new PersonEnroll(super.getCredentials(), super.getClient(), super.getContext(), personId, sessionId, request);
    }

    /**
     * @param personId
     * @param sessionId
     * @param request
     * @return PersonAuth an object for authentication (biometric verification) manipulations
     * @throws URISyntaxException
     * @throws WebApplicationException if personId or sessionId is not found in biometric system
     */
    public PersonAuth getPersonAuth(String personId, String sessionId, TransactionRequest request) throws URISyntaxException {
        return new PersonAuth(super.getCredentials(), super.getClient(), super.getContext(), personId, sessionId, request);
    }

    /**
     * @param personId
     * @param sessionId
     * @param request
     * @return PersonAuth an object for authentication (biometric verification) manipulations for dialog speech
     * @throws URISyntaxException
     * @throws WebApplicationException if personId or sessionId is not found in biometric system
     */
    public PersonAuth getPersonAuth(String personId, String sessionId, DialogTransactionRequest request) throws URISyntaxException {
        return new PersonAuth(super.getCredentials(), super.getClient(), super.getContext(), personId, sessionId, request);
    }
}
