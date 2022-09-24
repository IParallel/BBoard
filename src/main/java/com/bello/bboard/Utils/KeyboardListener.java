package com.bello.bboard.Utils;

import com.bello.bboard.Bboard;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;

public class KeyboardListener implements NativeKeyListener {

    private String lastPresed = "";
    private volatile boolean isStillSomethingPressed = false;
    public KeyboardListener() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            //System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (!keys.getOrDefault(NativeKeyEvent.getKeyText(e.getKeyCode()), false)) {
            //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            keys.put(NativeKeyEvent.getKeyText(e.getKeyCode()), true);
        }
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        if (isStillSomethingPressed) {
            if (lastPresed.contains(keyText)) return;
            lastPresed += "+" + keyText;
            return;
        }
        lastPresed += keyText;
        isStillSomethingPressed = true;
        Thread thread = new Thread(()->{
            while (isStillSomethingPressed) {
                Thread.onSpinWait();
            }
            try {
                //System.out.println(lastPresed.toUpperCase());
                Bboard.audioStream.getClipAudioPlayer().keyPressed(lastPresed.toUpperCase());
                lastPresed = "";
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            Thread.currentThread().interrupt();
        });

        thread.start();

    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (keys.getOrDefault(NativeKeyEvent.getKeyText(e.getKeyCode()), false)) {
            //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            keys.remove(NativeKeyEvent.getKeyText(e.getKeyCode()));
        }
        isStillSomethingPressed = false;
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    private final HashMap<String, Boolean> keys = new HashMap<>();
    public boolean isKeyPressed(String key) {
        return keys.getOrDefault(key, false);
    }
}
