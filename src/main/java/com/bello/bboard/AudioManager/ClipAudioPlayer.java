package com.bello.bboard.AudioManager;

import com.bello.bboard.Bboard;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;

public class ClipAudioPlayer {

    private Clip soundPlayer;
    private String lastPressed = "";
    private final AudioStream audioStream;
    public ClipAudioPlayer(AudioStream audioStream) {
        this.audioStream = audioStream;
    }

    public void keyPressed(String key) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (soundPlayer == null) return;
        HashMap<String, String> keys = Bboard.config.getConfig().getKeys();

        if (keys.containsKey(key)) {
            if (soundPlayer.isRunning() && lastPressed.equals(key)) {
                soundPlayer.close();
                soundPlayer.stop();
                soundPlayer.flush();
                lastPressed = "";
                return;
            }
            lastPressed = key;
            soundPlayer.close();
            soundPlayer.stop();
            soundPlayer.flush();
            AudioInputStream stream = audioStream.getStream(keys.get(key));
            soundPlayer.open(stream);
            FloatControl control = (FloatControl) soundPlayer.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(-27.0f);
            soundPlayer.start();

        }
    }

    public void setSoundPlayer(Clip soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public void stop() {
        soundPlayer.close();
        soundPlayer.stop();
        soundPlayer.flush();
    }
}
