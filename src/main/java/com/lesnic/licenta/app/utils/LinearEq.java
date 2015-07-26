package com.lesnic.licenta.app.utils;

/**
 * 
 * @version $Id$
 */
public class LinearEq {
    /**
     * 
     */
    private double Ax;
    private double Ay;
    private double Bx;
    private double By;
    private double a, b;

    public LinearEq(double Ax, double Ay, double Bx, double By) {
        this.Ax = Ax;
        this.Ay = Ay;
        this.Bx = Bx;
        this.By = By;

    }

    public void calcEq() {
        a = (By - Ay) / (Bx - Ax);
        b = Ay - a * Ax;
        System.out.println("y = " + a + "x+" + b);
    }

    public double calcY(int x) {
        double y = 0;
        if (this.a != 0 || this.b != 0) {
            y = this.a * x + this.b;
        }
        return y;
    }

    public void linearInterpolation(int freq, double[] input, int freqOffset,
            boolean isDown) {
        int minFreq, maxFreq;
        double dist;
        calcEq();
        minFreq = freq - freqOffset;
        maxFreq = freq + freqOffset;
        if (isDown) {
            for (int i = 1, j = minFreq + 2; i <= freqOffset; i++, j += 2) {
                dist = calcY(i);
                input[j] -= input[j] * dist / freqOffset * Bx / 100;
                input[j + 1] -= input[j + 1] * dist / freqOffset * Bx / 100;
            }
            for (int i = 1, j = maxFreq * 2 - 1; i <= freqOffset; i++, j -= 2) {
                dist = calcY(i);
                input[j] -= input[j] * dist / freqOffset * Bx / 100;
                input[j - 1] -= input[j - 1] * dist / freqOffset * Bx / 100;
            }

        } else {
            for (int i = 1, j = minFreq + 2; i <= freqOffset; i++, j += 2) {
                dist = calcY(i);
                input[j] += input[j] * dist / freqOffset * Bx / 100;
                input[j + 1] += input[j + 1] * dist / freqOffset * Bx / 100;
            }
            for (int i = 0, j = maxFreq * 2 - 1; i < freqOffset; i++, j -= 2) {
                dist = calcY(i);
                input[j] += input[j] * dist / freqOffset * Bx / 100;
                input[j - 1] += input[j - 1] * dist / freqOffset * Bx / 100;
            }

        }
    }

    public void ZeroInterpolation(int freq, double[] input, int freqOffset) {
        int maxFreq;
        maxFreq = freq + freqOffset;
        for (int i = 1; i < maxFreq * 2; i++) {
            input[i] = 0;
        }
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}
