package br.net.pin.qin_sunset;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import br.net.pin.qin_sunset.data.Air;
import br.net.pin.qin_sunset.data.Bases;
import br.net.pin.qin_sunset.data.Groups;
import br.net.pin.qin_sunset.data.Setup;
import br.net.pin.qin_sunset.data.Users;
import br.net.pin.qin_sunset.data.Way;

public class QinSunset {

  public static void main(String[] args) throws Exception {
    var options = cmdOptions();
    var command = new DefaultParser().parse(options, args);
    if (command.hasOption('?')) {
      System.out.println(
          "QinSunset is a command program that servers public files, graphical user interfaces, file system access with authorization, command programs dispatchers and monitoring, databases queries and scripts execution. It is the base of the Pointel platform and the backend of the Qinpel, the Quick Interface for Power Intelligence.");
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("qin_sunset", options);
      return;
    }
    Setup setup;
    var setupFile = new File("setup.json");
    if (setupFile.exists()) {
      setup = Setup.fromString(Files.readString(setupFile.toPath()));
    } else {
      setup = new Setup();
    }
    setFromCmd(command, setup);
    Users users;
    var usersFile = new File("users.json");
    if (usersFile.exists()) {
      users = Users.fromString(Files.readString(usersFile.toPath()));
    } else {
      users = new Users();
    }
    Groups groups;
    var groupsFile = new File("groups.json");
    if (groupsFile.exists()) {
      groups = Groups.fromString(Files.readString(groupsFile.toPath()));
    } else {
      groups = new Groups();
    }
    Bases bases;
    var basesFile = new File("bases.json");
    if (basesFile.exists()) {
      bases = Bases.fromString(Files.readString(basesFile.toPath()));
    } else {
      bases = new Bases();
    }
    setup.fixDefaults();
    users.fixDefaults();
    bases.fixDefaults();
    new Service(new Way(new Air(setup, users, groups, bases))).start();
  }

  public static Options cmdOptions() {
    var result = new Options();
    result.addOption(Option.builder("?").longOpt("help")
        .desc("Print usage information.").build());
    result.addOption(Option.builder("v").longOpt("verbose")
        .desc("Should we print verbose messages?").build());
    result.addOption(Option.builder("k").longOpt("archive")
        .desc("Should we archive all the messages?").build());
    result.addOption(Option.builder("n").longOpt("name").hasArg()
        .desc("On behalf of what name should we serve?").build());
    result.addOption(Option.builder("l").longOpt("lang").hasArg()
        .desc("On what language should we serve?").build());
    result.addOption(Option.builder("h").longOpt("host").hasArg()
        .desc("On what host should we serve?").build());
    result.addOption(Option.builder("p").longOpt("port").hasArg()
        .desc("On what port should we serve?").build());
    result.addOption(Option.builder("f").longOpt("folder").hasArg()
        .desc("On what folder should we serve?").build());
    result.addOption(Option.builder("u").longOpt("serves-pub")
        .desc("Should we serve public files?").build());
    result.addOption(Option.builder("a").longOpt("serves-app")
        .desc("Should we serve applications?").build());
    result.addOption(Option.builder("d").longOpt("serves-dir")
        .desc("Should we serve directories?").build());
    result.addOption(Option.builder("c").longOpt("serves-cmd")
        .desc("Should we serve commands?").build());
    result.addOption(Option.builder("b").longOpt("serves-bas")
        .desc("Should we serve databases storage?").build());
    result.addOption(Option.builder("r").longOpt("serves-reg")
        .desc("Should we serve register actions?").build());
    result.addOption(Option.builder("s").longOpt("serves-sql")
        .desc("Should we serve SQL executions?").build());
    result.addOption(Option.builder("g").longOpt("serves-giz")
        .desc("Should we serve GIZ executions?").build());
    return result;
  }

  public static void setFromCmd(CommandLine command, Setup setup) {
    if (command.hasOption('v')) {
      setup.serverVerbose = true;
    }
    if (command.hasOption('k')) {
      setup.serverArchive = true;
    }
    if (command.hasOption('n')) {
      setup.serverName = command.getOptionValue('n');
    }
    if (command.hasOption('l')) {
      setup.serverLang = command.getOptionValue('l');
    }
    if (command.hasOption('h')) {
      setup.serverHost = command.getOptionValue('h');
    }
    if (command.hasOption('p')) {
      setup.serverPort = Integer.parseInt(command.getOptionValue('p'));
    }
    if (command.hasOption('f')) {
      setup.serverFolder = command.getOptionValue('f');
    }
    if (command.hasOption('u')) {
      setup.servesPUB = true;
    }
    if (command.hasOption('a')) {
      setup.servesAPP = true;
    }
    if (command.hasOption('d')) {
      setup.servesDIR = true;
    }
    if (command.hasOption('c')) {
      setup.servesCMD = true;
    }
    if (command.hasOption('b')) {
      setup.servesBAS = true;
    }
    if (command.hasOption('r')) {
      setup.servesREG = true;
    }
    if (command.hasOption('s')) {
      setup.servesSQL = true;
    }
    if (command.hasOption('g')) {
      setup.servesGIZ = true;
    }
  }

}
