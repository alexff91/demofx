package ru.spbstu.dis.ep.kb.nf.nn;

public class NeuralNetworkOutput {

  private final String situation;

  private double output;

  public NeuralNetworkOutput(final String situation, final double output) {
    this.situation = situation;
    this.output = output;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("NeuralNetworkOutput{");
    sb.append("situation='").append(situation).append('\'');
    sb.append(", output=").append(output);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final NeuralNetworkOutput that = (NeuralNetworkOutput) o;

    if (Double.compare(that.output, output) != 0) {
      return false;
    }
    return !(situation != null ? !situation.equals(that.situation) : that.situation != null);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = situation != null ? situation.hashCode() : 0;
    temp = Double.doubleToLongBits(output);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  public String getSituation() {
    return situation;
  }

  public double getNeuralNetworkOutput() {
    return output;
  }
}
