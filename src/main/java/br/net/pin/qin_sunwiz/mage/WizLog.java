package br.net.pin.qin_sunwiz.mage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.commons.io.FilenameUtils;

public class WizLog {
    private static Boolean VERBOSE = false;
    private static PrintWriter ARCHIVE = null;
    private static String[] WATCHED = null;
    private static String[] IGNORED = {"java.", "javax.", "jakarta."};

    public static void setVerbose(Boolean verbose) {
        WizLog.VERBOSE = verbose;
    }

    public static void setArchive(String archive) throws FileNotFoundException {
        if (WizLog.ARCHIVE != null) {
            WizLog.ARCHIVE.close();
        }
        if (archive == null) {
            WizLog.ARCHIVE = null;
        } else {
            WizLog.ARCHIVE = new PrintWriter(archive);
        }
    }

    public static void setWatched(String[] watched) {
        WizLog.WATCHED = watched;
    }

    public static void info(String message) {
        message = WizLog.make("INFO", message, null);
        if (WizLog.VERBOSE) {
            System.out.print(message);
            System.out.flush();
        }
        if (WizLog.ARCHIVE != null) {
            WizLog.ARCHIVE.print(message);
            WizLog.ARCHIVE.flush();
        }
    }

    public static void warn(String message) {
        message = WizLog.make("WARN", message, WizLog.class);
        if (WizLog.VERBOSE) {
            System.out.print(message);
            System.out.flush();
        }
        if (WizLog.ARCHIVE != null) {
            WizLog.ARCHIVE.print(message);
            WizLog.ARCHIVE.flush();
        }
    }

    public static void erro(Throwable error) {
        var message = WizLog.make("ERRO", error.getMessage(), error);
        if (WizLog.VERBOSE) {
            System.out.print(message);
            System.out.flush();
        }
        if (WizLog.ARCHIVE != null) {
            WizLog.ARCHIVE.print(message);
            WizLog.ARCHIVE.flush();
        }
    }

    private static String make(String kind, String message, Object traceOf) {
        var result = new StringBuilder();
        result.append("[");
        result.append(kind);
        result.append("] ");
        result.append(message);
        if (traceOf != null) {
            if (traceOf instanceof Throwable error) {
                result.append(WizLog.getOrigin(error));
            } else {
                result.append(WizLog.getOrigin());
            }
        }
        result.append("\n");
        return result.toString();
    }

    private static boolean isWatched(StackTraceElement element) {
        if ("br.net.pin.jabx.mage.WizLog".equals(element.getClassName())) {
            return false;
        }
        if (WizLog.WATCHED == null) {
            for (var ignored : WizLog.IGNORED) {
                if (element.getClassName().startsWith(ignored)) {
                    return false;
                }
            }
            return true;
        }
        for (var watched : WizLog.WATCHED) {
            if (element.getClassName().startsWith(watched)) {
                return true;
            }
        }
        return false;
    }

    private static String getOrigin() {
        var builder = new StringBuilder();
        builder.append(" {");
        StackTraceElement elements[] = Thread.currentThread().getStackTrace();
        for (var i = elements.length - 1; i >= 0; i--) {
            var element = elements[i];
            if (WizLog.isWatched(element)) {
                builder.append(" |> ");
                builder.append(FilenameUtils.getBaseName(element.getFileName()));
                builder.append("[");
                builder.append(element.getLineNumber());
                builder.append("](");
                builder.append(element.getMethodName());
                builder.append(")");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    private static String getOrigin(Throwable ofError) {
        var builder = new StringBuilder();
        builder.append(" {");
        StackTraceElement elements[] = ofError.getStackTrace();
        for (var i = elements.length - 1; i >= 0; i--) {
            var element = elements[i];
            if (WizLog.isWatched(element)) {
                builder.append(" |> ");
                builder.append(FilenameUtils.getBaseName(element.getFileName()));
                builder.append("[");
                builder.append(element.getLineNumber());
                builder.append("](");
                builder.append(element.getMethodName());
                builder.append(")");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
