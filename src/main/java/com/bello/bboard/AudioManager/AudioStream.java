package com.bello.bboard.AudioManager;

import com.bello.bboard.Bboard;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AudioStream {
    private boolean stop = false;
    private boolean isRunning = false;

    private boolean playingSound = false;

    private ClipAudioPlayer clipAudioPlayer;

    public AudioStream() {
        clipAudioPlayer = new ClipAudioPlayer(this);
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }

    public void start() {
        try {
            if (isRunning) return;
            isRunning = true;
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Not Supported");
            }

            List<Mixer.Info> inputInfos = filterDevices(info);
            Mixer inputMixer = null;
            for (Mixer.Info in : inputInfos) {
                if (Objects.equals(in.getName(), Bboard.config.getConfig().getInputAdapter())) inputMixer = AudioSystem.getMixer(in);
            }
            TargetDataLine dataLine = (TargetDataLine) inputMixer.getLine(info);
            dataLine.open();
            dataLine.start();
            int CHUNK_SIZE = 1024;
            DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, format);
            List<Mixer.Info> infos = filterDevices(info2);
            Mixer outputMixer = null;
            for (Mixer.Info in : infos) {
                if (Objects.equals(in.getName(), Bboard.config.getConfig().getOutputAdapter())) outputMixer = AudioSystem.getMixer(in);
            }
            SourceDataLine sourceDataLine = (SourceDataLine) outputMixer.getLine(info2);

            sourceDataLine.open();
            sourceDataLine.start();
            Thread micLoopBack = new Thread(() -> {
                int actualReading = 0;
                byte[] data = new byte[dataLine.getBufferSize() / 5];
                while (!isStop()) {
                    actualReading = dataLine.read(data, 0, CHUNK_SIZE);
                    sourceDataLine.write(data, 0, actualReading);
                }
                setStop(false);
                isRunning = false;
                clipAudioPlayer.stop();
                sourceDataLine.stop();
                sourceDataLine.close();
                dataLine.stop();
                dataLine.close();
            });
            micLoopBack.start();


            Clip clip = AudioSystem.getClip(outputMixer.getMixerInfo());
            clipAudioPlayer.setSoundPlayer(clip);
            //TODO:

            //JOptionPane.showMessageDialog(null, "Hit ok to stop");

        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ClipAudioPlayer getClipAudioPlayer() {
        return clipAudioPlayer;
    }

    public AudioInputStream getStream(String path) throws UnsupportedAudioFileException, IOException {
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        AudioFormat sourceFormat = mp3Stream.getFormat();
        // create audio format object for the desired stream/audio format
        // this is *not* the same as the file format (wav)
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        // create stream that delivers the desired format
        return AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
    }

    private List<Mixer.Info> filterDevices(final Line.Info supportedLine) {
        List<Mixer.Info> result = new ArrayList<>();

        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(supportedLine)) {
                result.add(info);
            }
        }
        return result;
    }

    public void debugg() throws LineUnavailableException {

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        for (Mixer.Info mx : mixerInfo) {

            Mixer mixer = AudioSystem.getMixer(mx); // default mixer
            mixer.open();

            System.out.printf("Supported SourceDataLines of default mixer (%s):\n\n", mixer.getMixerInfo().getName());
            for(Line.Info info : mixer.getSourceLineInfo()) {
                if (SourceDataLine.class.isAssignableFrom(info.getLineClass())) {
                    SourceDataLine.Info info2 = (SourceDataLine.Info) info;
                    System.out.println(info2);
                    System.out.printf("  max buffer size: \t%d\n", info2.getMaxBufferSize());
                    System.out.printf("  min buffer size: \t%d\n", info2.getMinBufferSize());
                    AudioFormat[] formats = info2.getFormats();
                    System.out.println("  Supported Audio formats: ");
                    for (AudioFormat format : formats) {
                        System.out.println("    " + format);
                        System.out.printf("      encoding:           %s\n", format.getEncoding());
                        System.out.printf("      channels:           %d\n", format.getChannels());
//                        System.out.printf(format.getFrameRate()==-1?"":"      frame rate [1/s]:   %s\n", format.getFrameRate());
//                        System.out.printf("      frame size [bytes]: %d\n", format.getFrameSize());
                        System.out.printf(format.getSampleRate() == -1 ? "" : "      sample rate [1/s]:  %s\n", format.getSampleRate());
//                        System.out.printf("      sample size [bit]:  %d\n", format.getSampleSizeInBits());
//                        System.out.printf("      big endian:         %b\n", format.isBigEndian());
//
//                        Map<String,Object> prop = format.properties();
//                        if(!prop.isEmpty()) {
//                            System.out.println("      Properties: ");
//                            for(Map.Entry<String, Object> entry : prop.entrySet()) {
//                                System.out.printf("      %s: \t%s\n", entry.getKey(), entry.getValue());
//                            }
//                        }
                    }
                    System.out.println();
                } else {
                    if (TargetDataLine.class.isAssignableFrom(info.getLineClass())) {
                        TargetDataLine.Info info2 = (TargetDataLine.Info) info;
                        System.out.println(info2);
                        System.out.printf("  max buffer size: \t%d\n", info2.getMaxBufferSize());
                        System.out.printf("  min buffer size: \t%d\n", info2.getMinBufferSize());
                        AudioFormat[] formats = info2.getFormats();
                        System.out.println("  Supported Audio formats: ");
                        for (AudioFormat format : formats) {
                            System.out.println("    " + format);
                            System.out.printf("      encoding:           %s\n", format.getEncoding());
                            System.out.printf("      channels:           %d\n", format.getChannels());
//                        System.out.printf(format.getFrameRate()==-1?"":"      frame rate [1/s]:   %s\n", format.getFrameRate());
//                        System.out.printf("      frame size [bytes]: %d\n", format.getFrameSize());
                            System.out.printf(format.getSampleRate() == -1 ? "" : "      sample rate [1/s]:  %s\n", format.getSampleRate());
//                        System.out.printf("      sample size [bit]:  %d\n", format.getSampleSizeInBits());
//                        System.out.printf("      big endian:         %b\n", format.isBigEndian());
//
//                        Map<String,Object> prop = format.properties();
//                        if(!prop.isEmpty()) {
//                            System.out.println("      Properties: ");
//                            for(Map.Entry<String, Object> entry : prop.entrySet()) {
//                                System.out.printf("      %s: \t%s\n", entry.getKey(), entry.getValue());
//                            }
//                        }
                        }
                        System.out.println();
                        System.out.println(info);
                    }
                    System.out.println();
                }
            }

            mixer.close();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
