package com.lesnic.licenta.app.utils;

/**
 * 
 * @version $Id$
 */
public class Complex {

    private double re;
    private double im;

    /**
     * 
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    public double getRe() {
        return re;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }

}
