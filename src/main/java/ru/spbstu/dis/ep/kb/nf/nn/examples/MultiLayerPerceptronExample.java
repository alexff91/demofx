package ru.spbstu.dis.ep.kb.nf.nn.examples;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

import java.util.Arrays;

public class MultiLayerPerceptronExample {
  public static void main(String[] args) {
    final int inputsCount = 2;
    final int outputsCount = 2;
    // uses MomentumBackpropagation
    // random initial weights
    // SIGMOID activation function
    MultiLayerPerceptron neuralNet =
        new MultiLayerPerceptron(
            inputsCount,
            16,
            outputsCount);

    // configure learning rule
    MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
    learningRule.addListener(event -> {
      BackPropagation bp = (org.neuroph.nnet.learning.BackPropagation) event.getSource();
      System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp
          .getTotalNetworkError());
      //      System.out.println(learningEvent.getEventType());
    });
    // set learning rate and max error
    learningRule.setLearningRate(0.005);
    learningRule.setMaxError(0.001);

    // learn the network

    final DataSet dataSet = new DataSet(inputsCount, outputsCount);

    for (int i = 0; i < 1000; i++) {
      double[] inputs = new double[2];
      double[] outputs = new double[2];

      double radius = 10;
      final double t = Math.random() * 2 * Math.PI;
      double x = radius * Math.cos(t);
      double y = radius * Math.sin(t);
      inputs[0] = x;
      inputs[1] = y;
      outputs[0] = 1.0;
      outputs[1] = 0.0;

      final DataSetRow row = new DataSetRow(inputs, outputs);
      //      System.out.println(row);
      dataSet.addRow(row);
    }

    for (int i = 0; i < 1000; i++) {
      double[] inputs = new double[2];
      double[] outputs = new double[2];

      double radius = 20;
      final double t = Math.random() * 2 * Math.PI;
      double x = radius * Math.cos(t);
      double y = radius * Math.sin(t);

      inputs[0] = x;
      inputs[1] = y;
      outputs[0] = 0.0;
      outputs[1] = 1.0;

      dataSet.addRow(inputs, outputs);
    }

    dataSet.shuffle();

    // norbalize the data
    //    Normalizer norm = new MaxMinNormalizer();
    //    norm.normalize(dataSet);
    neuralNet.learn(dataSet);

    //    dataSet.getRows().forEach(dataSetRow -> {
    ////      double[] inputs = new double[]{0,0.5};
    //      neuralNet.setInput(dataSetRow.getInput());
    //      neuralNet.calculate();
    //      double[] networkOutput = neuralNet.getOutput();
    //      System.out.println("Input: " + Arrays.toString(dataSetRow.getInput()));
    //      System.out.println("Output: " + Arrays.toString(networkOutput) );
    //      System.out.println("Desired output: " + Arrays.toString(dataSetRow.getDesiredOutput()) );
    //
    //    });
    //
    //
    double[] inputs = new double[]{10, 0};
    neuralNet.setInput(inputs);
    neuralNet.calculate();
    double[] networkOutput = neuralNet.getOutput();
    System.out.println("Input: " + Arrays.toString(inputs));
    System.out.println("Output: " + Arrays.toString(networkOutput));

//    Works with last version from github
    // String[] classNames = {"Radius10", "Radius20"};
//    Evaluation evaluation = new Evaluation();
//    evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
//    evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(classNames));
//    ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
//    evaluation.evaluateDataSet(neuralNet, dataSet);
//
//    ConfusionMatrix confusionMatrix = evaluator.getResult();
//    System.out.println("Confusion matrrix:\r\n");
//    System.out.println(confusionMatrix.toString() + "\r\n\r\n");
//    System.out.println("Classification metrics\r\n");
//    ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
//    ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
//    for (ClassificationMetrics cm : metrics) {
//      System.out.println(cm.toString() + "\r\n");
//    }
//    System.out.println(average.toString());

//    CrossValidation crossval = new CrossValidation(neuralNet, dataSet, 18);
//    ((SubSampling) crossval.getSampling()).setAllowRepetition(true);
//    crossval.addEvaluator(new ClassifierEvaluator.MultiClass(classNames));
//
//    crossval.run();
//    CrossValidationResult results = crossval.getResult();
//    System.out.println(results);
  }
}