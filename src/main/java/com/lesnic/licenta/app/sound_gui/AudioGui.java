package com.lesnic.licenta.app.sound_gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.lesnic.licenta.app.audio_effects.AudioManipulation;
import com.lesnic.licenta.app.audio_effects.FFTImpl;
import com.lesnic.licenta.app.audio_effects.WavArrays;
import com.lesnic.licenta.app.model.EqSizes;
import com.lesnic.licenta.app.model.LinearEq;

public class AudioGui extends Application implements EventHandler<ActionEvent> {
    WavArrays audioStream = WavArrays.getInstance();
    FFTImpl fft = new FFTImpl();
    AudioManipulation audio = new AudioManipulation();
    private EqSizes eqSizes = new EqSizes();
    byte[] originalArray;
    int fileIndex = 0;
    private CheckBox ckBoxSpectrum;
    private FileChooser fileChooser;
    private File soundFile;
    private Button loadBtn, playBtn, graphBtn, echoBtn;
    private GraphicsContext gc;
    private TextField zoomTxt;
    private Label echoDelayJl, echoDecayJl, echoFbJl;
    private Canvas canvas;
    private File defaultDirectory;
    private Slider echoDelaySlider, echoDecaySlider, echoFbSlider;
    private double[] fftArray;
    private short[] ifftArray;
    private short[] sampleArray;
    private HBox eqSliders = new HBox(10);
    private static Rectangle eqLine31;
    private static Rectangle eqLine63;
    private static Rectangle eqLine125;
    private static Rectangle eqLine250;
    private static Rectangle eqLine500;
    private static Rectangle eqLine1k;
    private static Rectangle eqLine2k;
    private static Rectangle eqLine4k;
    private static Rectangle eqLine8k;
    private static Rectangle eqLine16k;

