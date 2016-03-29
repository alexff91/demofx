package ru.spbstu.dis.ep.kb.nf.fuzzy.examples;

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

public class JFuzzyLiteExample {

  public static void main(String[] args) {
    Engine engine = new Engine();
    engine.setName("simple-dimmer");

    InputVariable ambient = new InputVariable();
    ambient.setName("Ambient");
    ambient.setRange(0.000, 1.000);
    ambient.addTerm(new Triangle("DARK", 0.000, 0.250, 0.500));
    ambient.addTerm(new Triangle("MEDIUM", 0.250, 0.500, 0.750));
    ambient.addTerm(new Triangle("BRIGHT", 0.500, 0.750, 1.000));
    engine.addInputVariable(ambient);

    OutputVariable power = new OutputVariable();
    power.setName("Power");
    power.setRange(0.000, 1.000);
    power.setDefaultValue(Double.NaN);
    power.addTerm(new Triangle("LOW", 0.000, 0.250, 0.500));
    power.addTerm(new Triangle("MEDIUM", 0.250, 0.500, 0.750));
    power.addTerm(new Triangle("HIGH", 0.500, 0.750, 1.000));
    engine.addOutputVariable(power);

    RuleBlock ruleBlock = new RuleBlock();
    ruleBlock.addRule(Rule.parse("if Ambient is DARK then Power is HIGH", engine));
    ruleBlock.addRule(Rule.parse("if Ambient is MEDIUM then Power is MEDIUM", engine));
    ruleBlock.addRule(Rule.parse("if Ambient is BRIGHT then Power is LOW", engine));
    engine.addRuleBlock(ruleBlock);

    engine.configure("", "", "Minimum", "Maximum", "Centroid");

    StringBuilder status = new StringBuilder();
    if (!engine.isReady(status)) {
      throw new RuntimeException("Engine not ready. "
          + "The following errors were encountered:\n" + status.toString());
    }

    for (int i = 0; i < 50; ++i) {
      double light = ambient.getMinimum() + i * (ambient.range() / 50);
      ambient.setInputValue(light);
      engine.process();
      FuzzyLite.logger().info(String.format(
          "Ambient.input = %s -> Power.output = %s",
          Op.str(light), Op.str(power.getOutputValue())));
    }
  }
}