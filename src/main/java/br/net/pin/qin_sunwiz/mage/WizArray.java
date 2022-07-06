package br.net.pin.qin_sunwiz.mage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WizArray {

  @SuppressWarnings("all")
  public static <T> Boolean has(T value, T... onArray) {
    if (onArray != null) {
      for (Object daMatriz : onArray) {
        if (Objects.equals(value, daMatriz)) {
          return true;
        }
      }
    }
    return false;
  }

  public static Boolean has(char value, char... onArray) {
    if (onArray != null) {
      for (char daMatriz : onArray) {
        if (daMatriz == value) {
          return true;
        }
      }
    }
    return false;
  }

  public static Boolean has(int value, int... onArray) {
    if (onArray != null) {
      for (int daMatriz : onArray) {
        if (daMatriz == value) {
          return true;
        }
      }
    }
    return false;
  }

  @SuppressWarnings("all")
  public static <T> T[] insert(int index, T value, T... onArray) {
    if (onArray == null) {
      return null;
    }
    var result = Arrays.copyOf(onArray, Math.max(onArray.length + 1, index + 1));
    result[index] = value;
    for (var i = index; i < onArray.length; i++) {
      result[i + 1] = onArray[i];
    }
    return result;
  }

  @SuppressWarnings("all")
  public static <T> T[] getNotNull(T... elements) {
    if (elements == null) {
      return null;
    }
    List<T> list = new ArrayList<>();
    for (T element : elements) {
      if (element != null) {
        list.add(element);
      }
    }
    var result = Arrays.copyOf(elements, list.size());
    for (var i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }
    return result;
  }

  @SuppressWarnings("all")
  public static <T> T[] make(Class<T> clazz, T value, int size) {
    var result = (T[]) Array.newInstance(clazz, size);
    Arrays.fill(result, value);
    return result;
  }

}
