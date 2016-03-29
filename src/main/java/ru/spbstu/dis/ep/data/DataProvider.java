package ru.spbstu.dis.ep.data;

import java.util.function.Supplier;

@FunctionalInterface
public interface DataProvider extends Supplier<DataInput> {
  @Override
  default DataInput get() {
    return nextDataPortion();
  }

  DataInput nextDataPortion();
}
