package com.bello.bboard.Utils;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioHandler {


    private static HashMap<String, Mixer.Info> outPutMixers = new HashMap<>();
    private static HashMap<String, Mixer.Info> inputMixers = new HashMap<>();

    public static void filterDevices() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info inputInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info outputInfo = new DataLine.Info(SourceDataLine.class, format);
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(inputInfo)) {
                inputMixers.put(mixer.getMixerInfo().getName(), info);
            } else if (mixer.isLineSupported(outputInfo)) {
                outPutMixers.put(mixer.getMixerInfo().getName(), info);
            }
        }
    }

    public static Mixer.Info getInputDevice(String name) {
        for (String mixerName : inputMixers.keySet())
        {
            if (mixerName.matches(name)) return inputMixers.get(mixerName);
        }
        return null;
    }
    public static Mixer.Info getOutputDevice(String name) {
        for (String mixerName : outPutMixers.keySet())
        {
            if (mixerName.matches(name)) return outPutMixers.get(mixerName);
        }
        return null;
    }

    public static HashMap<String, Mixer.Info> getOutPutMixers() {
        return outPutMixers;
    }

    public static HashMap<String, Mixer.Info> getInputMixers() {
        return inputMixers;
    }
}
