package ru.spbstu.dis.ep.data.opc;

import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.data.DataProvider;
import ru.spbstu.dis.ep.data.Tag;

public class SoftingOpcDataProvider implements DataProvider{
  OPCDataReader opcDataReader;


  public SoftingOpcDataProvider(){
    opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();
  }

  @Override
  public DataInput nextDataPortion() {
    return new DataInput(opcDataReader.getActualValues());
  }
}
