package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * Author : bedash
 * Date   : 10.06.16
 */
abstract class AbstractRestClient {
    final private Credentials credentials;
    final private Client client;
    final private Context context;

    private Map<String, String> headers = new ConcurrentHashMap<>();

    static final String SESSION_HEADER_KEY = "X-Session-Id";
    static final String TRANSACTION_HEADER_KEY = "X-Transaction-Id";

    private final static List<Integer> errorCodes = new ArrayList<Integer>() {{
        add(500);
        add(400);
        add(401);
        add(403);
        add(404);
    }};

    AbstractRestClient(Credentials credentials, Client client, Context context) {
        this.credentials = credentials;
        this.client = client;
        this.context = context;
    }

    Credentials getCredentials() {
        return credentials;
    }

    Context getContext() {
        return context;
    }

    Client getClient() {
        return client;
    }

    Response post(String resourcePath, Object body) throws URISyntaxException {
        Response response = prepareClientRequest(resourcePath)
                .post(Entity.entity(body, APPLICATION_JSON_TYPE));
        checkStatus(response);
        return response;
    }

    private Invocation.Builder prepareClientRequest(String resourcePath) throws URISyntaxException {
        return prepareClientRequest(resourcePath, new HashMap<>());
    }

    private Invocation.Builder prepareClientRequest(String resourcePath, Map<String, String> queryParam) throws URISyntaxException {
        URI uri = new URI(context.getAgentUrl() + resourcePath);
        WebTarget webTarget = client.target(uri);

        for (Map.Entry<String, String> par : queryParam.entrySet())
            webTarget = webTarget.queryParam(par.getKey(), par.getValue());

        Invocation.Builder builder = webTarget.request(APPLICATION_JSON_TYPE);

        for (Map.Entry<String, String> hed : headers.entrySet())
            builder = builder.header(hed.getKey(), hed.getValue());
        return builder;
    }

    Response delete(String resourcePath) throws URISyntaxException {
        Response response = prepareClientRequest(resourcePath).delete();
        checkStatus(response);
        return response;
    }

    Response put(String resourcePath, Object body) throws URISyntaxException {
        Response response = prepareClientRequest(resourcePath)
                .put(Entity.entity(body, APPLICATION_JSON_TYPE));
        checkStatus(response);
        return response;
    }

    Response get(String resourcePath) throws URISyntaxException {
        return get(resourcePath, new HashMap<>());
    }

    Response get(String resourcePath, Map<String, String> queryParam) throws URISyntaxException {
        Response response = prepareClientRequest(resourcePath, queryParam).get();
        checkStatus(response);
        return response;
    }

    private void checkStatus(Response response) {
        if (response == null)
            throw new ServiceUnavailableException("Agent service is unavailable");

        if (errorCodes.contains(response.getStatus()))
            throw new WebApplicationException(
                    String.format("Failed : HTTP error code : %s: %s", response.getStatus(), response.readEntity(String.class)),
                    response
            );
    }

    void headers(final String name, final String value) {
        if (value == null) {
            headers.remove(name);
        } else {
            headers.put(name, value);
        }
    }


    String headersGet(final String name) {
        return headers.get(name);
    }
}
