package ru.spbstu.dis.ep.kb.nf;

import com.google.common.collect.Lists;
import ru.spbstu.dis.ep.kb.ChosenAction;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.kb.KnowledgeBase;
import ru.spbstu.dis.ep.kb.nf.fuzzy.FuzzyInferenceEngine;
import ru.spbstu.dis.ep.kb.nf.nn.NeuralNetwork;
import ru.spbstu.dis.ep.kb.nf.nn.NeuralNetworkOutput;
import java.util.LinkedList;
import java.util.List;

public class NeuroFuzzyKnowledgeBase implements KnowledgeBase {
  List<NeuralNetwork> neuralNetworks;

  FuzzyInferenceEngine engine;

  public NeuroFuzzyKnowledgeBase(
      final List<NeuralNetwork> neuralNetworks,
      final FuzzyInferenceEngine engine) {
    this.neuralNetworks = neuralNetworks;
    this.engine = engine;
  }

  @Override
  public ChosenAction inferOutput(final DataInput input) {
    // 1. load into neural networks all the data and get results
    final LinkedList<NeuralNetworkOutput> neuralNetworkOutputs =
        generateNeuralNetworkOutputs(input);

    // 2. generate action based on neural network outputs using fuzzy inference
    final ChosenAction chosenAction = generateChosenAction(neuralNetworkOutputs);

    return chosenAction;
  }

  private LinkedList<NeuralNetworkOutput> generateNeuralNetworkOutputs(final DataInput input) {
    final LinkedList<NeuralNetworkOutput> neuralNetworkResults = Lists.newLinkedList();
    for (NeuralNetwork neuralNetwork : neuralNetworks) {
      final NeuralNetworkOutput output = neuralNetwork.inferOutput(input);
      neuralNetworkResults.add(output);
    }
    return neuralNetworkResults;
  }

  private ChosenAction generateChosenAction(
      final LinkedList<NeuralNetworkOutput> neuralNetworkOutputs) {
    return engine.generateAction(neuralNetworkOutputs);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final NeuroFuzzyKnowledgeBase that = (NeuroFuzzyKnowledgeBase) o;

    if (neuralNetworks != null ? !neuralNetworks.equals(that.neuralNetworks) :
        that.neuralNetworks != null) {
      return false;
    }
    return !(engine != null ? !engine.equals(that.engine) : that.engine != null);
  }

  @Override
  public int hashCode() {
    int result = neuralNetworks != null ? neuralNetworks.hashCode() : 0;
    result = 31 * result + (engine != null ? engine.hashCode() : 0);
    return result;
  }
}
