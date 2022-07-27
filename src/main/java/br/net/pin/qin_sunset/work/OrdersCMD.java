package br.net.pin.qin_sunset.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import br.net.pin.qin_sunset.data.Authed;
import br.net.pin.qin_sunset.data.Issued;
import br.net.pin.qin_sunset.data.Way;
import br.net.pin.qin_sunset.swap.Execute;

public class OrdersCMD {
  public static Issued run(Execute execution) throws Exception {
    var issued = new Issued();
    var builder = new ProcessBuilder();
    if (execution.exec.toLowerCase().endsWith(".jar")) {
      builder.command().add("java");
      builder.command().add("-jar");
    }
    builder.command().add(execution.exec);
    if (execution.args != null) {
      builder.command().addAll(execution.args);
    }
    builder.redirectErrorStream(true);
    var process = builder.start();
    if (execution.inputs != null) {
      new Thread() {
        @Override
        public void run() {
          try {
            var writer = new BufferedWriter(new OutputStreamWriter(process
                .getOutputStream()));
            for (var input : execution.inputs) {
              writer.write(input);
              writer.newLine();
              writer.flush();
            }
          } catch (Exception e) {
            issued.addLine("Exception on put Input: " + e.getMessage());
          }
        };
      }.start();
    }
    new Thread() {
      public void run() {
        var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
          String line;
          while ((line = reader.readLine()) != null) {
            issued.addLine(line);
          }
        } catch (Exception e) {
          issued.addLine("Exception on get Output: " + e.getMessage());
        }
        try {
          var exitCode = process.waitFor();
          issued.setResultCoded(exitCode);
        } catch (Exception e) {
          issued.addLine("Exception on get Code: " + e.getMessage());
        } finally {
          issued.setDone();
        }
      };
    }.start();
    return issued;
  }

  public static String list(Way way, Authed forAuthed) {
    var cmdsDir = new File(way.air.setup.serverFolder, "cmd");
    if (forAuthed.isMaster()) {
      return Utils.listFolders(cmdsDir);
    }
    var result = new StringBuilder();
    for (var access : forAuthed.getAccess()) {
      if (access.cmd != null) {
        if (new File(cmdsDir, access.cmd.name).exists()) {
          result.append(access.cmd.name);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }
}
