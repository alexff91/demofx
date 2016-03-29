package ru.spbstu.dis.ep.kb.nf.nn.examples;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.*;
import java.io.*;
import java.util.Arrays;

public class HebbianEmergencyChemStation{


  public static void main(String args[]) throws IOException {

    // create training set

    int nInputs=0, nOutputs=0;
    DataSet trainingSet= new DataSet(nInputs, nOutputs);

    int numberOfNNToCreate=3;

    if(numberOfNNToCreate==1) {
      nInputs = 3;
      nOutputs = 1;
      trainingSet = new DataSet(nInputs, nOutputs);
      createTrainingSet_deltaT1(trainingSet);
    }
    if(numberOfNNToCreate==2) {
      nInputs = 2;
      nOutputs = 1;
      trainingSet = new DataSet(nInputs, nOutputs);
      createTrainingSet_deltaT(trainingSet);
    }
    if(numberOfNNToCreate==3) {
      nInputs = 3;
      nOutputs = 1;
      trainingSet = new DataSet(nInputs, nOutputs);
      createTrainingSet_lowT(trainingSet);
    }

    SupervisedHebbianNetwork myHebb = new SupervisedHebbianNetwork(nInputs, nOutputs);
    setRules(myHebb);
    // myHebb.learn(trainingSet);

    System.out.println("Testing MLP");
    NeuralNetwork myPerceptron = new Perceptron(nInputs, nOutputs);
    setRules(myPerceptron);
    // learn the training set
    myPerceptron.learn(trainingSet);
    // test perceptron
    if(numberOfNNToCreate==1) {
      trainingSet.addRow(new DataSetRow(new double[]{0.10, 0.004,1}, new double[]{1}));
      trainingSet.addRow(new DataSetRow(new double[]{0.10, 0.099,0}, new double[]{0}));
      trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.18,1}, new double[]{1}));
      trainingSet.addRow(new DataSetRow(new double[]{0.8, 0.792,1}, new double[]{1}));//2371
    }
    if(numberOfNNToCreate==2) {
      trainingSet.addRow(new DataSetRow(new double[]{0.10, 0.004}, new double[]{0}));
      trainingSet.addRow(new DataSetRow(new double[]{0.10, 0.04}, new double[]{0}));
      trainingSet.addRow(new DataSetRow(new double[]{0.10, 0.114}, new double[]{1}));
    }
    if(numberOfNNToCreate==3) {

      trainingSet.addRow(new DataSetRow(new double[]{0, 1,1}, new double[]{0}));
      trainingSet.addRow(new DataSetRow(new double[]{1, 1,0}, new double[]{0}));
      trainingSet.addRow(new DataSetRow(new double[]{1, 1,1}, new double[]{1}));
    }
    trainNeuralNetwork(trainingSet, myPerceptron);
    // trainNeuralNetwork(trainingSet, myHebb);
  }

  private static void setRules(final NeuralNetwork myNeuralNetwork) {
    SupervisedLearning learningRule2 = (SupervisedLearning) myNeuralNetwork.getLearningRule();
    learningRule2.setMaxError(0.00001);
    learningRule2.setMaxIterations( 10000);
  }

  private static void trainNeuralNetwork(final DataSet trainingSet,
                                         final NeuralNetwork myNeuralNetwork) {

    int i=0, k1=0;
    double out1=0, out2=0;
    for (DataSetRow trainingElement : trainingSet.getRows()) {
      myNeuralNetwork.setInput(trainingElement.getInput());
      myNeuralNetwork.calculate();
      double[] networkOutput = myNeuralNetwork.getOutput();
      System.out.print("i = " +i);
      System.out.print("  Input: " + Arrays.toString(trainingElement.getInput()));
      System.out.print(" Desired: " + Arrays.toString(trainingElement.getDesiredOutput()));
      System.out.println(" Output: " + Arrays.toString(networkOutput));

      i++;
      out1=trainingElement.getDesiredOutput()[0];
      out2=networkOutput[0];
      if (Math.abs(out1-Math.abs(out2))>0.49) {
        k1++;
        System.out.println(" ERROR!!!!!!!!!!!!!! ");
      }


    }
    System.out.println("Errors: "+k1 );

  }

  private static void createTrainingSet_deltaT(final DataSet trainingSet) throws
  IOException {
    // variate pressure
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    File f = new File("C:/Users/el/Desktop/CSV2.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));

    String line;
    while ((line = fin.readLine()) != null){
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      //System.out.println(Arrays.toString(values));
      trainingSet.addRow(new DataSetRow(new double[]{values[3],  values[4]  },
                                        new double[]{values[2]}));
    }


  }

  private static void createTrainingSet_deltaT1(final DataSet trainingSet) throws
  IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    File f = new File("C:/Users/el/Desktop/CSV1.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));

    String line;
    while ((line = fin.readLine()) != null){
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      //System.out.println(Arrays.toString(values));
      trainingSet.addRow(new DataSetRow(new double[]{values[2],  values[3], values[4]  },
                                        new double[]{values[1]}));
    }


  }

  private static void createTrainingSet_lowT(final DataSet trainingSet) throws
  IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    File f = new File("C:/Users/el/Desktop/CSV3.csv");
    BufferedReader fin = new BufferedReader(new FileReader(f));

    String line;
    while ((line = fin.readLine()) != null){
      final String[] splittedLine = line.split(";");
      double[] values = new double[splittedLine.length];
      for (int i = 0; i < splittedLine.length; i++) {
        values[i] = Double.parseDouble(splittedLine[i]);
      }
      //System.out.println(Arrays.toString(values));
      trainingSet.addRow(new DataSetRow(new double[]{values[1],  values[2], values[3]  },
                                        new double[]{values[0]}));
    }


  }
}
