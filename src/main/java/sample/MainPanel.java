package sample;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;


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
import org.jfree.data.time.Millisecond;
import org.jfree.ui.RefineryUtilities;
import popup.ssn.NotificationPopup;
import sun.plugin.util.UIUtil;

import javax.swing.*;

public
class MainPanel {

    public static final Triangle LOW = new Triangle("LOW",
                                                    0.000,
                                                    0.250,
                                                    0.500);

    public static final Triangle NORMAL = new Triangle("NORMAL",
                                                       0.250,
                                                       0.500,
                                                       0.750);

    public static final Triangle HIGHT = new Triangle("HIGH",
                                                      0.500,
                                                      0.750,
                                                      1.000);

    private static boolean alertDialogShown = false;
    private static boolean emergencyStop = false;
    static double growthValue;
    static double closenessValue;
    static double tankOverflowRiskValue;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        initClosenessValueChart();
initGrowthValueChart();
        initRiskValueChart();
        runSimulation();


    }

    private static DynamicDataDemo closenessChartFrame = new DynamicDataDemo("Closeness Value");
    private static
    void initClosenessValueChart() {

        Thread th = new Thread(() -> {
            while (true) {
                closenessChartFrame.lastValue = closenessValue;
    
        Thread th  = new Thread(() -> {
            while(true) {
                demo.lastValue = closenessValue;
                final Millisecond now = new Millisecond();
                System.out.println("Now = " + now.toString());
                closenessChartFrame.series.add(new Millisecond(),
                                closenessChartFrame.lastValue);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

        SwingUtilities.invokeLater(new Runnable() {
            public
            void run() {
                closenessChartFrame.addButton();
                closenessChartFrame.pack();
                closenessChartFrame.setVisible(true);
            }
        });
    }

    private static
    void initGrowthValueChart() {
        final DynamicDataDemo demo = new DynamicDataDemo("Growth value");

        Thread th = new Thread(() -> {
            while (true) {
                demo.lastValue = growthValue;
                final Millisecond now = new Millisecond();
                System.out.println("Now = " + now.toString());
                demo.series.add(new Millisecond(),
                                demo.lastValue);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

        closenessChartFrame.addChart(demo.chartPanel);
    }

    private static
    void initRiskValueChart() {
        final DynamicDataDemo demo = new DynamicDataDemo("Tank Overflow Risk");

        Thread th = new Thread(() -> {
            while (true) {
                demo.lastValue = tankOverflowRiskValue;
                final Millisecond now = new Millisecond();
                System.out.println("Now = " + now.toString());
                demo.series.add(new Millisecond(),
                                demo.lastValue);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

        closenessChartFrame.addChart(demo.chartPanel);
    }

    private static Engine engine = new Engine();

    private static
    void runSimulation() {

        engine.setName("EmergencyPredictor");

        InputVariable tGrowth = new InputVariable();
        tGrowth.setName("GROWTH");
        generateTriangularTerm(tGrowth);
        engine.addInputVariable(tGrowth);

        InputVariable tCloseness = new InputVariable();
        tCloseness.setName("CLOSENESS");
        generateTriangularTerm(tCloseness);
        engine.addInputVariable(tCloseness);

        InputVariable tankOverflowRisk = new InputVariable();
        tankOverflowRisk.setName("RISK");
        generateTriangularTerm(tankOverflowRisk);
        engine.addInputVariable(tankOverflowRisk);

        OutputVariable action = new OutputVariable();
        action.setName("ACTION");
        action.setRange(0.000,
                        1.000);
        action.setDefaultValue(Double.NaN);
        action.addTerm(new Triangle("DO_NOTHING",
                                    0.000,
                                    0.250,
                                    0.500));
        action.addTerm(new Triangle("USER_DECISION",
                                    0.250,
                                    0.500,
                                    0.750));
        action.addTerm(new Triangle("EMERGENCY_STOP",
                                    0.500,
                                    0.750,
                                    1.000));
        engine.addOutputVariable(action);

        RuleBlock ruleBlock = new RuleBlock("firstBlock",
                                            new Minimum(),
                                            new Maximum(),
                                            new Minimum());
        ruleBlock.addRule(
                Rule.parse("if GROWTH is HIGH and CLOSENESS is HIGH then ACTION is " +
                                   "EMERGENCY_STOP",
                           engine));

        ruleBlock.addRule(Rule.parse(
                "if GROWTH is HIGH and CLOSENESS is NORMAL then ACTION is "
                        + "USER_DECISION",
                engine));
        ruleBlock.addRule(Rule.parse(
                "if GROWTH is HIGH and CLOSENESS is LOW then ACTION is "
                        + "USER_DECISION",
                engine));
        ruleBlock.addRule(Rule.parse(
                "if GROWTH is NORMAL and CLOSENESS is HIGH then ACTION is USER_DECISION",
                engine));
        ruleBlock.addRule(
                Rule.parse("if GROWTH is NORMAL and CLOSENESS is NORMAL then ACTION is " +
                                   "DO_NOTHING",
                           engine));
        ruleBlock.addRule(Rule
                                  .parse("if GROWTH is NORMAL and CLOSENESS is LOW then" +
                                                 " ACTION is DO_NOTHING",
                                         engine));
        ruleBlock.addRule(Rule.parse(
                "if GROWTH is LOW and CLOSENESS is HIGH then ACTION is "
                        + "USER_DECISION",
                engine));
        ruleBlock.addRule(
                Rule.parse("if GROWTH is LOW and CLOSENESS is NORMAL then ACTION is " +
                                   "DO_NOTHING",
                           engine));
        ruleBlock.addRule(
                Rule.parse("if GROWTH is LOW and CLOSENESS is LOW then ACTION is " +
                                   "DO_NOTHING",
                           engine));
        ruleBlock.addRule(Rule.parse(
                "if RISK is HIGH and CLOSENESS is NORMAL and GROWTH is HIGH "
                        + "then ACTION is " + "EMERGENCY_STOP",
                engine));

        ruleBlock.addRule(Rule.parse(
                "if RISK is HIGH and CLOSENESS is HIGH then ACTION is " + "USER_DECISION",
                engine));
        ruleBlock.addRule(Rule.parse(
                "if RISK is HIGH and CLOSENESS is NORMAL then ACTION is USER_DECISION",
                engine));
        ruleBlock.addRule(Rule
                                  .parse("if RISK is HIGH and CLOSENESS is LOW then " +
                                                 "ACTION is EMERGENCY_STOP",
                                         engine));
        ruleBlock.addRule(Rule
                                  .parse("if RISK is NORMAL and CLOSENESS is HIGH then " +
                                                 "ACTION is "
                                                 + "DO_NOTHING",
                                         engine));
        ruleBlock.addRule(Rule.parse(
                "if RISK is NORMAL and CLOSENESS is NORMAL then ACTION is USER_DECISION",
                engine));
        ruleBlock.addRule(Rule.parse(
                "if RISK is NORMAL and CLOSENESS is LOW then ACTION is USER_DECISION",
                engine));
        ruleBlock.addRule(Rule.parse(
                "if RISK is LOW and GROWTH is HIGH then ACTION is " + "USER_DECISION",
                engine));
        engine.addRuleBlock(ruleBlock);

        // engine.configure("Minimum", "Maximum", "Minimum", "Maximum", "MeanOfMaximum");

        engine.configure(new AlgebraicProduct(),
                         new Maximum(),
                         new Minimum(),
                         new Maximum(),
                         new Bisector());

        StringBuilder status = new StringBuilder();
        if (!engine.isReady(status)) {
            throw new RuntimeException(
                    "Engine not ready. " + "The following errors were encountered:\n"
                            + status.toString());
        }
        for (int i = 1;
             !emergencyStop;
             i++) {
            growthValue = tGrowth.getMinimum() + i * (tGrowth.range() / 5);
            tGrowth.setInputValue(growthValue);

            for (int j = 1;
                 j < 3;
                 j++) {

                closenessValue = tCloseness.getMinimum() + i * (tCloseness.range() / 3);
                tCloseness.setInputValue(closenessValue);

                for (int k = 1; k < 3; k++) {

                    tankOverflowRiskValue = tankOverflowRisk.getMinimum() + i * (tankOverflowRisk.range() / 3);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tCloseness.setInputValue(tankOverflowRiskValue);
                    engine.process();
                    if(action
                            .highestMembershipTerm
                                    (action.getOutputValue()) != null  && action
                            .highestMembershipTerm
                            (action.getOutputValue())
                             .getName().equals("USER_DECISION")){
                        // show a joptionpane dialog using showMessageDialog
                        Object[] options = {"Turn off First Pump",
                                "Turn off Second Pump",
                                "Turn off Third Pump", "Close all pumps",
                                "Stop Station"};
                        int n = JOptionPane.showOptionDialog(closenessChartFrame,
                                                             "Would you like to close " +
                                                                     "pumps?",
                                                             "User decision",
                                                             JOptionPane.YES_NO_CANCEL_OPTION,
                                                             JOptionPane.QUESTION_MESSAGE,
                                                             null,
                                                             options,
                                                             options[2]);
                        if(n== 1){
                            closenessValue -= closenessValue/2;
                            growthValue    -= growthValue/2;
                            tankOverflowRiskValue   -= tankOverflowRiskValue/2;
                        }
                        if(n== 2){
                            closenessValue -= closenessValue/2;
                            growthValue    -= growthValue/2;
                            tankOverflowRiskValue   -= tankOverflowRiskValue/2;
                        }
                        if(n== 3){
                            closenessValue =0;
                            growthValue   =0;
                            tankOverflowRiskValue  =0;
                            return;
                        }
                        if(n== 4){
                            closenessValue =0;
                            growthValue   =0;
                            tankOverflowRiskValue  =0;
                            return;
                        }
                        if(n== 0){
                            closenessValue -= closenessValue/2;
                            growthValue    -= growthValue/2;
                            tankOverflowRiskValue   -= tankOverflowRiskValue/2;
                        }
                    }
                    if(action
                            .highestMembershipTerm
                                    (action.getOutputValue()) == null || action
                            .highestMembershipTerm(action.getOutputValue())
                             .getName().equals("EMERGENCY_STOP")){
                        // show a joptionpane dialog using showMessageDialog
                        JOptionPane.showMessageDialog(closenessChartFrame,
                                                      "Water Overflow",
                                                      "Emergency situation, Station " +
                                                              "stopped",
                                                      JOptionPane.ERROR_MESSAGE);
                        closenessValue =0;
                        growthValue   =0;
                        tankOverflowRiskValue  =0;
                        return;
                    }
                    FuzzyLite.logger()
                             .info(String.format(
                                     "growth=%s, closeness=%s, tankOverflowRisk=%s -> "
                                             + "ACTION.output=%s, action=%s",
                                     Op.str(growthValue),
                                     Op.str(closenessValue),
                                     Op.str(tankOverflowRiskValue),
                                     Op.str(action.getOutputValue()),
                                     action.fuzzyOutputValue()));
                    notifier(String.format(
                            "GROWTH=%s,\nCLOSENESS=%s,\nOVERFLOW_RISK=%s " +
                                    "->\n"
                                    + " RECOMMENDED ACTION=%s,\nACTIONS=%s",
                            tGrowth.highestMembershipTerm(tGrowth.getInputValue())
                                   .getName(),
                            tCloseness.highestMembershipTerm(closenessValue)
                                      .getName(),
                            tankOverflowRisk.highestMembershipTerm(tankOverflowRiskValue)
                                            .getName(),
                            action.highestMembershipTerm(action.getOutputValue())
                                  .getName(),
                            action.fuzzyOutputValue())
                            ,
                             tankOverflowRiskValue);
                }

            }

        }

    }

    private static void generateTriangularTerm(InputVariable tankOverflowRisk) {
        tankOverflowRisk.setRange(0.000, 1.000);
        tankOverflowRisk.addTerm(LOW);
        tankOverflowRisk.addTerm(NORMAL);
        tankOverflowRisk.addTerm(HIGHT);
    }

    private static
    void notifier(
            String term,
            double dangerLevel) {

    private static void notifier(String term, double dangerLevel) {


        if (dangerLevel > 0.5d) {
            ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/alarm.png"));
            showErrorNotif(term, new Color(249, 78, 30), icon);
        }
        if (dangerLevel > 0.0d && dangerLevel <= 0.3d) {
            ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/info.png"));
            showErrorNotif(term, new Color(127, 176, 72), icon);
        }
        if (dangerLevel > 0.3d && dangerLevel <= 0.5d) {
            ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/warning.png"));
            showErrorNotif(term, new Color(249, 236, 100), icon);
        }
    }

    private static void showErrorNotif(String term, Color color, ImageIcon icon) {
        NotificationPopup nf = new NotificationPopup(term);
        nf.setIcon(icon);
        nf.setWIDTH(850);
        nf.setHEIGHT(100);
        nf.setLocation(10, 10);
        nf.setFont(new Font("Tachoma", Font.LAYOUT_LEFT_TO_RIGHT, 14));
        nf.setAlwaysOnTop(true);
        nf.setHEIGHT(250);

        nf.setTitle("Ошибка");
        nf.setDisplayTime(2000);
        nf.setBackgroundColor1(Color.white);
        nf.setBackGroundColor2(color);
        nf.setForegroundColor(java.awt.Color.darkGray);
        nf.display();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}