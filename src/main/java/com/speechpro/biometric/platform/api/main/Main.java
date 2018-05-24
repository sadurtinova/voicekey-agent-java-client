package com.speechpro.biometric.platform.api.main;

import com.speechpro.biometric.platform.api.AgentRestClient;
import com.speechpro.biometric.platform.api.PersonAuth;
import com.speechpro.biometric.platform.api.PersonEnroll;
import com.speechpro.biometric.platform.api.Session;
import com.speechpro.biometric.platform.api.data.RawData;
import com.speechpro.biometric.platform.api.data.ReplaceExisting;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.structure.AudioSource;
import com.speechpro.biometric.platform.api.data.transaction.VerificationTransaction;
import com.speechpro.biometric.platform.data.Quality;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * VoiceKey.Agent is a biometric authentication service which allows you to enroll a person into the system
 * using his/her voice sample. Later the person can be identified also by his/her voice sample.
 * VoiceKey.Agent is a text independent system, i.e. the system doesn't take into account what a person says.
 * <p>
 * <p>
 * REGISTRATION
 * <p>
 * To register a person into the system 40 seconds of speech should be recorded. VoiceKey.Agent can be integrated with
 * a contact centre audio acquisition server. In this case speech is recorded from the telephone line, the line extension
 * should be passed. In other cases speech record is passed by means of VoiceKey.Agent REST API, the extension variable
 * is set to null. In customized systems speech duration can be set to another value, but recommended duration is 40 seconds.
 * It is also possible to collect necessary amount of speech by parts. In this case the biometric system should be configured
 * so that the necessary amount of speech required for registration model is less than 40 seconds, for example, 5 or 10 seconds.
 * Total amount of necessary speech is controlled in this case by your custom application, VoiceKey.Agent only takes into
 * account the amount of speech in one part.
 * <p>
 * <p>
 * VERIFICATION
 * <p>
 * To verify a person 10 (in classic scenario) seconds of speech should be collected and sent to the biometric server.
 * This value can also be changed in customized systems, but 10 seconds is a recommended value. If more than 10 seconds
 * of speech is sent to the biometric server only last 10 seconds (configured value) will be processed.
 * <p>
 * EXAMPLE
 * <p>
 * This example shows how to enroll (register) a person with identifier PERSON to the
 * biometric authentication service and then verify his/her identity.
 * <p>
 * This is a classic scenario. Duration of speech required for enrollment and verification may vary in customized systems
 * (they are set in the system configuration file), but
 * the MOST SECURE scenario supposes 40 seconds for registration and 10 seconds for verification.
 */

public class Main {

    /**
     * Biometric platform login credentials
     *
     * @value USER
     * @value PASSWORD
     * @value DOMAIN
     * @value URI
     */
    public static final String USER = "admin";
    public static final String PASSWORD = "password";
    public static final String DOMAIN = "201";
    public static final String URI = "http://localhost:8081/vkagent/rest";

    /**
     * EXTENSION is needed if biometric system is integrated with
     * a contact centre
     */
    public static final String EXTENSION = "test";
    public static final int STEP = 10000;
    public static final String SOUND_1 = "telephone.wav";
    public static final String PERSON_ID = "test1113";

    public static void main(String[] args) {
        Main main = new Main();
        try {
            AgentRestClient agentClient = new AgentRestClient(new Credentials(USER, PASSWORD, DOMAIN), new Context(URI));
            Session session = agentClient.getSession();
            /**
             * REGISTRATION
             * If the system is integrated with the contact centre audio acquisition server,
             * EXTENSION variable is used to identify a telephone line. In other cases you don't need it.
             */
            PersonEnroll enroll = session.getPersonEnroll(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, false, AudioSource.SAMPLE));
            byte[] soundBytes = Files.readAllBytes(main.getFile(SOUND_1).toPath());
            int i = 0;
            /**
             * If required amount of speech hasn't been collected yet,
             * the model QUALITY value will be UNDEFINED. If the QUALITY value differs from UNDEFINED this means
             * that required amount of speech for the registration model has been collected.
             */
            while (enroll.getModelStatus().getQuality() == Quality.UNDEFINED && soundBytes.length > i) {
                if (soundBytes.length > i + STEP) {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, i + STEP)));
                } else {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, soundBytes.length)));
                }
                i += STEP;
            }

            /**
             * The model created during enrollment transaction will be successfully saved if required amount of speech
             * (40 seconds by default) has been collected
             */
            int enrollResult = enroll.saveModel(new ReplaceExisting(false));
            /**
             * VERIFICATION
             */
            PersonAuth authentication = session.getPersonAuth(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, true, AudioSource.SAMPLE));
            int putAuthSound = authentication.putAuthenticationSound(new RawData(Files.readAllBytes(main.getFile(SOUND_1).toPath())));
            System.out.println(putAuthSound);
            VerificationTransaction verification = authentication.getAuthenticationState();
            /**
             * Authentication score will be 0 if required amount of speech for verification models (10 seconds by default)
             * hasn't been collected
             */
            int score = (int) verification.score;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file;
    }
}
