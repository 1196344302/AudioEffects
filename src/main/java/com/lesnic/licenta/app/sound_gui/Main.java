package com.lesnic.licenta.app.sound_gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {

    public static void main(String[] args) {
        File file = new File("sounds/218475__xserra__impulse-response.wav");
        setByteArray(file);

    }

    public static void setByteArray(File soundFile) {
        int byteArraySize;

        byte[] audioArray;

        // length of the stream, expressed in sample frames rather than bytes.
        int frameLenght;

        // the number of bytes per frame
        int frameSize;

        try {

            AudioInputStream audioIn = AudioSystem
                    .getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                    audioFormat);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem
                    .getLine(info);
            audioLine.open(audioFormat);

            audioLine.start();
            frameLenght = (int) audioIn.getFrameLength();
            frameSize = audioFormat.getFrameSize();
            byteArraySize = frameLenght * frameSize;
            audioArray = new byte[byteArraySize];
            audioIn.read(audioArray);

            short[] bit16Array = new short[byteArraySize / 2];
            System.out.println(audioFormat);
            if (audioFormat.isBigEndian()) {
                for (int i = 0; i < byteArraySize / 2; i++) {
                    bit16Array[i] = getSampleBigEndian(audioArray, i);
                }

            } else {
                for (int i = 0; i < byteArraySize / 2; i++) {
                    bit16Array[i] = getSampleLittleEndian(audioArray, i);
                }
            }

            writeTxt(bit16Array, "ir");
            audioLine.drain();
            audioLine.close();
            audioIn.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void writeTxt(short[] byteArray, String fileName) {
        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter(new File(fileName + ".txt"));
            for (int i = 0; i < byteArray.length; i++) {
                txtWriter.println(byteArray[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            txtWriter.close();
        }
    }

    public static short getSampleLittleEndian(byte[] buffer, int position) {
        return (short) (((buffer[2 * position + 1] & 0xff) << 8) | (buffer[2 * position] & 0xff));
    }

    public static void setSampleLittleEndian(byte[] buffer, int position,
            short sample) {
        buffer[2 * position] = (byte) (sample);
        buffer[2 * position + 1] = (byte) (sample >> 8);

    }

    public static short getSampleBigEndian(byte[] buffer, int position) {
        return (short) (((buffer[position] & 0xff) << 8) | (buffer[position + 1] & 0xff));
    }

    public static void setSampleBigEndian(byte[] buffer, int position,
            short sample) {
        buffer[position + 1] = (byte) (sample & 0xff);
        buffer[position] = (byte) ((sample >> 8) & 0xff);
    }
}
