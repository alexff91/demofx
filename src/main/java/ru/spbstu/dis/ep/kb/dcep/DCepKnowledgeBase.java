package ru.spbstu.dis.ep.kb.dcep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.receiver.Receiver;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.kb.ChosenAction;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.kb.KnowledgeBase;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DCepKnowledgeBase implements KnowledgeBase, Serializable{
  private static LinkedBlockingDeque<DataInput> incomingData = new LinkedBlockingDeque<>();
  private static LinkedBlockingDeque<ChosenAction> outgoingData = new LinkedBlockingDeque<>();

  public DCepKnowledgeBase() {
    Executors.newFixedThreadPool(1).submit(() -> {
      SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
      JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.milliseconds(25));
      JavaReceiverInputDStream<DataInput> dataInputReceiver = jssc.receiverStream(new Receiver<DataInput>(StorageLevel.MEMORY_ONLY()) {
        @Override
        public void onStart() {
          Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
              try {
                store(incomingData.take());
              } catch (InterruptedException e) {
                e.printStackTrace();
                throw new IllegalStateException();
              }
          }, 1000L, 100L, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onStop() {

        }
      });

      JavaDStream<ChosenAction> actionStream = dataInputReceiver.map(dataInput -> {
        StaticBlockingSiddhi.supplyData(
            dataInput.getDataForTag(Tag.PRESSURE),
            dataInput.getDataForTag(Tag.LOWER_PRESSURE));
        return StaticBlockingSiddhi.getAction();
//        return new ChosenAction("ad");
      });

      actionStream.foreachRDD((Function2<JavaRDD<ChosenAction>, Time, Void>) (chosenActionJavaRDD, time) -> {
        chosenActionJavaRDD.foreach(chosenAction -> {
          try {
            outgoingData.offer(chosenAction);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        return null;
      });

      jssc.start();
    });
  }

  @Override
  public ChosenAction inferOutput(DataInput input) {
    try {
      incomingData.put(input);
      return outgoingData.take();
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }
}