    @Override
    public void start(final Stage stage) throws Exception {
        defaultDirectory = new File(
                "C:\\Users\\Dima\\Proiect Licenta\\AudioPlayExample_SourceDataLine\\sounds");

        eqLine31 = new Rectangle(20, 80);
        eqLine63 = new Rectangle(20, 80);
        eqLine125 = new Rectangle(20, 80);
        eqLine250 = new Rectangle(20, 80);
        eqLine500 = new Rectangle(20, 80);
        eqLine1k = new Rectangle(20, 80);
        eqLine2k = new Rectangle(20, 80);
        eqLine4k = new Rectangle(20, 80);
        eqLine8k = new Rectangle(20, 80);
        eqLine16k = new Rectangle(20, 80);

        stage.setTitle("Equalizer");
        stage.setMaximized(true);

        Pane topComponents = new Pane();
        topComponents.setPrefHeight(300);
        HBox eqLines = new HBox(10);
        eqLines.setLayoutX(300);
        eqLines.setLayoutY(100);
        final Rotate rotationTransform = new Rotate(180,
                300 + eqLines.getWidth() / 1, 50 + eqLines.getHeight());
        eqLines.getTransforms().add(rotationTransform);
        VBox topBtns = new VBox();

        VBox root = new VBox();
        VBox rootVSlider = new VBox(10);
        VBox rootSlName = new VBox(5);

        HBox rootLoad = new HBox();
        HBox rootAllSlider = new HBox(10);

        rootVSlider.setMaxWidth(200);
        canvas = new Canvas(8192, 1000);
        gc = canvas.getGraphicsContext2D();

        fileChooser = new FileChooser();

        // CheckBox
        ckBoxSpectrum = new CheckBox("spectrum");
        // Buttons
        loadBtn = new Button("load");
        playBtn = new Button("play");
        graphBtn = new Button("plot");
        echoBtn = new Button("echo");

        // TextFields
        zoomTxt = new TextField("160");

        // Sliders
        echoDelaySlider = new Slider(0, 1, 0.5);
        echoDecaySlider = new Slider(0, 1, 0.5);
        echoFbSlider = new Slider(0, 1, 0.5);

        // Labels
        echoDecayJl = new Label("Decay:");
        echoDelayJl = new Label("Delay:");
        echoFbJl = new Label("Feedback:");

        playBtn.setOnAction(this);
        graphBtn.setOnAction(this);
        echoBtn.setOnAction(this);
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(defaultDirectory);
                fileChooser.setTitle("Open Resource File");
                soundFile = fileChooser.showOpenDialog(stage);
                audio.setByteArray(soundFile);
                originalArray = new byte[audioStream.getSampleArrayWav().length];
                for (int i = 0; i < originalArray.length; i++) {
                    originalArray[i] = audioStream.getSampleArrayWav()[i];
                }
                audio.computeWavSamples(audioStream.getSampleArrayWav());
            }
        });
        ckBoxSpectrum.selectedProperty().addListener(
                new ChangeListener<Boolean>() {
                    public void changed(
                            ObservableValue<? extends Boolean> observable,
                            Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            audio.computeWavSamples(audioStream
                                    .getSampleArrayWav());
                            sampleArray = audioStream.getBitArrayWav();
                            fft.writeTxt(sampleArray, "out/originalSamples");
                            fftArray = fft.calcFFT(sampleArray);
                            fft.calcMangFreq(fftArray);

                            // createMonitoredSlider(31, fftArray, eqSliders);
                            // createMonitoredSlider(63, fftArray, eqSliders);
                            // createMonitoredSlider(87, fftArray, eqSliders);
                            // createMonitoredSlider(125, fftArray, eqSliders);
                            // createMonitoredSlider(175, fftArray, eqSliders);
                            // createMonitoredSlider(250, fftArray, eqSliders);
                            // createMonitoredSlider(350, fftArray, eqSliders);
                            // createMonitoredSlider(500, fftArray, eqSliders);
                            // createMonitoredSlider(700, fftArray, eqSliders);
                            createMonitoredSlider(1000, fftArray, eqSliders);
                            // createMonitoredSlider(1400, fftArray, eqSliders);
                            // createMonitoredSlider(2800, fftArray, eqSliders);
                            // createMonitoredSlider(4000, fftArray, eqSliders);
                            // createMonitoredSlider(5600, fftArray, eqSliders);
                            // createMonitoredSlider(8000, fftArray, eqSliders);

                        }
                    }
                });

        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(stage.getMaxWidth(), 400);
        sp.setContent(canvas);
        rootLoad.getChildren().addAll(graphBtn, zoomTxt);
        rootVSlider.getChildren().addAll(echoDelaySlider, echoDecaySlider,
                echoFbSlider);
        rootSlName.getChildren().addAll(echoDelayJl, echoDecayJl, echoFbJl);
        topBtns.getChildren().addAll(playBtn, loadBtn, rootLoad, echoBtn,
                ckBoxSpectrum);
        root.getChildren().addAll(topComponents);
        eqLines.getChildren().addAll(eqLine31, eqLine63, eqLine125, eqLine250,
                eqLine500, eqLine1k, eqLine2k, eqLine4k, eqLine8k, eqLine16k);
        topComponents.getChildren().addAll(topBtns, eqLines);
        root.getChildren().add(sp);
        rootAllSlider.getChildren().addAll(rootSlName, rootVSlider, eqSliders);
        root.getChildren().add(rootAllSlider);
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * O sa adaug un check box pentru spectrum
     * 
     * @param gc
     */
    private void drawTime(GraphicsContext gc, int zoom) {

        short[] audioBits = audioStream.getBitArrayWav();
        int audioBitSize = audioBits.length;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(1);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        double y_last = 0;
        for (int i = 0; i < audioBitSize; i++) {
            double y_new = -audioBits[i] / 150;
            gc.strokeLine(i / zoom, y_new + 200, (i + 1) / zoom, y_last + 200);
            y_last = y_new;
        }

    }

    private void drawFreq(GraphicsContext gc, int zoom) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(1);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        double y_last = 0;
        for (int i = 0; i < fft.getFreg().length; i++) {
            double y_new = -(fft.getMagnitude()[i]);
            gc.strokeLine(50 + fft.getFreg()[i] / zoom, 1000,
                    50 + fft.getFreg()[i] / zoom, y_new + 1000);
            // gc.strokeLine(fft.getFreg()[i] / zoom, y_new + 1000,
            // fft.getFreg()[i + 1] / zoom, y_last + 1000);
            // y_last = y_new;
        }

    }

    private void drawSpectrum(GraphicsContext gc, int freq, int i) {
        int magnIndex = 0;
        double freqVal = 0;
        double magnVal;
        List<Double> freqList = new ArrayList<Double>();
        for (Double d : fft.getFreg()) {
            freqList.add(d);
        }
        Collections.sort(freqList);
        for (int j = 0; j < freqList.size(); j++) {
            if (freqList.get(j) > freq) {
                freqVal = freqList.get(j);
                break;
            }
        }
        for (int j = 0; j < fft.getFreg().length; j++) {
            if (fft.getFreg()[j] == freqVal) {
                magnIndex = j;
                break;
            }

        }
        magnVal = fft.getMagnitude()[magnIndex];
        System.out.println(magnIndex);
        gc.fillRect(30 * i, 100, 20, magnVal);

    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void handle(ActionEvent event) {
        if (event.getSource() == playBtn) {
            Thread threadPlay = new Thread() {
                @Override
                public void run() { // override the run() for the
                                    // running behaviors
                    try {
                        audio.playWav();
                        sleep(10); // milliseconds
                    } catch (InterruptedException ex) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
            threadPlay.start(); // call back run()

        } else if (event.getSource() == graphBtn) {
            if (ckBoxSpectrum.isSelected()) {
                ifftArray = fft.calcInverseFFT(fftArray);
                drawFreq(gc, Integer.parseInt(zoomTxt.getText()));
                audio.samplesToByte(ifftArray);
            } else {
                audio.samplesToByte(audioStream.getBitArrayWav());
                drawTime(gc, Integer.parseInt(zoomTxt.getText()));

            }
        } else if (event.getSource() == echoBtn) {
            double delay = echoDelaySlider.getValue();
            float decay = (float) echoDecaySlider.getValue();
            audio.echoEffect(originalArray, delay, decay);
        }
    }

    private Slider createMonitoredSlider(final int freq,
            final double[] fftInput, HBox vbox) {

        final Slider slider = new Slider(-100, 100, 0);
        vbox.getChildren().add(slider);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setMajorTickUnit(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinHeight(Slider.USE_PREF_SIZE);
        slider.valueChangingProperty().addListener(
                new ChangeListener<Boolean>() {
                    public void changed(
                            ObservableValue<? extends Boolean> observableValue,
                            Boolean wasChanging, Boolean changing) {
                        if (wasChanging) {
                            fft.writeTxt(fftInput, "out/fft");
                            if (slider.getValue() < 0) {
                                double offset = -slider.getValue();
                                System.out.println("slider Value " + offset);
                                final LinearEq lineEq = new LinearEq(0, 0,
                                        offset, offset);
                                // lineEq.linearInterpolation(freq, fftInput,
                                // freq, true);
                                lineEq.ZeroInterpolation(freq, fftInput, freq);
                                fft.writeTxt(fftInput, "out/fftInterpolated");
                            } else {
                                double offset = slider.getValue();
                                System.out.println("slider Value " + offset);
                                final LinearEq lineEq = new LinearEq(0, 0,
                                        offset, offset);
                                lineEq.linearInterpolation(freq, fftInput,
                                        freq, true);
                                fft.writeTxt(fftInput, "out/fftInterpolated");

                            }
                        }
                    }
                });

        return slider;

    }

    public static Rectangle getEqLine31() {
        return eqLine31;
    }

    public static Rectangle getEqLine63() {
        return eqLine63;
    }

    public static Rectangle getEqLine125() {
        return eqLine125;
    }

    public static Rectangle getEqLine250() {
        return eqLine250;
    }

    public static Rectangle getEqLine500() {
        return eqLine500;
    }

    public static Rectangle getEqLine1k() {
        return eqLine1k;
    }

    public static Rectangle getEqLine2k() {
        return eqLine2k;
    }

    public static Rectangle getEqLine4k() {
        return eqLine4k;
    }

    public static Rectangle getEqLine8k() {
        return eqLine8k;
    }

    public static Rectangle getEqLine16k() {
        return eqLine16k;
    }
}
