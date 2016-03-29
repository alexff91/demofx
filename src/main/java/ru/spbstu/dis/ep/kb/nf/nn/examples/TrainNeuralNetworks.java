package ru.spbstu.dis.ep.kb.nf.nn.examples;

import org.neuroph.core.data.*;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.Perceptron;
import java.io.*;

/**
 * Created by el on 08.12.2015.
 */
public class TrainNeuralNetworks {
  private int nInputs, nOutputs;
  private static DataSet trainingSet;



  private String filePath; //"C:/Users/el/Desktop"
  public String getFilePath() {
    return filePath;
  }

  public TrainNeuralNetworks(final String filePath) {
    this.nInputs = 0;
    this.nOutputs = 0;

    this.filePath = filePath;

  }
  public static void trainPerceptron (final int nInputs,final int nOutputs) throws IOException {
    org.neuroph.core.NeuralNetwork myPerceptron1 = new Perceptron(nInputs, nOutputs);
    setRules(myPerceptron1);
   // createTrainingSet1(getFilePath());
    myPerceptron1.learn(trainingSet);


  }
  private static void setRules(final org.neuroph.core.NeuralNetwork myNeuralNetwork) {
    SupervisedLearning learningRule2 = (SupervisedLearning) myNeuralNetwork.getLearningRule();
    learningRule2.setMaxError(0.00001);
    learningRule2.setMaxIterations( 10000);
  }
  public static void createTrainingSet1(String filePath) throws IOException {
    trainingSet = new DataSet(3, 1);
    File f = new File(filePath + "/CSV1.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));
    String line;
    while ((line = fin.readLine()) != null) {
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      trainingSet.addRow(new DataSetRow(new double[]{values[2], values[3], values[4]},
                                        new double[]{values[1]}));
    }
  }
  public static void createTrainingSet2(String filePath) throws IOException {
    trainingSet = new DataSet(2, 1);
    File f = new File(filePath+"/CSV2.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));
    String line;
    while ((line = fin.readLine()) != null){
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      trainingSet.addRow(new DataSetRow(new double[]{values[3],  values[4]  },
                                        new double[]{values[2]}));
    }
    }


  public static void createTrainingSet3(String filePath) throws IOException {
    trainingSet = new DataSet(3, 1);
    File f = new File(filePath+"/CSV3.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));
    String line;
    while ((line = fin.readLine()) != null){
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      trainingSet.addRow(new DataSetRow(new double[]{values[1],  values[2], values[3]  },
                                        new double[]{values[0]}));
    }
  }
}
