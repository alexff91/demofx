package ru.spbstu.dis.ep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.data.DataProvider;
import ru.spbstu.dis.ep.kb.ChosenAction;
import ru.spbstu.dis.ep.kb.ChosenActionListener;
import ru.spbstu.dis.ep.kb.KnowledgeBase;

public class EmergencyPredictor {
  private static Logger LOGGER = LoggerFactory.getLogger(EmergencyPredictor.class);

  private DataProvider dataProvider;

  private KnowledgeBase knowledgeBase;

  private ChosenActionListener chosenActionListener;

  public EmergencyPredictor(final DataProvider dataProvider, final KnowledgeBase knowledgeBase,
      final ChosenActionListener chosenActionListener) {
    this.dataProvider = dataProvider;
    this.knowledgeBase = knowledgeBase;
    this.chosenActionListener = chosenActionListener;
  }

  public DataProvider getDataProvider() {
    return dataProvider;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public ChosenActionListener getChosenActionListener() {
    return chosenActionListener;
  }

  public void execute() {
    final DataInput integerDoubleHashMap = dataProvider.nextDataPortion();
    LOGGER.warn("Data portion is " + integerDoubleHashMap);
    final ChosenAction chosenAction = knowledgeBase.inferOutput(integerDoubleHashMap);
    LOGGER.warn("Chosen action is " + chosenAction);
    chosenActionListener.accept(chosenAction);
  }
}