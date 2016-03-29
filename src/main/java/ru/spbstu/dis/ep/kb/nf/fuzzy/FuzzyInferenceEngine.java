package ru.spbstu.dis.ep.kb.nf.fuzzy;

import ru.spbstu.dis.ep.kb.ChosenAction;
import ru.spbstu.dis.ep.kb.nf.nn.NeuralNetworkOutput;
import java.util.List;

public interface FuzzyInferenceEngine {
  ChosenAction generateAction(List<NeuralNetworkOutput> listOfNeuralNetworkOutputs);
}
