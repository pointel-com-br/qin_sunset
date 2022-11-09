package br.net.pin.qin_sunwiz.mage;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

import br.net.pin.qin_sunwiz.data.Nature;

public class WizData {
  public static String[] getColumnNames(ResultSet results) throws Exception {
    var meta = results.getMetaData();
    var names = new String[meta.getColumnCount()];
    for (int i = 1; i <= names.length; i++) {
      names[(i - 1)] = meta.getColumnName(i);
    }
    return names;
  }

  public static Nature[] getNaturesFrom(ResultSet results) throws Exception {
    var metaData = results.getMetaData();
    var columnCount = metaData.getColumnCount();
    var natures = new Nature[columnCount];
    for (int i = 1; i <= columnCount; i++) {
      natures[(i - 1)] = WizData.getNatureOfSQL(metaData.getColumnType(i));
    }
    return natures;
  }

  public static Nature getNatureOfSQL(int jdbcType) {
    switch (jdbcType) {
      case 16:
        return Nature.BOOL;
      case -7:
      case -6:
      case 5:
      case 4:
        return Nature.INT;
      case -5:
        return Nature.LONG;
      case 6:
      case 7:
        return Nature.FLOAT;
      case 8:
      case 2:
      case 3:
        return Nature.DOUBLE;
      case 1:
      case -15:
        return Nature.CHAR;
      case 12:
      case -1:
      case -9:
      case -16:
        return Nature.CHARS;
      case 91:
        return Nature.DATE;
      case 92:
      case 2013:
        return Nature.TIME;
      case 93:
      case 2014:
        return Nature.TIMESTAMP;
      case -2:
      case -3:
      case -4:
      case 2004:
      case 2005:
      case 2011:
      case 2009:
        return Nature.BYTES;
      default:
        throw new UnsupportedOperationException(
            "Could not identify the data nature of jdbc type: " + jdbcType);
    }
  }

  public static Object getValueFrom(Nature nature, String formatted) throws Exception {
    if (formatted == null || formatted.isEmpty()) {
      return null;
    }
    switch (nature) {
      case BOOL:
      case BIT:
        return Boolean.parseBoolean(formatted);
      case BYTE:
        return Byte.parseByte(formatted);
      case INT:
      case SERIAL:
        return Integer.parseInt(formatted);
      case LONG:
      case BIG_SERIAL:
        return Long.parseLong(formatted);
      case FLOAT:
      case REAL:
        return Float.parseFloat(formatted);
      case DOUBLE:
      case NUMERIC:
        return Double.parseDouble(formatted);
      case CHAR:
        return formatted.charAt(0);
      case CHARS:
      case TEXT:
        return formatted;
      case DATE:
        return WizDate.parseDate(formatted);
      case TIME:
        return WizDate.parseTime(formatted);
      case TIMESTAMP:
        return WizDate.parseTimestamp(formatted);
      case BYTES:
      case BLOB:
        return WizBytes.decodeFromBase64(formatted);
      default:
        throw new Exception("DataType Not Supported.");
    }
  }

  public static String formatValue(Nature nature, Object value) throws Exception {
    if (value == null) {
      return "";
    }
    switch (nature) {
      case BOOL:
      case BIT:
      case BYTE:
      case TINY:
      case SMALL:
      case INT:
      case LONG:
      case FLOAT:
      case REAL:
      case DOUBLE:
      case NUMERIC:
      case CHAR:
      case CHARS:
      case TEXT:
        return String.valueOf(value);
      case DATE:
        return WizDate.formatDate(WizDate.get(value));
      case TIME:
        return WizDate.formatTime(WizDate.get(value));
      case TIMESTAMP:
        return WizDate.formatTimestamp(WizDate.get(value));
      case BYTES:
      case BLOB:
        return WizBytes.encodeToBase64(WizBytes.get(value));
      default:
        throw new Exception("DataType Not Supported.");
    }
  }

  public static Boolean getBoolean(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Boolean) {
      return (Boolean) data;
    }
    throw new RuntimeException("Could not convert to Boolean from class: " + data.getClass().getCanonicalName());
  }

  public static Byte getByte(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Byte) {
      return (Byte) data;
    }
    throw new RuntimeException("Could not convert to Byte from class: " + data.getClass().getCanonicalName());
  }

  public static Short getShort(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Short) {
      return (Short) data;
    }
    throw new RuntimeException("Could not convert to Short from class: " + data.getClass().getCanonicalName());
  }

  public static Integer getInteger(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Integer) {
      return (Integer) data;
    }
    throw new RuntimeException("Could not convert to Integer from class: " + data.getClass().getCanonicalName());
  }

  public static Long getLong(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Long) {
      return (Long) data;
    }
    throw new RuntimeException("Could not convert to Long from class: " + data.getClass().getCanonicalName());
  }

  public static Float getFloat(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Float) {
      return (Float) data;
    }
    throw new RuntimeException("Could not convert to Float from class: " + data.getClass().getCanonicalName());
  }

  public static Double getDouble(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Double) {
      return (Double) data;
    }
    throw new RuntimeException("Could not convert to Double from class: " + data.getClass().getCanonicalName());
  }

  public static BigDecimal getBigDecimal(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof BigDecimal) {
      return (BigDecimal) data;
    }
    throw new RuntimeException("Could not convert to BigDecimal from class: " + data.getClass().getCanonicalName());
  }

  public static String getString(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof String) {
      return (String) data;
    }
    return data.toString();
  }

  public static Date getDate(Object data) throws Exception {
    return new Date(WizDate.get(data).getTime());
  }

  public static Time getTime(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Time) {
      return (Time) data;
    }
    throw new RuntimeException("Could not convert to Time from class: " + data.getClass().getCanonicalName());
  }

  public static Timestamp getTimestamp(Object data) {
    if (data == null) {
      return null;
    }

    if (data instanceof Timestamp) {
      return (Timestamp) data;
    }
    throw new RuntimeException("Could not convert to Timestamp from class: " + data.getClass().getCanonicalName());
  }

  public static byte[] getBytes(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof byte[]) {
      return (byte[]) data;
    }
    throw new RuntimeException("Could not convert to Bytes from class: " + data.getClass().getCanonicalName());
  }

  public static Blob getBlob(Object data) {
    if (data == null) {
      return null;
    }
    if (data instanceof Blob) {
      return (Blob) data;
    }
    throw new RuntimeException("Could not convert to Blob from class: " + data.getClass().getCanonicalName());
  }

  public static void setParams(PreparedStatement statement, Object[] params) throws Exception {
    for (int i = 0; i < params.length; i++) {
      statement.setObject(i + 1, params[i]);
    }    
  }
}
