package com.bello.bboard.Utils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;

public class AudioHandler {

    public static DevicesList getAdapters() {
        final List<String> outputList = new ArrayList<>();
        final List<String> inputList = new ArrayList<>();
        final Mixer.Info[] devices = AudioSystem.getMixerInfo();
        final Line.Info sourceInfo = new Line.Info(SourceDataLine.class);

        for (final Mixer.Info mixerInfo : devices) {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Mixer.Info info = mixer.getMixerInfo();
            final String data = info.getName() + " - " + info.getVendor();
            if (mixer.isLineSupported(sourceInfo)) {
                // the device supports output, add as suitable
                outputList.add(data);
                continue;
            }
            inputList.add(data);
        }
        return new DevicesList(outputList, inputList);
    }




}
