package ru.spbstu.dis.ep.kb;

import ru.spbstu.dis.ep.data.DataInput;

public interface KnowledgeBase {
  ChosenAction inferOutput(DataInput input);
}
