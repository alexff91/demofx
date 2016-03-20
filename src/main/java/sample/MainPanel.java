package sample;/**
 * Created by Александр on 18.03.2016.
 */

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.defuzzifier.Bisector;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import java.util.Optional;

public class MainPanel extends Application {

  @Override
  public void start(Stage primaryStage)
  throws Exception {
  }

  public static void main(String[] args) {
    new JFXPanel();
    Engine engine = new Engine();
    engine.setName("EmergencyPredictor");

    InputVariable tGrowth = new InputVariable();
    tGrowth.setName("Growth");
    tGrowth.setRange(0.000, 1.000);
    tGrowth.addTerm(new Triangle("Slow", 0.000, 0.250, 0.500));
    tGrowth.addTerm(new Triangle("Normal", 0.250, 0.500, 0.750));
    tGrowth.addTerm(new Triangle("Fast", 0.500, 0.750, 1.000));
    engine.addInputVariable(tGrowth);

    InputVariable tCloseness = new InputVariable();
    tCloseness.setName("Closeness");
    tCloseness.setRange(0.000, 1.000);
    tCloseness.addTerm(new Triangle("Far", 0.000, 0.250, 0.500));
    tCloseness.addTerm(new Triangle("Medium", 0.250, 0.500, 0.750));
    tCloseness.addTerm(new Triangle("Near", 0.500, 0.750, 1.000));
    engine.addInputVariable(tCloseness);

    InputVariable tankOverflowRisk = new InputVariable();
    tankOverflowRisk.setName("Risk");
    tankOverflowRisk.setRange(0.000, 1.000);
    tankOverflowRisk.addTerm(new Triangle("Low", 0.000, 0.250, 0.500));
    tankOverflowRisk.addTerm(new Triangle("Medium", 0.250, 0.500, 0.750));
    tankOverflowRisk.addTerm(new Triangle("High", 0.500, 0.750, 1.000));
    engine.addInputVariable(tankOverflowRisk);

    OutputVariable action = new OutputVariable();
    action.setName("Action");
    action.setRange(0.000, 1.000);
    action.setDefaultValue(Double.NaN);
    action.addTerm(new Triangle("DoNothing", 0.000, 0.250, 0.500));
    action.addTerm(new Triangle("YieldDecisionToUser", 0.250, 0.500, 0.750));
    action.addTerm(new Triangle("EmergencyStop", 0.500, 0.750, 1.000));
    engine.addOutputVariable(action);

    RuleBlock ruleBlock = new RuleBlock("firstBlock", new Minimum(), new Maximum(), new Minimum());
    ruleBlock.addRule(Rule.parse("if Growth is Fast and Closeness is Near then Action is EmergencyStop",
        engine));

    ruleBlock.addRule(Rule.parse("if Growth is Fast and Closeness is Medium then Action is " +
            "YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Fast and Closeness is Far then Action is " +
            "YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Normal and Closeness is Near then Action is YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Normal and Closeness is Medium then Action is DoNothing",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Normal and Closeness is Far then Action is DoNothing",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Slow and Closeness is Near then Action is " +
            "YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Slow and Closeness is Medium then Action is DoNothing",
        engine));
    ruleBlock.addRule(Rule.parse("if Growth is Slow and Closeness is Far then Action is DoNothing",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is High and Closeness is Medium and Growth is Fast " +
            "then Action is " +
            "EmergencyStop",
        engine));

    ruleBlock.addRule(Rule.parse("if Risk is High and Closeness is Near then Action is " +
            "YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is High and Closeness is Medium then Action is YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is High and Closeness is Far then Action is EmergencyStop",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is Medium and Closeness is Near then Action is " +
            "DoNothing",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is Medium and Closeness is Medium then Action is YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is Medium and Closeness is Far then Action is YieldDecisionToUser",
        engine));
    ruleBlock.addRule(Rule.parse("if Risk is Low and Growth is Fast then Action is " +
            "YieldDecisionToUser",
        engine));
    engine.addRuleBlock(ruleBlock);

    //    engine.configure("Minimum", "Maximum", "Minimum", "Maximum", "MeanOfMaximum");

    engine.configure(new AlgebraicProduct(), new Maximum(), new Minimum(),
        new Maximum(), new Bisector());

    StringBuilder status = new StringBuilder();
    if (!engine.isReady(status)) {
      throw new RuntimeException("Engine not ready. "
          + "The following errors were encountered:\n" + status.toString());
    }

    for (int i = 1; i < 5; i++) {
      double growthValue = tGrowth.getMinimum() + i * (tGrowth.range() / 5);
      tGrowth.setInputValue(growthValue);

      for (int j = 1; j < 5; j++) {
        double closenessValue = tCloseness.getMinimum() + i * (tCloseness.range() / 5);
        tCloseness.setInputValue(closenessValue);

        for (int k = 1; k < 5; k++) {
          double tankOverflowRiskValue = tankOverflowRisk.getMinimum() + i * (tankOverflowRisk
              .range() / 5);
          try {
            Thread.sleep(600);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          tCloseness.setInputValue(tankOverflowRiskValue);
          engine.process();
          FuzzyLite.logger().info(
              String.format(
                  "growth=%s, closeness=%s, tankOverflowRisk=%s -> "
                      + "Action.output=%s, action=%s",
                  Op.str(growthValue),
                  Op.str(closenessValue),
                  Op.str(tankOverflowRiskValue),
                  Op.str(action.getOutputValue()),
                  action.fuzzyOutputValue()));
          notifier(Notifications.create().title("Emergency").text(String.format(
              "growth=%s, closeness=%s, tankOverflowRisk=%s -> "
                  + "Action.output=%s, action=%s",
              Op.str(growthValue),
              Op.str(closenessValue),
              Op.str(tankOverflowRiskValue),
              Op.str(action.getOutputValue()),
              action.fuzzyOutputValue()))
              .position
                  (Pos
                      .TOP_RIGHT),tankOverflowRiskValue);
        }

      }

    }



    showDialog();
  }

  private static void showDialog() {
    Platform.runLater(() -> {
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Подтверждение");
          alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
          alert.setContentText("Choose your option.");

          ButtonType buttonTypeOne = new ButtonType("One");
          ButtonType buttonTypeTwo = new ButtonType("Two");
          ButtonType buttonTypeThree = new ButtonType("Three");
          ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

          alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == buttonTypeOne){
            // ... user chose "One"
          } else if (result.get() == buttonTypeTwo) {
            // ... user chose "Two"
          } else if (result.get() == buttonTypeThree) {
            // ... user chose "Three"
          } else {
            // ... user chose CANCEL or closed the dialog
          }
        }
    );
  }
  private static void notifier(Notifications notif, double dangerLevel) {
    Platform.runLater(() -> {
          Stage owner = new Stage(StageStyle.TRANSPARENT);
          StackPane root = new StackPane();
          root.setStyle("-fx-background-color: TRANSPARENT");
          Scene scene = new Scene(root, 1, 1);
          scene.setFill(Color.TRANSPARENT);
          owner.setScene(scene);
          owner.setWidth(1);
          owner.setHeight(1);
          owner.toBack();
          owner.show();
          if(dangerLevel > 0.5d){
            notif.show();
          }
          if(dangerLevel > 0.0d  && dangerLevel <= 0.3d){
            notif.showInformation();
          }
          if(dangerLevel > 0.3d  && dangerLevel <= 0.5d){
            notif.showWarning();
          }
        }
    );
  }
}