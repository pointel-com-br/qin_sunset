package br.net.pin.qin_sunset.work;

import java.io.File;
import java.io.StringWriter;

import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Issued;
import br.net.pin.qin_sunset.core.Way;
import br.net.pin.qin_sunset.swap.Execute;

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

  public static Issued run(Authed forAuthed, Execute execution) throws Exception {
    var gizMap = forAuthed.getGizMap();
    var script = gizMap.getScript(execution.exec);
    var issued = new Issued();
    new Thread() {
      @Override
      public void run() {
        synchronized (script) {
          try {
            var binding = script.getBinding();
            binding.setVariable("args", execution.args);
            var out = new StringWriter();
            binding.setProperty("out", out);
            var result = script.run();
            issued.addLine(out.toString());
            if (result instanceof Integer exitCode) {
              issued.setResultCoded(exitCode);
            } else {
              issued.setResultCoded(0);
            }
          } catch (Exception e) {
            issued.addLine(e.getMessage());
            issued.setResultCoded(-1);
          } finally {
            issued.setDone();
          }
        }
      };
    }.start();
    return issued;
  }
}
