package com.lesnic.licenta.app.audio_effects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.jtransforms.fft.DoubleFFT_1D;

import com.lesnic.licenta.app.utils.Complex;

public class FFTImpl {

    private double[] freg;
    private double[] magnitude;
    private DoubleFFT_1D fft;
    private WavArrays audio = WavArrays.getInstance();
    private List<Complex> complexList;

    /**
     * Computes 1D forward DFT of complex data leaving the result in a. Complex
     * number is stored as two double values in sequence: the real and imaginary
     * part, i.e. the size of the input array must be greater or equal 2*n. The
     * physical layout of the input data has to be as follows:
     * 
     * 
     * @param byteArr
     * @return complexArray
     */
    public double[] calcFFT(short[] byteArr) {

        double[] complexArray = new double[byteArr.length * 2];
        for (int i = 0; i < byteArr.length; i++) {
            complexArray[i] = (double) byteArr[i];
        }
        fft = new DoubleFFT_1D(byteArr.length);
        // HanningWindow(complexArray, 0, 2048);
        fft.realForwardFull(complexArray);
        return complexArray;
    }

    /**
     * Calculate magnitude and frequency for a array of complex numbers.
     */
    public void calcMangFreq(double[] input) {
        double sampleRate = audio.getAudioIn().getFormat().getSampleRate();
        double re, im;
        int size = input.length / 2;
        freg = new double[size / 2];
        magnitude = new double[size / 2];
        for (int i = 1; i < size / 2; i++) {
            re = input[2 * i];
            im = input[2 * i + 1];
            magnitude[i] = Math.sqrt(re * re + im * im);
            magnitude[i] = 20 * Math.log(10) * Math.log10(magnitude[i]);
            freg[i] = (double) i * sampleRate / (double) size;

        }
        getMaxFreq();
    }

    public void HanningWindow(double[] signal_in, int pos, int size) {
        for (int i = pos; i < pos + size; i++) {
            int j = i - pos; // j = index into Hann window function
            signal_in[i] = (short) (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0
                    * Math.PI * j / size)));
        }
    }

    /**
     * Computes 1D inverse DFT of complex data leaving the result in a byte
     * array.Each sample is calculated as the magnitude: sample = Math.sqrt(re *
     * re + im * im)
     * 
     * @param input
     *            array composed of real and imaginary numbers
     * @return wavByteArray
     */
    public short[] calcInverseFFT(double[] input) {
        short[] wavByteArray = null;
        if (fft != null) {
            fft.realInverse(input, true);
            int size = input.length / 2;
            wavByteArray = new short[size];
            for (int i = 0; i < size; i++) {
                wavByteArray[i] = (short) Math.round(input[i]);
            }
        }
        return wavByteArray;
    }

    public void lowPassFilter(double[] input, int maxFreq) {
        for (int i = 0; i < maxFreq * 2; i++) {
            input[i] = 0.0;
        }
    }

    public double getMaxFreq() {
        int maxIndex = 0;
        double maxFreq = magnitude[0];
        for (int i = 0; i < magnitude.length; i++) {
            if (maxFreq < magnitude[i]) {
                maxFreq = magnitude[i];
                maxIndex = i;
            }
        }
        System.out
                .println("Fregventa maxima " + freg[maxIndex] + " " + maxFreq);
        return maxFreq;
    }

    /**
     * Write byte samples to a specified text file.
     * 
     * @param byteArray
     * @param fileName
     */
    public void writeTxt(short[] byteArray, String fileName) {
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

    /**
     * Write complex data to a specified text file.Complex number is stored as
     * two double values in sequence: the real and imaginary part.
     * 
     * @param byteArray
     * @param fileName
     */
    public void writeTxt(double[] doubleArray, String fileName) {
        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter(new File(fileName + ".txt"));
            for (int i = 0; i < doubleArray.length; i++) {
                txtWriter.println(doubleArray[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            txtWriter.close();
        }
    }

    public double[] getFreg() {
        return freg;
    }

    public void setFreg(double[] freg) {
        this.freg = freg;
    }

    public double[] getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double[] magnitude) {
        this.magnitude = magnitude;
    }
}
