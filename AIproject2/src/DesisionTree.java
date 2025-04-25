import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.gui.treevisualizer.PlaceNode1;

import java.io.File;

import javax.swing.JFrame;

public class DesisionTree {
    
    public static void main(String[] args) throws Exception {

        // Load datasets from .ARFF file 
        DataSource source = new DataSource("file:///C:/Users/user/Downloads/car.arff");
        Instances data = source.getDataSet();
        
       // setting class attribute if the data format does not provide this information
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }
        
        // call method print
        printClassDistribution(data);

        /** create model 1 */
        // Split dataset into 70% training and 30% testing using spilt method 
        Instances[] split1 = splitDataset(data, 0.7);
        Instances trainM1 = split1[0];
        Instances testM1 = split1[1];
       
        // call method train model for model 1
        J48 M1 = trainModel(trainM1);

        // Evaluate model M1
        System.out.println("\nEvaluating Model M1 (70-30 Split):");
        evaluateModel(M1, testM1);
        
        /** create model 2 */
        // Split dataset into 50% training and 50% testing using spilt method 
        Instances[] split2 = splitDataset(data, 0.5);
        Instances trainM2 = split2[0];
        Instances testM2 = split2[1];
       
        // call method train model for model 2
        J48 M2 = trainModel(trainM2);

        // Evaluate model M1
        System.out.println("\nEvaluating Model M2 (50-50 Split):");
        evaluateModel(M2, testM2);
        
        // call method plot tree for model 1 and model 2
        plotTree(M1, "Decision Tree M1");
        plotTree(M2, "Decision Tree M2");
        
        }
	
	// Print the distribution of the target class
    public static void printClassDistribution(Instances data) {
        int[] counts = new int[data.numClasses()];
        for (int i = 0; i < data.numInstances(); i++) {
            int classIndex = (int) data.instance(i).classValue();
            counts[classIndex]++; //The code goes through all the rows (instances) in the data, and counts the number of times each class appears in these rows.
        }

        System.out.println("Class Distribution:");
        for (int i = 0; i < data.numClasses(); i++) {
            double percentage = ((double) counts[i] / data.numInstances()) * 100;
            System.out.println(""+ data.classAttribute().value(i)+ " : " +percentage);//The code calculates and displays the percentage of each category out of the total categories in the data
        }
    }
    
    // Split dataset into training and testing
    public static Instances[] splitDataset(Instances data, double trainingPercentage) throws Exception {
    	// Shuffle the data before using it
        Randomize randomize = new Randomize();
        randomize.setInputFormat(data);
        Instances randomizedData = Filter.useFilter(data, randomize);

        int trainSize = (int) Math.round(data.numInstances() * trainingPercentage); // in this project we use 70% and 50% of data
        int testSize = data.numInstances() - trainSize; // in this project we use 30% and 50% of data
        Instances train = new Instances(randomizedData, 0, trainSize); // get random data from first to train size
        Instances test = new Instances(randomizedData, trainSize, testSize); // get random data from train size to test size

        return new Instances[]{train, test};
    }
    
    // Train a decision tree model using C4.5 algorithm
    public static J48 trainModel(Instances trainData) throws Exception {
        J48 tree = new J48();
        tree.setOptions(new String[]{"-U"}); // Use unpruned tree
        tree.buildClassifier(trainData);
        return tree;
    }

    // Evaluate the model and print the accuracy and the F1-Score of the test set
    public static void evaluateModel(J48 model, Instances testData) throws Exception {
        Evaluation eval = new Evaluation(testData);
        eval.evaluateModel(model, testData);

        System.out.println("Accuracy: " +eval.pctCorrect());
        System.out.println("F1-Score (Weighted): " +eval.weightedFMeasure());
    }
    
 // Generate (plot) the generated decision tree of models
    public static void plotTree(J48 model, String title) throws Exception {
        // Create a JFrame to display the decision tree
        JFrame frame = new JFrame(title);
        frame.setSize(2000, 1000); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a TreeVisualizer with default PlaceNode2
        TreeVisualizer visualizer = new TreeVisualizer(null, model.graph(), new PlaceNode1());

        // Add the visualizer to the frame
        frame.getContentPane().add(visualizer);
        frame.setVisible(true);

        // Adjust scaling and fit the tree to the screen
        visualizer.fitToScreen(); // Automatically fit to the screen size

        visualizer.setScale(2.0, 2.0); 

        frame.revalidate();
        frame.repaint();
    }
}

