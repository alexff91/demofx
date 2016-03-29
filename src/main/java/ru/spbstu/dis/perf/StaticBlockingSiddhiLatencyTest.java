package ru.spbstu.dis.perf;

import com.google.common.base.Stopwatch;
import ru.spbstu.dis.ep.kb.dcep.StaticBlockingSiddhi;

import java.util.concurrent.TimeUnit;

public class StaticBlockingSiddhiLatencyTest {
  public static void main(String[] args) {
    Stopwatch sw = Stopwatch.createUnstarted();
    for (int i = 0; i < 1000; i++) {
      sw.start();
      StaticBlockingSiddhi.supplyData(Math.random(),  Math.random());
      long elapsed = sw.elapsed(TimeUnit.MICROSECONDS);
      sw.reset();
      System.out.println(elapsed);
    }
  }
}
