package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private TextArea textArea = new TextArea();
    private Label label1 , label2, label3;
    private Button openFileButton, startButton;
    private LineChart<Number, Number> lineChart;

    @Override
    public void start(Stage primaryStage) {
    	label1 = new Label();
    	label2 = new Label();
    	label3 = new Label();
        styleLabel(label1);
        styleLabel(label2);
        styleLabel(label3);
        
        startButton = new Button("Start");
        styleButton(startButton);

        textArea.setWrapText(true);
        textArea.setPrefHeight(300);
        textArea.setEditable(false);


        openFileButton = new Button("Open File");
        openFileButton.setVisible(false);
        styleButton(openFileButton);
        
        
        // Chart setup
        lineChart = new LineChart<>(new NumberAxis("Generation", 0, 100, 10), new NumberAxis("fitness", 0, 100, 10));
        lineChart.setTitle("Fitness  over Generations");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);
        

        //Button Actions
        openFileButton.setOnAction(event -> openFile(primaryStage));
        startButton.setOnAction(event -> startAlgorithm(series));
        
        
        VBox layout = new VBox(10, label1, label2, startButton, textArea, label3, openFileButton, lineChart);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Genetic Algorithm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startAlgorithm(XYChart.Series<Number, Number> series) {
    	try {
            openFileButton.setVisible(true);
            series.getData().clear();
            GeneticAlgorithm.main(label1, label2, label3, textArea, series);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    private void openFile(Stage primaryStage) {
    	File file = new File("convergenceRates.txt"); // Specify the file to open

        if (file.exists()) {
            try {
                // Open the file in the default text editor
                java.awt.Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        else {
        	textArea.appendText("The file convergenceRates.txt does not exist.\n");
        }
    }

    private void styleLabel(Label label) {
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
    }
    
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10; -fx-border-radius: 10;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}