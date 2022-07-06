package br.net.pin.qin_sunwiz.mage;

import java.io.File;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WizFile {

  public static String clean(String path) {
    return WizFile.getAbsolute(WizFile.fixSeparators(path));
  }

  public static String getAbsolute(String path) {
    if (path == null || path.isEmpty()) {
      return path;
    }
    final var samePrefix = "." + File.separator;
    final var upperPrefix = ".." + File.separator;
    final var homePrefix = "~" + File.separator;
    if (path.startsWith(samePrefix) || path.startsWith(upperPrefix)) {
      var workingDir = new File(System.getProperty("user.dir"));
      while (path.startsWith(samePrefix) || path.startsWith(upperPrefix)) {
        if (path.startsWith(samePrefix)) {
          path = path.substring(samePrefix.length());
        } else {
          workingDir = workingDir.getParentFile();
          path = path.substring(upperPrefix.length());
        }
      }
      return WizFile.sum(workingDir.getAbsolutePath(), path);
    }
    if (path.startsWith(homePrefix)) {
      var homeDir = new File(System.getProperty("user.home"));
      return WizFile.sum(homeDir.getAbsolutePath(), path);
    }
    return path;
  }

  public static String fixSeparators(String path) {
    if (WizChars.isEmpty(path)) {
      return path;
    }
    if (path.contains("\\") && "/".equals(File.separator)) {
      path = path.replaceAll("\\", "/");
    } else if (path.contains("/") && "\\".equals(File.separator)) {
      path = path.replaceAll("/", "\\");
    }
    return path;
  }

  public static String sum(String path, String child) {
    if (!WizChars.isNotEmpty(path) || !WizChars.isNotEmpty(child)) {
      return WizChars.firstNonEmpty(path, child);
    }
    if (path.endsWith(File.separator) && child.startsWith(File.separator)) {
      return path + child.substring(File.separator.length());
    } else if (path.endsWith(File.separator) || child.startsWith(File.separator)) {
      return path + child;
    } else {
      return path + File.separator + child;
    }
  }

  public static String sum(String path, String... children) {
    var result = path;
    if (children != null) {
      for (String filho : children) {
        result = WizFile.sum(result, filho);
      }
    }
    return result;
  }

  public static File sum(File path, String... children) {
    var result = path;
    if (result != null && children != null) {
      for (String child : children) {
        result = new File(result, child);
      }
    }
    return result;
  }

  public static File getParent(File path, String withName) {
    File result = null;
    if (path != null) {
      var actual = path.getParentFile();
      while (!withName.equals(actual.getName())) {
        actual = actual.getParentFile();
        if (actual == null) {
          break;
        }
      }
      result = actual;
    }
    return result;
  }

  public static String getParent(String path) {
    if (path.contains(File.separator)) {
      return path.substring(0, path.lastIndexOf(File.separator));
    }
    return path;
  }

  public static File getRoot(String withName, File fromPath) {
    if (fromPath == null) {
      return null;
    }
    var result = fromPath.getParentFile();
    while (result != null && !Objects.equals(withName, result.getName())) {
      result = result.getParentFile();
    }
    return result;
  }

  public static String getName(String path) {
    if (path == null) {
      return null;
    }
    final var sep = path.lastIndexOf(File.separator);
    if (sep == -1) {
      return path;
    }
    return path.substring(sep + 1);
  }

  public static String getBaseName(String path) {
    if (path == null) {
      return null;
    }
    path = WizFile.getName(path);
    final var dot = path.lastIndexOf(".");
    if (dot > -1) {
      return path.substring(0, dot);
    }
    return path;
  }

  public static String getExtension(String path) {
    if (path == null) {
      return null;
    }
    final var dot = path.lastIndexOf(".");
    if (dot > -1) {
      return path.substring(dot);
    }
    return "";
  }

  public static String addOnBaseName(String path, String chars) {
    if (path == null) {
      return chars;
    }
    if (WizChars.isEmpty(chars)) {
      return path;
    }
    var dotIndex = path.lastIndexOf(".");
    if (dotIndex > -1) {
      return path.substring(0, dotIndex) + chars + path.substring(dotIndex);
    }
    return path + chars;
  }

  public static File addOnBaseName(File file, String chars) {
    return new File(WizFile.addOnBaseName(file.getAbsolutePath(), chars));
  }

  public static File notOverride(File path) {
    if ((path == null) || !path.exists()) {
      return path;
    }
    File result = null;
    var attempt = 2;
    do {
      result = new File(WizFile.addOnBaseName(path.getAbsolutePath(), " (" + attempt
          + ")"));
      attempt++;
    } while (result.exists());
    return result;
  }

  public static JFileChooser chooser(String description, String... extensions) {
    var chooser = new JFileChooser();
    chooser.setDialogTitle("Select");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setAcceptAllFileFilterUsed(extensions == null);
    FileFilter filter = new FileNameExtensionFilter(description, extensions);
    chooser.setFileFilter(filter);
    return chooser;
  }

  public static File open() {
    return WizFile.open(null);
  }

  public static File open(File selected) {
    return WizFile.open(selected, null);
  }

  public static File open(String description, String... extensions) {
    return WizFile.open(null, description, extensions);
  }

  public static File open(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select File or Directory");
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.OPEN,
          FileTerminalNature.BOTH,
          selected, description, extensions);
    }
    return result;
  }

  public static File openFile() {
    return WizFile.openFile(null);
  }

  public static File openFile(File selected) {
    return WizFile.openFile(selected, null);
  }

  public static File openFile(String description, String... extensions) {
    return WizFile.openFile(null, description, extensions);
  }

  public static File openFile(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select File");
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.OPEN,
          FileTerminalNature.FILE,
          selected, description, extensions);
    }
    return result;
  }

  public static File openDir() {
    return WizFile.openDir(null);
  }

  public static File openDir(File selected) {
    return WizFile.openDir(selected, null);
  }

  public static File openDir(String description, String... extensions) {
    return WizFile.openDir(null, description, extensions);
  }

  public static File openDir(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select Directory");
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.OPEN,
          FileTerminalNature.DIRECTORY,
          selected, description, extensions);
    }
    return result;
  }

  public static File[] openMany() {
    return WizFile.openMany(null);
  }

  public static File[] openMany(File[] selected) {
    return WizFile.openMany(selected, null);
  }

  public static File[] openMany(String description, String... extensions) {
    return WizFile.openMany(null, description, extensions);
  }

  public static File[] openMany(File[] selected, String description,
      String... extensions) {
    File[] result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select Many Files or Directories");
      chooser.setMultiSelectionEnabled(true);
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFiles(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFiles();
      }
    } else {
      result = WizFile.selectFileTerminalMany(true, FileTerminalAction.OPEN,
          FileTerminalNature.BOTH, selected, description, extensions);
    }
    return result;
  }

  public static File[] openFileMany() {
    return WizFile.openFileMany(null);
  }

  public static File[] openFileMany(File[] selected) {
    return WizFile.openFileMany(selected, null);
  }

  public static File[] openFileMany(String description, String... extensions) {
    return WizFile.openFileMany(null, description, extensions);
  }

  public static File[] openFileMany(File[] selected, String description,
      String... extensions) {
    File[] result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select Many Files");
      chooser.setMultiSelectionEnabled(true);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFiles(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFiles();
      }
    } else {
      result = WizFile.selectFileTerminalMany(true, FileTerminalAction.OPEN,
          FileTerminalNature.FILE, selected, description, extensions);
    }
    return result;
  }

  public static File[] openDirMany() {
    return WizFile.openDirMany(null);
  }

  public static File[] openDirMany(File[] selected) {
    return WizFile.openDirMany(selected, null);
  }

  public static File[] openDirMany(String description, String... extensions) {
    return WizFile.openDirMany(null, description, extensions);
  }

  public static File[] openDirMany(File[] selected, String description,
      String... extensions) {
    File[] result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Select Many Directories");
      chooser.setMultiSelectionEnabled(true);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFiles(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFiles();
      }
    } else {
      result = WizFile.selectFileTerminalMany(true, FileTerminalAction.OPEN,
          FileTerminalNature.DIRECTORY, selected, description, extensions);
    }
    return result;
  }

  public static File save() {
    return WizFile.save(null);
  }

  public static File save(File selected) {
    return WizFile.save(selected, null);
  }

  public static File save(String description, String... extensions) {
    return WizFile.save(null, description, extensions);
  }

  public static File save(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Save File or Directory");
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
        var ext = WizFile.getExtension(result.getAbsolutePath());
        if (extensions != null) {
          if (!WizArray.has(ext, extensions)) {
            result = new File(result.getAbsolutePath() + "." + extensions[0]);
          }
        }
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.SAVE,
          FileTerminalNature.BOTH,
          selected, description, extensions);
    }
    return result;
  }

  public static File saveFile() {
    return WizFile.saveFile(null);
  }

  public static File saveFile(File selected) {
    return WizFile.saveFile(selected, null);
  }

  public static File saveFile(String description, String... extensions) {
    return WizFile.saveFile(null, description, extensions);
  }

  public static File saveFile(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Save File");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
        if (extensions != null) {
          if (extensions.length > 0) {
            var mustInclude = true;
            if (result.getName().contains(".")) {
              var ext = WizFile.getExtension(result.getName());
              if (WizArray.has(ext, extensions)) {
                mustInclude = false;
              }
            }
            if (mustInclude) {
              result = new File(result.getAbsolutePath() + "." + extensions[0]);
            }
          }
        }
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.SAVE,
          FileTerminalNature.FILE,
          selected, description, extensions);
    }
    return result;
  }

  public static File saveDir() {
    return WizFile.saveDir(null);
  }

  public static File saveDir(File selected) {
    return WizFile.saveDir(selected, null);
  }

  public static File saveDir(String description, String... extensions) {
    return WizFile.saveDir(null, description, extensions);
  }

  public static File saveDir(File selected, String description, String... extensions) {
    File result = null;
    if (WizDesk.isStarted()) {
      var chooser = new JFileChooser();
      chooser.setDialogTitle("Save Directory");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(extensions == null);
      if (selected != null) {
        chooser.setSelectedFile(selected);
      }
      if (description != null & extensions != null) {
        FileFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
      }
      if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        result = chooser.getSelectedFile();
        var ext = WizFile.getExtension(result.getAbsolutePath());
        if (extensions != null) {
          if (!WizArray.has(ext, extensions)) {
            result = new File(result.getAbsolutePath() + "." + extensions[0]);
          }
        }
      }
    } else {
      result = WizFile.selectFileTerminal(FileTerminalAction.SAVE,
          FileTerminalNature.DIRECTORY,
          selected, description, extensions);
    }
    return result;
  }

  private static enum FileTerminalAction {
    OPEN, SAVE
  }

  private static enum FileTerminalNature {
    BOTH, DIRECTORY, FILE,
  }

  private static File selectFileTerminal(FileTerminalAction action,
      FileTerminalNature nature, File selected, String description,
      String... extensions) {
    return WizFile.selectFileTerminalMany(false, action, nature, new File[] { selected },
        description, extensions)[0];
  }

  private static File[] selectFileTerminalMany(boolean askForMany,
      FileTerminalAction action, FileTerminalNature nature, File[] selected,
      String description, String... extensions) {
    throw new UnsupportedOperationException("Select file(s) from terminal is not yet implemented.");
  }

}
