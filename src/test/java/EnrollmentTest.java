import com.speechpro.biometric.platform.api.AgentRestClient;
import com.speechpro.biometric.platform.api.PersonEnroll;
import com.speechpro.biometric.platform.api.Session;
import com.speechpro.biometric.platform.api.data.RawData;
import com.speechpro.biometric.platform.api.data.ReplaceExisting;
import com.speechpro.biometric.platform.api.data.TransactionRequest;
import com.speechpro.biometric.platform.api.data.config.Context;
import com.speechpro.biometric.platform.api.data.config.Credentials;
import com.speechpro.biometric.platform.api.data.structure.AudioSource;
import com.speechpro.biometric.platform.api.data.structure.PersonInfo;
import com.speechpro.biometric.platform.data.Quality;
import org.apache.commons.io.FileUtils;
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

public class EnrollmentTest {

    private static final String SOUND_1 = "telephone.wav";
    private static final String SOUND_2 = "own_6944.wav";
    public static final String PERSON_ID = "test1114";
    public static final String NON_EXISTING_PERSON_ID = "notexist";
    public static final String USER = "admin";
    public static final String PASSWORD = "password";
    public static final String DOMAIN = "201";
    public static final String URI = "http://localhost:8081/vkagent/rest";
    public static final String EXTENSION = "test";
    public static final int STEP = 100000;
    public static AgentRestClient agentClient;
    public static EnrollmentTest enrollmentTest;

    @BeforeClass
    public static void createClient() {
        enrollmentTest = new EnrollmentTest();
        try {
            agentClient = new AgentRestClient(
                    new Credentials(USER,
                            PASSWORD,
                            DOMAIN),
                    new Context(URI));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Test(expected = WebApplicationException.class)
    public void testPerson_PersonNotExists_ThrowsWebApplicationException() {
        Session session;
        try {
            session = agentClient.getSession();
            PersonInfo personInfo = session.getPersonInfo(NON_EXISTING_PERSON_ID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPerson_PersonExists_ReturnPersonInfoObject() {
        Session session;
        try {
            session = agentClient.getSession();
            PersonEnroll enroll = session.getPersonEnroll(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, false, AudioSource.SAMPLE));
            enroll.putModelSound(new RawData(FileUtils.readFileToByteArray(getFile(SOUND_1))));
            enroll.saveModel(new ReplaceExisting(false));
            PersonInfo personInfo = session.getPersonInfo(PERSON_ID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPersonEnrollment_ModelSavedSuccessfully_ReturnsTrue() {
        Session session = null;
        try {
            session = agentClient.getSession();
            PersonEnroll enroll = session.getPersonEnroll(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, false, AudioSource.SAMPLE));
            byte[] soundBytes = Files.readAllBytes(enrollmentTest.getFile(SOUND_1).toPath());
            int i = 0;
            while (enroll.getModelStatus().getQuality() == Quality.UNDEFINED && soundBytes.length > i) {
                if (soundBytes.length > i + STEP) {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, i + STEP)));
                } else {
                    enroll.putModelSound(new RawData(Arrays.copyOfRange(soundBytes, i, soundBytes.length)));
                }
                i += STEP;
            }
            int enrollResult = enroll.saveModel(new ReplaceExisting(false));
            Assert.assertEquals(204, enrollResult, 0);
            PersonInfo personInfo = session.getPersonInfo(PERSON_ID);
            Assert.assertTrue(personInfo.getHasModel());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Test(expected = WebApplicationException.class)
    public void testPersonEnrollment_PersonNotHaveEnoughSpeechInModel_ThrowsWebApplicationException() {
        Session session;
        try {
            session = agentClient.getSession();
            PersonEnroll enroll = session.getPersonEnroll(PERSON_ID, session.getSessionId(),
                    new TransactionRequest(EXTENSION, false, AudioSource.SAMPLE));
            enroll.putModelSound(new RawData(Files.readAllBytes(enrollmentTest.getFile(SOUND_2).toPath())));
            enroll.saveModel(new ReplaceExisting(false));
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
