package com.lesnic.licenta.app.audio_effects;

import javax.sound.sampled.AudioInputStream;

public class WavArrays {

    private WavArrays() {
    }

    private static WavArrays instance = null;

    // sample array after manipulation
    private byte[] sampleArrayWav = null;

    private short[] bitArrayWav = null;

    // current audio input stream
    private AudioInputStream audioIn = null;

    public static synchronized WavArrays getInstance() {
        if (instance == null) {
            instance = new WavArrays();
        }
        return instance;
    }

    public byte[] getSampleArrayWav() {
        return sampleArrayWav;
    }

    public void setSampleArrayWav(byte[] sampleArrayWav) {
        this.sampleArrayWav = sampleArrayWav;
    }

    public short[] getBitArrayWav() {
        return bitArrayWav;
    }

    public void setBitArrayWav(short[] bitArrayWav) {
        this.bitArrayWav = bitArrayWav;
    }

    public AudioInputStream getAudioIn() {
        return audioIn;
    }

    public void setAudioIn(AudioInputStream audioIn) {
        this.audioIn = audioIn;
    }

}
