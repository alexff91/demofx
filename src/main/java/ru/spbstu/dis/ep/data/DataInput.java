package ru.spbstu.dis.ep.data;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataInput implements Serializable{
  private Map<Tag, Double> data;

  public DataInput(Map<Tag, Double> data) {
    this.data = data;
  }

  public double getDataForTag(Tag tag) {
    Preconditions.checkArgument(data.containsKey(tag));
    return data.get(tag);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final DataInput dataInput = (DataInput) o;
    return !(data != null ? !data.equals(dataInput.data) : dataInput.data != null);
  }

  @Override
  public int hashCode() {
    return data != null ? data.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DataInput{");
    sb.append("data=").append(data);
    sb.append('}');
    return sb.toString();
  }

  public static DataInput withPressureAndLowerPressure(
      final double pressure,
      final double lowerPressure) {
    final HashMap<Tag, Double> data = new HashMap<>();
    data.put(Tag.PRESSURE, pressure);
    data.put(Tag.LOWER_PRESSURE, lowerPressure);
    return new DataInput(data);
  }
}
