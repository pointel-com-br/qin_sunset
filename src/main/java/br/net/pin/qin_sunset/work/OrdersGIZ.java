package br.net.pin.qin_sunset.work;

import java.io.File;

import br.net.pin.qin_sunset.data.Authed;
import br.net.pin.qin_sunset.data.Way;

public class OrdersGIZ {
  public static String list(Way way, Authed forAuthed) {
    var gizDir = new File(way.air.setup.serverFolder, "giz");
    if (forAuthed.isMaster()) {
      return Utils.listFilesWithExtension(gizDir, ".giz");
    }
    var result = new StringBuilder();
    for (var access : forAuthed.getAccess()) {
      if (access.giz != null) {
        if (new File(gizDir, access.giz.path).exists()) {
          result.append(access.giz.path);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }
}
