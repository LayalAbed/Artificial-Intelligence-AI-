package application;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class GeneticAlgorithm {
    private static final Random random = new Random();
    private static String passcode;
    private String bestParent;
    private int bestFitness;
    private static final int PASSCODE_LENGTH = 32;
    private static int generationCount = 0;
    
    // Constructor
    public GeneticAlgorithm() {
        this.bestParent = generateParent(PASSCODE_LENGTH);
        this.bestFitness = getFitness(bestParent);
        System.out.println("Started");
    }

	// Generate a random 32-bit passcode
    public static String generateRandomPasscode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PASSCODE_LENGTH; i++) {
            sb.append(random.nextBoolean() ? '1' : '0');
        }
        return sb.toString();
    }

    

    // Convert binary number to ASCII code
    public static String fromBinary(String binaryString) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 8) {
            String byteString = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
            char c = (char) Integer.parseInt(byteString, 2);
            text.append(c);
        }
        return text.toString();
    }

    // Generate a random binary number as a parent
    private String generateParent(int length) {
        StringBuilder parent = new StringBuilder();
        while (parent.length() < length) {
            parent.append(random.nextInt(2)); // random number using 0 and 1 
        }
        return parent.toString();
    }

    // Calculate fitness based on matching bits
    private int getFitness(String guess) {
        int fitness = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (passcode.charAt(i) == guess.charAt(i)) {
                fitness++;
            }
        }
        return fitness;
    }

    
    // Apply mutation on the binary string
    private String mutate(String parent) {
        char[] childGenes = parent.toCharArray();
        for (int i = 0; i < childGenes.length; i++) {
            if (random.nextDouble() < 0.05) {
                childGenes[i] = (childGenes[i] == '0') ? '1' : '0';
            }
        }
        return new String(childGenes);
    }

    // Apply crossover between two parents
    private String crossover(String parent1, String parent2) {
        int crossoverPoint = random.nextInt(parent1.length());
        return parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
    }

 // Display the results
    private void display(String guess, int fitness, TextArea txt, XYChart.Series<Number, Number> series) throws IOException {
        generationCount++;
        txt.appendText("Generation #" + generationCount + ": " + fromBinary(guess) + " : " + guess + " | Fitness: " + fitness + "\n");
        
        // Generation on X-axis and Fitness on Y-axis
        series.getData().add(new XYChart.Data<>(generationCount, fitness));
        
        if (generationCount == 1) { // Clear the file only on the first generation
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("convergenceRates.txt", false))) {
                writer.write(""); // Clear the file
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("convergenceRates.txt", true))) {
            writer.write("Generation " + generationCount + " Fitness: " + fitness + "\n");
        }
    }



    // Run the genetic algorithm
    public void runWithDisplayUpdates(TextArea txt, XYChart.Series<Number, Number> series) throws IOException {
    	display(bestParent, bestFitness, txt, series);
        while (true) {
            String parent1 = bestParent;
            String parent2 = generateParent(PASSCODE_LENGTH);
            String crossoverChild = crossover(parent1, parent2);
            String child = mutate(crossoverChild);
            int childFitness = getFitness(child);

            display(child, childFitness, txt, series);

            if (bestFitness >= childFitness) {
                continue;
            }

            bestFitness = childFitness;
            bestParent = child;

            if (childFitness == PASSCODE_LENGTH) {
            	txt.appendText("\nPassword found: " +fromBinary(bestParent)+ "\n");
            	txt.appendText("Total generations: " + generationCount +"\n");
                break;
            }
        }
    }

    public static void main(Label l1 ,Label l2, Label l3, TextArea txt, XYChart.Series<Number, Number> series) throws IOException {
        passcode = generateRandomPasscode();
        String pass = fromBinary(passcode);
        l1.setText("Binary Passcode: " + passcode);
        l2.setText("ASCII Passcode: " + pass);

        long startTime = System.currentTimeMillis();
        
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.runWithDisplayUpdates(txt, series);
        
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime)  / 1000;

        l3.setText("Time taken to find passcode: " + timeTaken + " Seconds");
        
    }
}