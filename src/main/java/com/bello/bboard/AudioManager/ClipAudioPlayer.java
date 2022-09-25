package com.bello.bboard.AudioManager;

import com.bello.bboard.Bboard;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;

public class ClipAudioPlayer {

    private Clip soundPlayer;
    private String lastPressed = "";
    private boolean isRunning = false;
    private boolean isStopped = true;
    private final AudioStream audioStream;
    public ClipAudioPlayer(AudioStream audioStream) {
        this.audioStream = audioStream;
    }

    public void keyPressed(String key) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (soundPlayer == null || isStopped) return;
        HashMap<String, String> keys = Bboard.config.getConfig().getKeys();

        if (keys.containsKey(key)) {
            if (soundPlayer.isRunning() && lastPressed.equals(key)) {
                soundPlayer.close();
                soundPlayer.stop();
                soundPlayer.flush();
                lastPressed = "";
                isRunning = false;
                return;
            }
            lastPressed = key;
            soundPlayer.close();
            soundPlayer.stop();
            soundPlayer.flush();
            AudioInputStream stream = audioStream.getStream(keys.get(key));
            soundPlayer.open(stream);
            FloatControl control = (FloatControl) soundPlayer.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = Bboard.config.getConfig().getVolume();
            if (volume > 6.0206) volume = 6.0206f;
            if (volume < -80.0f) volume = -80.0f;

            control.setValue(volume);
            soundPlayer.start();
            isRunning = true;
        }
    }

    public void setSoundPlayer(Clip soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void stop() {
        this.setRunning(false);
        if (soundPlayer == null) return;
        soundPlayer.close();
        soundPlayer.stop();
        soundPlayer.flush();
    }
}
