package br.net.pin.qin_sunwiz.mage;

import java.util.Random;

public class WizRand {
  private static Random rand = null;

  private static void iniciaRand() {
    if (WizRand.rand == null) {
      WizRand.rand = new Random();
    }
  }

  public static Boolean getBool() {
    WizRand.iniciaRand();
    return WizRand.rand.nextBoolean();
  }

  public static Boolean getBool(Integer min, Integer max, Integer odds) {
    WizRand.iniciaRand();
    return WizRand.getInt(min, max) < odds;
  }

  public static Integer getDigit() {
    WizRand.iniciaRand();
    return WizRand.rand.nextInt(10);
  }

  public static Integer getInt() {
    WizRand.iniciaRand();
    return WizRand.rand.nextInt();
  }

  public static Integer getInt(Integer max) {
    WizRand.iniciaRand();
    return WizRand.rand.nextInt(max);
  }

  public static Integer getInt(Integer min, Integer max) {
    WizRand.iniciaRand();
    return min + WizRand.rand.nextInt(max - min);
  }

  public static char getChar() {
    return WizChar.SIMPLE[WizRand.getInt(0, WizChar.SIMPLE.length)];
  }

  public static String getChars(int size) {
    var result = "";
    if (size > 0) {
      while (result.length() < size) {
        result = result + WizRand.getChar();
      }
    }
    return result;
  }

  public static Float getFloat() {
    WizRand.iniciaRand();
    return WizRand.rand.nextFloat();
  }

  public static Double getDouble() {
    WizRand.iniciaRand();
    return WizRand.rand.nextDouble();
  }

  public static Double getGaussian() {
    WizRand.iniciaRand();
    return WizRand.rand.nextGaussian();
  }

  public static <T> T getItem(T[] fromList) {
    if (fromList != null) {
      if (fromList.length > 0) {
        return fromList[WizRand.getInt(fromList.length)];
      }
    }
    return null;
  }
}
