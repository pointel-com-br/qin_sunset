package br.net.pin.qin_sunwiz.mage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WizDate {

    public static Date get(Object fromValue) throws Exception {
        if (fromValue == null) {
            return null;
        }
        var clazz = fromValue.getClass();
        if (Date.class.isAssignableFrom(clazz)) {
            return (Date) fromValue;
        }
        if (fromValue instanceof java.sql.Date) {
            return new Date(((java.sql.Date) fromValue).getTime());
        } else if (fromValue instanceof java.sql.Time) {
            return new Date(((java.sql.Time) fromValue).getTime());
        } else if (fromValue instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) fromValue).getTime());
        } else if (fromValue instanceof String string) {
            for (SimpleDateFormat format : WizDate.FORMATS) {
                if (WizDate.is(string, format)) {
                    return format.parse(string);
                }
            }
        }
        throw new Exception("Could not convert the value of class " + fromValue.getClass()
                        .getName() + " to a date value.");
    }

    public static boolean is(String str, SimpleDateFormat inFormat) {
        return Objects.equals(WizChars.replaceLettersOrDigits(str, 'x'), WizChars
                        .replaceLettersOrDigits(inFormat.toPattern(), 'x'));
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.DATE_FORMAT.format(date);
    }

    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIME_FORMAT.format(date);
    }

    public static String formatTimeMillis(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIME_MILLIS_FORMAT.format(date);
    }

    public static String formatTimestamp(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIMESTAMP_FORMAT.format(date);
    }

    public static String formatMoment(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.MOMENT_FORMAT.format(date);
    }

    public static String formatTimeFile(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIME_FILE_FORMAT.format(date);
    }

    public static String formatTimeMillisFile(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIME_MILLIS_FILE_FORMAT.format(date);
    }

    public static String formatTimestampFile(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.TIMESTAMP_FILE_FORMAT.format(date);
    }

    public static String formatMomentFile(Date date) {
        if (date == null) {
            return "";
        }
        return WizDate.MOMENT_FILE_FORMAT.format(date);
    }

    public static Date parseDate(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.DATE_FORMAT.parse(formatted);
    }

    public static Date parseTime(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIME_FORMAT.parse(formatted);
    }

    public static Date parseTimeMillis(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIME_MILLIS_FORMAT.parse(formatted);
    }

    public static Date parseTimestamp(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIMESTAMP_FORMAT.parse(formatted);
    }

    public static Date parseMoment(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.MOMENT_FORMAT.parse(formatted);
    }

    public static Date parseTimeFile(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIME_FILE_FORMAT.parse(formatted);
    }

    public static Date parseTimeMillisFile(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIME_MILLIS_FILE_FORMAT.parse(formatted);
    }

    public static Date parseTimestampFile(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.TIMESTAMP_FILE_FORMAT.parse(formatted);
    }

    public static Date parseMomentFile(String formatted) throws Exception {
        if (formatted == null || formatted.isEmpty()) {
            return null;
        }
        return WizDate.MOMENT_FILE_FORMAT.parse(formatted);
    }

    public static final SimpleDateFormat DATE_USER_FORMAT = new SimpleDateFormat(
                    "dd/MM/yyyy");
    public static final SimpleDateFormat TIME_USER_FORMAT = new SimpleDateFormat(
                    "HH:mm:ss");
    public static final SimpleDateFormat TIMESTAMP_USER_FORMAT = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat TIME_MILLIS_FORMAT = new SimpleDateFormat(
                    "HH:mm:ss.ZZZ");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat MOMENT_FORMAT = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.ZZZ");

    public static final SimpleDateFormat TIME_FILE_FORMAT = new SimpleDateFormat(
                    "HH-mm-ss");
    public static final SimpleDateFormat TIME_MILLIS_FILE_FORMAT = new SimpleDateFormat(
                    "HH-mm-ss.ZZZ");
    public static final SimpleDateFormat TIMESTAMP_FILE_FORMAT = new SimpleDateFormat(
                    "yyyy-MM-dd-HH-mm-ss");
    public static final SimpleDateFormat MOMENT_FILE_FORMAT = new SimpleDateFormat(
                    "yyyy-MM-dd-HH-mm-ss.ZZZ");

    public static final SimpleDateFormat[] FORMATS = {
                    WizDate.DATE_USER_FORMAT, WizDate.TIME_USER_FORMAT,
                    WizDate.TIMESTAMP_USER_FORMAT,
                    WizDate.DATE_FORMAT, WizDate.TIME_FORMAT, WizDate.TIME_MILLIS_FORMAT,
                    WizDate.TIMESTAMP_FORMAT,
                    WizDate.MOMENT_FORMAT, WizDate.TIME_FILE_FORMAT,
                    WizDate.TIME_MILLIS_FILE_FORMAT, WizDate.MOMENT_FILE_FORMAT
    };
}
