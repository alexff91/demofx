package sample;/**
 * Created by Александр on 18.03.2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;

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

import javax.swing.*;

public class MainPanel extends Application {

  private static boolean alertDialogShown = false;
  private static boolean emergencyStop = false;

  @Override
  public void start(Stage primaryStage)
  throws Exception {

  }

  static double growthValue;
  static double closenessValue;
  static double tankOverflowRiskValue;

  public static void main(String[] args) {

    runSimulation();

  }
  static JFrame frame;

  private static void initAndShowGUI() {
    // This method is invoked on Swing thread
    frame = new JFrame("FX");

    final JFXPanel fxPanel = new JFXPanel();
    frame.add(fxPanel);
    frame.pack();
    frame.setSize(800,800);
    frame.setVisible(true);

  }


  private static void runSimulation() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    new JFXPanel();
    Engine engine = new Engine();
    engine.setName("EmergencyPredictor");

    InputVariable tGrowth = new InputVariable();
    tGrowth.setName("Рост");
    tGrowth.setRange(0.000, 1.000);
    tGrowth.addTerm(new Triangle("Низкий", 0.000, 0.250, 0.500));
    tGrowth.addTerm(new Triangle("Нормальный", 0.250, 0.500, 0.750));
    tGrowth.addTerm(new Triangle("Быстрый", 0.500, 0.750, 1.000));
    engine.addInputVariable(tGrowth);

    InputVariable tCloseness = new InputVariable();
    tCloseness.setName("Близость");
    tCloseness.setRange(0.000, 1.000);
    tCloseness.addTerm(new Triangle("Маленькая", 0.000, 0.250, 0.500));
    tCloseness.addTerm(new Triangle("Средняя", 0.250, 0.500, 0.750));
    tCloseness.addTerm(new Triangle("Близкая", 0.500, 0.750, 1.000));
    engine.addInputVariable(tCloseness);

    InputVariable tankOverflowRisk = new InputVariable();
    tankOverflowRisk.setName("Риск");
    tankOverflowRisk.setRange(0.000, 1.000);
    tankOverflowRisk.addTerm(new Triangle("Низкий", 0.000, 0.250, 0.500));
    tankOverflowRisk.addTerm(new Triangle("Нормальный", 0.250, 0.500, 0.750));
    tankOverflowRisk.addTerm(new Triangle("Высокий", 0.500, 0.750, 1.000));
    engine.addInputVariable(tankOverflowRisk);

    OutputVariable action = new OutputVariable();
    action.setName("Действие");
    action.setRange(0.000, 1.000);
    action.setDefaultValue(Double.NaN);
    action.addTerm(new Triangle("Ничего_не_делать", 0.000, 0.250, 0.500));
    action.addTerm(new Triangle("Предоставить_решение_пользователю", 0.250, 0.500, 0.750));
    action.addTerm(new Triangle("Экстренная_остановка", 0.500, 0.750, 1.000));
    engine.addOutputVariable(action);

    RuleBlock ruleBlock = new RuleBlock("firstBlock", new Minimum(), new Maximum(), new Minimum());
    ruleBlock.addRule(
        Rule.parse("if Рост is Быстрый and Близость is Близкая then Действие is Экстренная_остановка", engine));

    ruleBlock.addRule(Rule.parse(
        "if Рост is Быстрый and Близость is Средняя then Действие is " + "Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule.parse(
        "if Рост is Быстрый and Близость is Маленькая then Действие is " + "Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule.parse(
        "if Рост is Нормальный and Близость is Близкая then Действие is Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(
        Rule.parse("if Рост is Нормальный and Близость is Средняя then Действие is Ничего_не_делать", engine));
    ruleBlock.addRule(Rule
        .parse("if Рост is Нормальный and Близость is Маленькая then Действие is Ничего_не_делать", engine));
    ruleBlock.addRule(Rule.parse(
        "if Рост is Низкий and Близость is Близкая then Действие is " + "Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(
        Rule.parse("if Рост is Низкий and Близость is Средняя then Действие is Ничего_не_делать", engine));
    ruleBlock.addRule(
        Rule.parse("if Рост is Низкий and Близость is Маленькая then Действие is Ничего_не_делать", engine));
    ruleBlock.addRule(Rule.parse("if Риск is Высокий and Близость is Средняя and Рост is Быстрый "
        + "then Действие is " + "Экстренная_остановка", engine));

    ruleBlock.addRule(Rule.parse(
        "if Риск is Высокий and Близость is Близкая then Действие is " + "Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule.parse(
        "if Риск is Высокий and Близость is Средняя then Действие is Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule
        .parse("if Риск is Высокий and Близость is Маленькая then Действие is Экстренная_остановка", engine));
    ruleBlock.addRule(Rule
        .parse("if Риск is Нормальный and Близость is Близкая then Действие is " + "Ничего_не_делать", engine));
    ruleBlock.addRule(Rule.parse(
        "if Риск is Нормальный and Близость is Средняя then Действие is Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule.parse(
        "if Риск is Нормальный and Близость is Маленькая then Действие is Предоставить_решение_пользователю",
        engine));
    ruleBlock.addRule(Rule.parse(
        "if Риск is Низкий and Рост is Быстрый then Действие is " + "Предоставить_решение_пользователю",
        engine));
    engine.addRuleBlock(ruleBlock);

    // engine.configure("Minimum", "Maximum", "Minimum", "Maximum", "MeanOfMaximum");

    engine.configure(new AlgebraicProduct(), new Maximum(), new Minimum(), new Maximum(), new Bisector());

    StringBuilder status = new StringBuilder();
    if (!engine.isReady(status)) {
      throw new RuntimeException(
          "Engine not ready. " + "The following errors were encountered:\n" + status.toString());
    }
    showChartDialog();
    for (int i = 1; !emergencyStop; i++) {
      growthValue = tGrowth.getMinimum() + i * (tGrowth.range() / 5);
      tGrowth.setInputValue(growthValue);

      for (int j = 1; j < 3; j++) {

        closenessValue = tCloseness.getMinimum() + i * (tCloseness.range() / 5);
        tCloseness.setInputValue(closenessValue);

        for (int k = 1; k < 3; k++) {

          tankOverflowRiskValue = tankOverflowRisk.getMinimum() + i * (tankOverflowRisk.range() / 5);

          tCloseness.setInputValue(tankOverflowRiskValue);
          engine.process();
          FuzzyLite.logger()
              .info(String.format(
                  "growth=%s, closeness=%s, tankOverflowRisk=%s -> "
                      + "Действие.output=%s, action=%s",
                  Op.str(growthValue), Op.str(closenessValue), Op.str(tankOverflowRiskValue),
                  Op.str(action.getOutputValue()), action.fuzzyOutputValue()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          notifier(Notifications.create().title("Предупреждение")
              .text(String.format(
                      "Рост уровня воды=%s,\r\nБлизость к переполнению=%s,\r\n" +
                              "Риск переполнения бака=%s,\r\n"
                              + "Вероятность действия=%s",
                      Op.str(growthValue),
                      Op.str(closenessValue),
                      Op.str(tankOverflowRiskValue),
                      action.fuzzyOutputValue()))
              .position(Pos.TOP_RIGHT), tankOverflowRiskValue);


        }

      }

    }

  }

  private static void showDialog() {
    Platform.runLater(() -> {
      alertDialogShown = true;
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.initStyle(StageStyle.UTILITY);
      alert.setTitle("Выбор действия");
      alert.setHeaderText("Выберите действие для предотвращения аварии");
      alert.setContentText("Отключить подачу воды из:");

      ButtonType buttonTypeOne = new ButtonType("Бак №1");
      ButtonType buttonTypeTwo = new ButtonType("Бак №2");
      ButtonType buttonTypeThree = new ButtonType("Бак №3");
      ButtonType buttonTypeAll = new ButtonType("Все баки");
        ButtonType buttonTypeSTOP = new ButtonType("Останов");
        ButtonType buttonTypeReciept2 = new ButtonType("Рецепт 2");
      ButtonType buttonTypeCancel = new ButtonType("Отменить", ButtonData.CANCEL_CLOSE);

      alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeAll,
         buttonTypeSTOP, buttonTypeReciept2, buttonTypeCancel);

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == buttonTypeOne) {
        growthValue = growthValue / 3;
        closenessValue = closenessValue / 3;
        tankOverflowRiskValue = tankOverflowRiskValue / 3;
        alert.close();
      } else if (result.get() == buttonTypeTwo) {
        growthValue = growthValue / 2;
        closenessValue = closenessValue / 2;
        tankOverflowRiskValue = tankOverflowRiskValue / 2;
        alert.close();
      } else if (result.get() == buttonTypeThree) {
        growthValue = growthValue / 4;
        closenessValue = growthValue / 4;
        tankOverflowRiskValue = tankOverflowRiskValue / 4;
        alert.close();
      }
      else if (result.get() == buttonTypeAll) {
        emergencyStop =true;
        alert.close();
      }
      else {
        // ... user chose CANCEL or closed the dialog
      }
      alertDialogShown = false;
    });
  }

  private static void showChartDialog() {
    Platform.runLater(() -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.initStyle(StageStyle.UTILITY);
      alert.setTitle("Мониторинг аварийных ситуаций на установке");
      alert.setHeaderText("Выберите действие для предотвращения аварии");
      alert.setContentText("Выберите действие:");

      Text text = new Text("Уровень воды в баке :  " + 225 + " мл");

        Text text1 = new Text("Расход воды :  " + 10 + " мл/мин");
      DialogPane pane;
        pane = new DialogPane();
        BarChart<String, Number> chart = generateBarChart();
        GridPane grid = new GridPane();
        grid.add(chart,0,0);
        grid.add(text,0,1);
        grid.add(generateLineChart(),1,0);
        grid.add(text1,1,1);
      //		lineChart.getData().add(series);
      pane.setContent(grid);
        pane.setContentText("Значение уровня воды = " + growthValue);
      alert.setDialogPane(pane);
      ButtonType buttonTypeCancel = new ButtonType("Закрыть", ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeStop = new ButtonType("Остановить станцию",
                                                   ButtonData.NO);

      alert.getButtonTypes().setAll(
              buttonTypeStop,buttonTypeCancel);

      Optional<ButtonType> result = alert.showAndWait();

    });
  }

    private static
    LineChart<String, Number> generateLineChart() {
        LineChart<String, Number> chart;

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Время");
        yAxis.setLabel("Расход воды, мл/мин");

        chart = new LineChart<>(xAxis, yAxis);

        XYChart.Series series;
        series = new XYChart.Series<>();
        chart.setTitle("Расход воды");
        chart.getData().add(series);
        int i =0;
        int acc =0;
        while (i<5){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                if (series != null) {
                    acc ++;
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }

        while (i<7){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                acc--;
                if (series != null) {
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }
        while (i<10){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                acc++;
                if (series != null) {
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }
        return chart;
    }

    private static
    BarChart<String, Number> generateBarChart() {
        BarChart<String, Number> chart;

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Время");
        yAxis.setLabel("Уровень воды, мл");

        chart = new BarChart<>(xAxis,yAxis);

        XYChart.Series series;
        series = new XYChart.Series<>();
        chart.setTitle("Уровень заполнения бака");
        chart.getData().add(series);
        int i =0;
        int acc = 0;
        while (i<15){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                if (series != null) {
                    acc = acc + i;
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }

        while (i<20){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                acc = acc - i;
                if (series != null) {
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }
        while (i<28){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime()+i*1000);
            try {
                acc = acc + i;
                if (series != null) {
                    series.getData().add(new XYChart.Data(dateFormat.format(date),
                                                          acc));
                }
            }catch (IllegalArgumentException ex){

            }
            i++;
        }
        return chart;
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
      notif.hideAfter(new Duration(6000));
      if (!alertDialogShown) {
        if (dangerLevel > 0.5d) {
          notif.showError();
          showDialog();
        }
        if (dangerLevel > 0.0d && dangerLevel <= 0.3d) {
//          notif.showInformation();
        }
        if (dangerLevel > 0.3d && dangerLevel <= 0.5d) {
          notif.showWarning();
        }
      }
    });
  }


}