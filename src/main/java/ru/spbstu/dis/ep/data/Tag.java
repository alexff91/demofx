package ru.spbstu.dis.ep.data;

import com.google.common.collect.Maps;
import java.util.HashMap;

public enum Tag {
  PRESSURE,
  LOWER_PRESSURE,
  TEST_3B1,
  TEST_3B2,
  TEST_3B3,
  TEST_MIXER,
  TEST_TEMP1,
  TEST_TEMP2,
  TEST_TEMP3,
  TEST_Proc,
  TEST_3PV1_TP,
  TEST_3CO1_Real,
  TEST_Pump3m1,
  MIX_2B6,
  MIX_2B7,
  MIX_2M2;

  public static HashMap<Tag, String> TAG_TO_ID_MAPPING = Maps.newHashMap();
  static {
    // real OPC server tags
    TAG_TO_ID_MAPPING.put(TEST_TEMP1, "ReactorConnection/M/Temp1");
    TAG_TO_ID_MAPPING.put(TEST_TEMP2, "ReactorConnection/M/Temp2");
    TAG_TO_ID_MAPPING.put(TEST_TEMP3, "ReactorConnection/M/Temp3");
    TAG_TO_ID_MAPPING.put(TEST_Proc, "ReactorConnection/M/TempProc");//Active Temperature
    TAG_TO_ID_MAPPING.put(TEST_3CO1_Real, "ReactorConnection/M/3CO1_Real");//controlled
    TAG_TO_ID_MAPPING.put(TEST_3PV1_TP, "ReactorConnection/M/3PV1_TP");//Текущая температура процесса
    TAG_TO_ID_MAPPING.put(TEST_MIXER, "ReactorConnection/M/TP_3M4");
    TAG_TO_ID_MAPPING.put(TEST_Pump3m1, "ReactorConnection/M/TP_3M1");
    TAG_TO_ID_MAPPING.put(TEST_3B1, "ReactorConnection/E/3B1");
    TAG_TO_ID_MAPPING.put(TEST_3B2, "ReactorConnection/E/3B2");
    TAG_TO_ID_MAPPING.put(TEST_3B3, "ReactorConnection/E/3B3");
    TAG_TO_ID_MAPPING.put(MIX_2B6, "MixingConnection/E/2B6");
    TAG_TO_ID_MAPPING.put(MIX_2B7, "MixingConnection/E/2B7");
    TAG_TO_ID_MAPPING.put(MIX_2M2, "MixingConnection/M/TP_2M2");

    // demo OPC server tags
    TAG_TO_ID_MAPPING.put(Tag.PRESSURE, "maths.sin");
    TAG_TO_ID_MAPPING.put(Tag.LOWER_PRESSURE, "maths.cos");
  }
}
