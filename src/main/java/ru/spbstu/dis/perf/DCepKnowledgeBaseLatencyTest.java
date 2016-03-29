package ru.spbstu.dis.perf;

import com.google.common.base.Stopwatch;
import ru.spbstu.dis.ep.data.random.RandomDataProvider;
import ru.spbstu.dis.ep.kb.dcep.DCepKnowledgeBase;

import java.util.concurrent.TimeUnit;

public class DCepKnowledgeBaseLatencyTest {
  public static void main(String[] args) {
    DCepKnowledgeBase dCepKnowledgeBase = new DCepKnowledgeBase();
    Stopwatch sw = Stopwatch.createUnstarted();
    RandomDataProvider randomDataProvider = new RandomDataProvider();
    for (int i = 0; i < 1000; i++) {
      sw.start();
      dCepKnowledgeBase.inferOutput(randomDataProvider.nextDataPortion());
      System.out.println(sw.elapsed(TimeUnit.MICROSECONDS));
      sw.reset();
    }
  }
}
