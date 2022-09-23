package br.net.pin.qin_sunwiz.mage;

public class Base36 {
  public static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final int BASE = ALPHABET.length();

  private Base36() {
  }

  public static String fromBase10(Integer i) {
    StringBuilder sb = new StringBuilder("");
    if (i == 0) {
      return "a";
    }
    while (i > 0) {
      i = fromBase10(i, sb);
    }
    return sb.reverse().toString();
  }

  public static String fromBase10(Long i) {
    StringBuilder sb = new StringBuilder("");
    if (i == 0) {
      return "a";
    }
    while (i > 0) {
      i = fromBase10(i, sb);
    }
    return sb.reverse().toString();
  }

  private static Integer fromBase10(Integer i, final StringBuilder sb) {
    int rem = i % BASE;
    sb.append(ALPHABET.charAt(rem));
    return i / BASE;
  }

  private static Long fromBase10(Long i, final StringBuilder sb) {
    Long rem = i % BASE;
    sb.append(ALPHABET.charAt(rem.intValue()));
    return i / BASE;
  }

  public static Integer toBase10(String str) {
    return toBase10(new StringBuilder(str).reverse().toString().toCharArray());
  }

  private static Integer toBase10(char[] chars) {
    Integer n = 0;
    for (Integer i = chars.length - 1; i >= 0; i--) {
      n += toBase10(ALPHABET.indexOf(chars[i]), i);
    }
    return n;
  }

  private static Integer toBase10(Integer n, Integer pow) {
    return n * ((Double) Math.pow(BASE, pow)).intValue();
  }

  public static Long toBase10Lon(String str) {
    return toBase10Lon(new StringBuilder(str).reverse().toString().toCharArray());
  }

  private static Long toBase10Lon(char[] chars) {
    Long n = 0l;
    for (int i = chars.length - 1; i >= 0; i--) {
      n += toBase10Lon(ALPHABET.indexOf(chars[i]), i);
    }
    return n;
  }

  private static Long toBase10Lon(Integer n, Integer pow) {
    return n * ((Double) Math.pow(BASE, pow)).longValue();
  }
}
