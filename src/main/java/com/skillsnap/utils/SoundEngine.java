package com.skillsnap.utils;

import javax.sound.sampled.*;
import java.util.prefs.Preferences;

public class SoundEngine {

    // ── Singleton ─────────────────────────────────────────────
    private static SoundEngine instance;
    private boolean soundEnabled = true;

    private SoundEngine() {
        // Load saved sound preference
        Preferences prefs = Preferences.userNodeForPackage(
                SoundEngine.class);
        soundEnabled = prefs.getBoolean("soundEnabled", true);
    }

    public static SoundEngine getInstance() {
        if (instance == null) instance = new SoundEngine();
        return instance;
    }

    // ── Toggle sound on/off ───────────────────────────────────
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        Preferences prefs = Preferences.userNodeForPackage(
                SoundEngine.class);
        prefs.putBoolean("soundEnabled", enabled);
    }

    public boolean isSoundEnabled() { return soundEnabled; }

    // ── PUBLIC SOUND METHODS ──────────────────────────────────

    // Correct answer — bright ascending two-tone
    public void playCorrect() {
        if (!soundEnabled) return;
        new Thread(() -> {
            playTone(880, 80, 0.4f);   // A5 — short bright ping
            sleep(60);
            playTone(1046, 120, 0.35f); // C6 — higher confirm
        }).start();
    }
    // Button click — short soft tick
    public void playClick() {
        if (!soundEnabled) return;
        new Thread(() -> {
            playTone(600, 40, 0.25f);
        }).start();
    }

    // Wrong answer — low descending thud
    public void playWrong() {
        if (!soundEnabled) return;
        new Thread(() -> {
            playTone(300, 80, 0.5f);
            sleep(60);
            playTone(220, 150, 0.45f);
        }).start();
    }

    // Badge unlocked — celebratory three-note chime
    public void playBadgeUnlocked() {
        if (!soundEnabled) return;
        new Thread(() -> {
            playTone(659, 100, 0.4f);  // E5
            sleep(80);
            playTone(784, 100, 0.4f);  // G5
            sleep(80);
            playTone(1047, 220, 0.5f); // C6 — held note
        }).start();
    }

    // Level up — triumphant four-note fanfare
    public void playLevelUp() {
        if (!soundEnabled) return;
        new Thread(() -> {
            playTone(523, 100, 0.45f); // C5
            sleep(80);
            playTone(659, 100, 0.45f); // E5
            sleep(80);
            playTone(784, 100, 0.45f); // G5
            sleep(80);
            playTone(1047, 300, 0.6f); // C6 — held finale
        }).start();
    }

    // ── CORE TONE GENERATOR ───────────────────────────────────
    // Generates a pure sine wave tone at given frequency
    private void playTone(int freqHz, int durationMs,
                          float volume) {
        try {
            float sampleRate  = 44100f;
            int   numSamples  = (int)(sampleRate * durationMs / 1000);
            byte[] buffer     = new byte[numSamples * 2];

            // Generate sine wave samples
            for (int i = 0; i < numSamples; i++) {
                double angle = 2.0 * Math.PI * i *
                        freqHz / sampleRate;
                double sineVal = Math.sin(angle);

                // Apply fade-out envelope to avoid clicking
                double envelope = 1.0;
                int fadeLen = numSamples / 5;
                if (i > numSamples - fadeLen) {
                    envelope = (double)(numSamples - i) / fadeLen;
                }

                short sample = (short)(sineVal * envelope *
                        volume * Short.MAX_VALUE);
                buffer[2 * i]     = (byte)(sample & 0xFF);
                buffer[2 * i + 1] = (byte)((sample >> 8) & 0xFF);
            }

            // Play via Java Sound API
            AudioFormat format = new AudioFormat(
                    sampleRate, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(
                    SourceDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) return;

            SourceDataLine line =
                    (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}