package br.net.pin.qin_server.work;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import br.net.pin.qin_sunjar.mage.WizArray;

public class Utils {
  public static String newRandomToken() {
    return java.util.UUID.randomUUID().toString();
  }

  public static String listFolders(File onDir) {
    var result = new StringBuilder();
    for (var file : onDir.listFiles()) {
      if (file.isDirectory()) {
        result.append(file.getName());
        result.append("\n");
      }
    }
    return result.toString();
  }

  public static File resolveFile(String path, String parentIfRelative) {
    var parent = Paths.get(parentIfRelative);
    var child = Paths.get(path);
    var result = parent.resolve(child);
    return result.toFile();
  }

  public static void close(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException ignore) {
      }
    }
  }

  private final static String[] TEXT_EXTENSIONS = new String[] { "txt", "htm", "html",
      "css", "log" };
  private final static String[] IMAGE_EXTENSIONS = new String[] { "jpg", "jpeg", "gif",
      "png", "ico", "bmp", "svg" };
  private final static String[] AUDIO_EXTENSIONS = new String[] { "mp3", "ogg", "wav",
      "midi", "mid" };
  private final static String[] VIDEO_EXTENSIONS = new String[] { "mp4", "ogv", "avi",
      "mpg", "webm", "flv", "mov" };

  public static String getMimeType(String fileName) {
    var dot = fileName.lastIndexOf(".");
    if (dot == -1) {
      return "application/octet-stream";
    }
    var extension = fileName.substring(dot + 1);
    if (extension.equals("js")) {
      return "text/javascript";
    }
    if (WizArray.has(extension, Utils.TEXT_EXTENSIONS)) {
      return "text/" + extension;
    }
    if (WizArray.has(extension, Utils.IMAGE_EXTENSIONS)) {
      return "image/" + extension;
    }

    if (WizArray.has(extension, Utils.AUDIO_EXTENSIONS)) {
      return "audio/" + extension;
    }

    if (WizArray.has(extension, Utils.VIDEO_EXTENSIONS)) {
      return "video/" + extension;
    }
    return "application/" + extension;
  }
}
