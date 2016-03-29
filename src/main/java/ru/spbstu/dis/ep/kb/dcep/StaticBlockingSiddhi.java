package ru.spbstu.dis.ep.kb.dcep;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import ru.spbstu.dis.ep.kb.ChosenAction;

import java.util.concurrent.*;

public class StaticBlockingSiddhi {
  private static SiddhiManager siddhiManager = new SiddhiManager();

  private static LinkedBlockingDeque<ChosenAction> actions = new LinkedBlockingDeque<>();

  private static String query1 = "" +
      "define stream dataStream (pressure double, lowerPressure double); " +
      "@info(name = 'query1') " +
      "from dataStream[pressure < 0.5] " +
      "select * " +
      "insert into outputStream ;";

  private static ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(query1);

  static {
    executionPlanRuntime.start();
  }

  private static InputHandler inputHandler = executionPlanRuntime.getInputHandler("dataStream");

  static {
    executionPlanRuntime.addCallback("query1", new QueryCallback() {
      @Override
      public void receive(long timeStamp, Event[] inEvents, org.wso2.siddhi.core.event.Event[] removeEvents) {
//        EventPrinter.print(timeStamp, inEvents, removeEvents);
        try {
          actions.put(new ChosenAction("StopEverything"));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void supplyData(double pressure, double lowerPressure) {
    try {
      inputHandler.send(new Object[]{pressure, lowerPressure});
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static ExecutorService executorService = Executors.newFixedThreadPool(1);

  public static ChosenAction getAction() {
    Future<ChosenAction> submit = executorService.submit(new Callable<ChosenAction>() {
      @Override
      public ChosenAction call() throws Exception {
        return actions.take();
      }
    });
    try {
      ChosenAction chosenAction = submit.get(25, TimeUnit.MILLISECONDS);
      return chosenAction;
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    } catch (ExecutionException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    } catch (TimeoutException e) {
      submit.cancel(true);
      return new ChosenAction("doNothing");
    }
  }
}