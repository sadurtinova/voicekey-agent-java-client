package com.speechpro.biometric.platform.api;

import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.mapper.ObjectMapperProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import java.net.URISyntaxException;

/**
 * AgentRestClient - REST client for agent, you must use it for
 * session creating.
 *
 * Author : bedash
 * Date   : 10.06.16
 */
public final class AgentRestClient {
    private final Credentials cred;
    private final Client client;
    private final Context context;

    public AgentRestClient(Credentials cred, Context context,
                           Integer readTimeOut, Integer connectTimeOut) throws URISyntaxException {
        ClientConfig clientConfig = getDefaultConfig();

        clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeOut);
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectTimeOut);

        this.client = JerseyClientBuilder.createClient(clientConfig);
        this.cred = cred;
        this.context = context;
    }

    public AgentRestClient(Credentials cred, Context context) throws URISyntaxException {
        this.client = JerseyClientBuilder.createClient(getDefaultConfig());
        this.cred = cred;
        this.context = context;
    }

    private ClientConfig getDefaultConfig(){
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION,Boolean.TRUE);
        clientConfig.register(JacksonFeature.class);
        clientConfig.register(ObjectMapperProvider.class);
        return clientConfig;
    }

    public Session getSession() throws URISyntaxException {
        return new Session(cred, client, context);
    }
}
