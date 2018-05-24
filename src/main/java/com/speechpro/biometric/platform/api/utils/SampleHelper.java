package com.speechpro.biometric.platform.api.utils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Author : bedash
 * Date   : 21.06.16
 */
public class SampleHelper {
    private static final Logger logger = Logger.getLogger(SampleHelper.class.getName());
    private static AudioFormat ethalonAudioFormat;

    public static AudioFormat getEthalonAudioFormat(){
        return ethalonAudioFormat;
    }

    public static void setEthalonSampleRate(int sampleRate){
        if(ethalonAudioFormat == null || (int)ethalonAudioFormat.getFrameRate() != sampleRate){
            ethalonAudioFormat = new AudioFormat(sampleRate, 16, 1, true, false);
        }
    }

    static {
        setEthalonSampleRate(11025);
    }

    public static Sample loadFromWav(File soundFile){
        Sample result = null;

        try {
            AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(soundFile);
            AudioFormat format = audioFileFormat.getFormat();

            if(!format.matches(ethalonAudioFormat)){
                String mess = String.format("File '%s' has unexpected format '%s' and will not be processed. Expected format '%s'",
                        soundFile.getAbsolutePath(), format.toString(), ethalonAudioFormat.toString());
                logger.warning(mess);
                return null;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile)) {
                int bytesPerFrame = format.getFrameSize();

                int numBytes = 1024 * bytesPerFrame;
                byte[] audioBytes = new byte[numBytes];
                int numBytesRead = 0;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
                    byteArrayOutputStream.write(audioBytes, 0, numBytesRead);
                }
                result = new Sample(byteArrayOutputStream.toByteArray(), (int) format.getFrameRate());
                //logger.debug("Loaded %d bytes Sample from %s (%s)", result.json.length, soundFile.getAbsolutePath(), format.toString());
            }
        }
        catch (Exception ex){
            String mess =String.format("Failed to load Sample from file '%s'", soundFile.getAbsolutePath());
            logger.warning(mess);
        }
        return result;
    }

    public static List<File> getFiles(String folderName){
        List<File> result = new ArrayList<>();
        File folder = new File(folderName);
        if(folder.exists()){
            for (final File file : folder.listFiles()) {
                if (file.isFile()) {
                    result.add(file);
                }
            }
        }
        return result;
    }
}
