package br.net.pin.qin_sunset.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Issued;
import br.net.pin.qin_sunset.core.Way;
import br.net.pin.qin_sunset.swap.Execute;

public class OrdersCMD {
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

  public static Issued run(Execute execution) throws Exception {
    var issued = new Issued();
    var builder = new ProcessBuilder();
    builder.command().add(execution.exec);
    if (execution.args != null) {
      builder.command().addAll(execution.args);
    }
    builder.redirectErrorStream(false);
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
            issued.addErrLine("Exception on put Input: " + e.getMessage());
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
            issued.addOutLine(line);
          }
        } catch (Exception e) {
          issued.addErrLine("Exception on get Output: " + e.getMessage());
        }
      };
    }.start();
    new Thread() {
      public void run() {
        var reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        try {
          String line;
          while ((line = reader.readLine()) != null) {
            issued.addErrLine(line);
          }
        } catch (Exception e) {
          issued.addErrLine("Exception on get Error: " + e.getMessage());
        }
      };
    }.start();
    new Thread() {
      public void run() {
        try {
          var resultCode = process.waitFor();
          issued.setResultCode(resultCode);
        } catch (Exception e) {
          issued.addErrLine("Exception on get Result Code: " + e.getMessage());
        } finally {
          issued.setDone();
        }
      };
    }.start();
    return issued;
  }
}
