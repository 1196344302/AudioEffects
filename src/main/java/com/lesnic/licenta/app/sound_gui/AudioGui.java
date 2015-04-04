package com.lesnic.licenta.app.sound_gui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.lesnic.licenta.app.audio_effects.AudioManipulation;
import com.lesnic.licenta.app.audio_effects.JTransformImpl;
import com.lesnic.licenta.app.audio_effects.WavArrays;

public class AudioGui extends Application implements EventHandler<ActionEvent> {
    WavArrays audioStream = WavArrays.getInstance();
    JTransformImpl fft = new JTransformImpl();
    AudioManipulation audio = new AudioManipulation();
    byte[] originalArray;
    int fileIndex = 0;
    private CheckBox ckBoxSpectrum;
    private FileChooser fileChooser;
    private File soundFile;
    private Button loadBtn, playBtn, graphBtn, echoBtn;
    private GraphicsContext gc;
    private TextField zoomTxt;
    private Label echoDelayJl, echoDecayJl, echoFbJl;
    private Label echoDelayJlVal, echoDecayJlVal, echoFbJlVal;
    private Canvas canvas;
    private File defaultDirectory;
    private Slider echoDelaySlider, echoDecaySlider, echoFbSlider;
    private double[] fftArray;
    private short[] ifftArray;
    private short[] sampleArray;

    @Override
    public void start(final Stage stage) throws Exception {

        stage.setTitle("Equalizer");
        stage.setMaximized(true);
        defaultDirectory = new File(
                "C:\\Users\\Dima\\Proiect Licenta\\AudioPlayExample_SourceDataLine\\sounds");

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
            }
        });
        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(stage.getMaxWidth(), 400);
        sp.setContent(canvas);
        rootLoad.getChildren().addAll(graphBtn, zoomTxt);
        rootVSlider.getChildren().addAll(echoDelaySlider, echoDecaySlider,
                echoFbSlider);
        rootSlName.getChildren().addAll(echoDelayJl, echoDecayJl, echoFbJl);
        root.getChildren().addAll(playBtn, loadBtn, rootLoad, echoBtn,
                ckBoxSpectrum);
        root.getChildren().add(sp);
        rootAllSlider.getChildren().addAll(rootSlName, rootVSlider);
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
        audio.setWavGraph(audioStream.getSampleArrayWav());
        short[] audioBits = audioStream.getBitArrayWav();
        int audioBitSize = audioBits.length;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(1);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        double y_last = 0;
        for (int i = 0; i < audioBitSize; i++) {
            double y_new = -audioBits[i] / 100;
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
        for (int i = 0; i < fft.getFreg().length - 1; i++) {
            double y_new = -(fft.getMagnitude()[i]);
            gc.strokeLine(fft.getFreg()[i] / zoom, 500,
                    fft.getFreg()[i] / zoom, y_new + 500);
            // gc.strokeLine(fft.getFreg()[i] / zoom, y_new + 1000,
            // fft.getFreg()[i + 1] / zoom, y_last + 1000);
            // y_last = y_new;
        }

    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void handle(ActionEvent event) {
        if (event.getSource() == playBtn) {
            try {
                audio.playWav();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else if (event.getSource() == graphBtn) {
            if (ckBoxSpectrum.isSelected()) {
                audio.setWavGraph(audioStream.getSampleArrayWav());
                sampleArray = audioStream.getBitArrayWav();
                fft.writeTxt(sampleArray, "out/originalSamples");
                fftArray = fft.calcFFT(sampleArray);
                fft.writeTxt(fftArray, "out/fft");
                fft.calcMangFreq(fftArray);
                // fft.lowPassFilter(fftArray, 50);

                ifftArray = fft.calcInverseFFT(fftArray);
                // audio.samplesToByte(ifftArray);

                // fft.writeTxt(ifftArray, "out/ifftByteArray");

                fft.writeTxt(fft.getMagnitude(), "magnitudee");
                fft.getMaxFreq();
                drawFreq(gc, Integer.parseInt(zoomTxt.getText()));
            } else {
                drawTime(gc, Integer.parseInt(zoomTxt.getText()));
            }
        } else if (event.getSource() == echoBtn) {
            double delay = echoDelaySlider.getValue();
            float decay = (float) echoDecaySlider.getValue();
            audio.echoEffect(originalArray, delay, decay);
        }
    }
}
