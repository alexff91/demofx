package ru.spbstu.dis.ep.kb.dcep;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

// docs can be found at https://docs.wso2.com/display/CEP300/WSO2+Complex+Event+Processor+Documentation
public class SiddhiExample {
  public static void main(String[] args) throws InterruptedException {
    // Creating Siddhi Manager
    SiddhiManager siddhiManager = new SiddhiManager();

    String executionPlan = "" +
        "define stream dataStream (pressure double, lowerPressure double); " +
        "@info(name = 'query1') " +
        "from dataStream[pressure < 0.5] " +
        "select * " +
        "insert into outputStream ;";

    //Generating runtime
    ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(executionPlan);

    //Adding callback to retrieve output events from query
    executionPlanRuntime.addCallback("query1", new QueryCallback() {
      @Override
      public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
        EventPrinter.print(timeStamp, inEvents, removeEvents);
      }
    });

    //Retrieving InputHandler to push events into Siddhi
    InputHandler inputHandler = executionPlanRuntime.getInputHandler("dataStream");

    //Starting event processing
    executionPlanRuntime.start();

    Thread.sleep(500);

    //Sending events to Siddhi
    inputHandler.send(new Object[]{0.8, 0.5,});
    inputHandler.send(new Object[]{0.1, 0.5,});

    Thread.sleep(500);

    //Shutting down the runtime
    executionPlanRuntime.shutdown();

    //Shutting down Siddhi
    siddhiManager.shutdown();
  }
}
