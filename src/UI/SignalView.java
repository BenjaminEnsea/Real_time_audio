package UI;

import Audio.AudioSignal;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SignalView extends LineChart<Number,Number> {
    private int FrameSize;

    Scene scene ;
    NumberAxis xAxis = new NumberAxis(0, 100, 1);
    NumberAxis yAxis = new NumberAxis(-20, 20, 0.001);

    public SignalView(Axis<Number> numberAxis, Axis<Number> numberAxis2) throws LineUnavailableException {
        super(numberAxis, numberAxis2);
        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Audio Input");

        AudioSignal A1 = new AudioSignal(100);
        AudioFormat format = new AudioFormat(8000, 16, 1, true, true);
        TargetDataLine line = AudioSystem.getTargetDataLine(format);
        line.open(format);
        line.start();
        A1.recordFrom(line);

        int i;
        for (i=0; i < 100-1; i++){
            series.getData().add(new XYChart.Data(i, 50*A1.getSample(i)));
        }


        //Setting the data to Line chart
        linechart.getData().add(series);

        //Creating a Group object
        Group root = new Group(linechart);

        //Creating a scene object
        this.scene = new Scene(root, 1100, 800);


        //Adding scene to the stage


        //Displaying the contents of the stage


    }

    void updateData(double[] data){

    }


}
