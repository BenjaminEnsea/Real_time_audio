package UI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;


public class Main extends Application {

    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setTop(createToolbar());
            root.setBottom(createStatusbar());
            root.setCenter(createMainContent());
            Scene scene = new Scene(root,1500,800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            primaryStage.show();
            NumberAxis xAxis = new NumberAxis(0, 10, 1);
            NumberAxis yAxis = new NumberAxis(0, 10, 1);
            xAxis.setLabel("Nombre de Sample");
            yAxis.setLabel("dB level");
            SignalView sig = new SignalView(xAxis,yAxis);
            primaryStage.setScene(sig.scene);
            stage.setScene(scene);
            stage.show();
            } catch(Exception e) {e.printStackTrace();}
       }

        private Node createToolbar(){
        Button button = new Button("appuyez !");
        ToolBar tb = new ToolBar(button, new Label("ceci est un label"), new Separator());
        button.setOnAction(event -> System.out.println("appui!"));
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cb);
        return tb;
        }

        private Node createStatusbar(){
        HBox statusbar = new HBox();
        statusbar.getChildren().addAll(new Label("Name:"), new TextField(""));
        return statusbar;
        }
        private Node createMainContent(){
        Group g = new Group();
        // ici en utilisant g.getChildren().add(...) vous pouvez ajouter tout  ́el ́ement graphique souhait ́e de type Node
        return g;
        }

        Stage stage = new Stage();


}

