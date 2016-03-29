package ru.spbstu.dis.ep.kb.nf.nn;

import ru.spbstu.dis.ep.data.DataInput;

public interface NeuralNetwork {
  NeuralNetworkOutput inferOutput(DataInput input);
}
