import com.speechpro.biometric.platform.api.AgentRestClient;
import com.speechpro.biometric.platform.api.PersonAuth;
import com.speechpro.biometric.platform.api.PersonEnroll;
import com.speechpro.biometric.platform.api.Session;
import com.speechpro.biometric.platform.api.data.RawData;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.structure.AudioSource;
import com.speechpro.biometric.platform.api.data.transaction.VerificationTransaction;
import com.speechpro.biometric.platform.data.Quality;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by sadurtinova on 11.05.2018.
 */
public class VerificationTest {
    private static final String SOUND_1 = "telephone.wav";
    private static final String SOUND_2 = "own_6944.wav";
    private static final String PERSON_ID = "test1114";
    private static final String NON_EXISTING_PERSON_ID = "notexist";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";
    private static final String DOMAIN = "201";
    private static final String URI = "http://localhost:8081/vkagent/rest";
    private static final String EXTENSION = "test";
    private static final int STEP = 100000;
    private static AgentRestClient agentClient;
    private static VerificationTest verificationTest;

    @BeforeClass
    public static void enrollPerson() {
        verificationTest = new VerificationTest();
        try {
            agentClient = new AgentRestClient(
                    new Credentials(USER,
                            PASSWORD,
                            DOMAIN),
                    new Context(URI));
            Session session = agentClient.getSession();
            PersonEnroll enroll = session.getPersonEnroll(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, false, AudioSource.SAMPLE));
            byte[] soundBytes = Files.readAllBytes(verificationTest.getFile(SOUND_1).toPath());
            int i = 0;
            while (enroll.getModelStatus().getQuality() == Quality.UNDEFINED && soundBytes.length > i) {
                if (soundBytes.length > i + STEP) {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, i + STEP)));
                } else {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, soundBytes.length)));
                }
                i += STEP;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = WebApplicationException.class)
    public void testVerification_PersonNotEnrolled_ThrowsWebApplicationException() {
        try {
            Session session = agentClient.getSession();
            PersonAuth authentification = session.getPersonAuth(NON_EXISTING_PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, true, AudioSource.SAMPLE));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerification_WithSameVoice_ReturnsScore98() {
        Session session = null;
        try {
            session = agentClient.getSession();
            PersonAuth authentification = session.getPersonAuth(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, true, AudioSource.SAMPLE));
            int putAuthSound = authentification.putAuthenticationSound(
                    new RawData(Files.readAllBytes(verificationTest.getFile(SOUND_1).toPath())));
            System.out.println(putAuthSound);
            VerificationTransaction verification = authentification.getAuthenticationState();
            int score = (int) verification.score;
            Assert.assertEquals(98, score, 0);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerification_NotEnoughSpeech_ReturnsScore0() {
        try {
            Session session = agentClient.getSession();
            PersonAuth authentification = session.getPersonAuth(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, true, AudioSource.SAMPLE));
            int putAuthSound = authentification.putAuthenticationSound(
                    new RawData(Files.readAllBytes(verificationTest.getFile(SOUND_2).toPath())));
            VerificationTransaction verification = authentification.getAuthenticationState();
            int score = (int) verification.score;
            Assert.assertEquals(0, score, 0);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
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
