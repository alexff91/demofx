package ru.spbstu.dis.ep.data.opc;

import au.com.bytecode.opencsv.CSVWriter;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import ru.spbstu.dis.ep.data.Tag;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class OpcCsvWriter {
  public static void main(String[] args)
  throws InterruptedException, AlreadyConnectedException, JIException, IOException,
      NotConnectedException, DuplicateGroupException, AddFailedException {

    final FileWriter fileWriter = new FileWriter("data.csv");
    fileWriter.write("sep=,\n");
    fileWriter.flush();
    CSVWriter writer = new CSVWriter(fileWriter, ',', '\0');
    writer.writeNext(
        Tag.TAG_TO_ID_MAPPING.entrySet().stream()
        .map(tagStringEntry -> tagStringEntry.getKey().toString())
        .collect(Collectors.toCollection(LinkedList::new))
        .toArray(new String[0]));

    final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();


    Thread.sleep(2000);

    while (true) {
      final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
      System.out.println(actualValues);
      final String[] data = Tag.TAG_TO_ID_MAPPING.entrySet().stream()
          .map(tagStringEntry -> "" + actualValues.get(tagStringEntry.getKey()))
          .collect(Collectors.toCollection(LinkedList::new))
          .toArray(new String[0]);
      writer.writeNext(data);
      writer.flush();
      Thread.sleep(1000);
    }
  }
}

