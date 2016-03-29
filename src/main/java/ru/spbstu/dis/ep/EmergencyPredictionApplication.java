package ru.spbstu.dis.ep;

import ru.spbstu.dis.ep.data.DataProvider;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.random.RandomDataProvider;
import ru.spbstu.dis.ep.kb.KnowledgeBase;
import ru.spbstu.dis.ep.kb.dcep.DCepKnowledgeBase;
import ru.spbstu.dis.ep.kb.dcep.StaticBlockingSiddhi;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmergencyPredictionApplication {
  public static void main(String[] args) {
//    final DataProvider dataProvider = new SoftingOpcDataProvider();
//    final KnowledgeBase kb = new NeuroFuzzyKnowledgeBase(getNeuralNetworks(), getFuzzyEngine());
    DataProvider dataProvider = new RandomDataProvider();
    KnowledgeBase kb = new DCepKnowledgeBase();

    final EmergencyPredictor emergencyPredictor =
        new EmergencyPredictor(
            dataProvider,
            kb,
            chosenAction -> {
              System.out.println("EMERGENCY PREDICTOR GENERATED ACTION " + chosenAction);
            });

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
        () -> {
          try {
            emergencyPredictor.execute();
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        0L, 100L, TimeUnit.MILLISECONDS);
  }
}
