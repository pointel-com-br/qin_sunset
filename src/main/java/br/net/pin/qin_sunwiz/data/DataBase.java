package br.net.pin.qin_sunwiz.data;

public enum DataBase {

  SQLiteMemory("org.sqlite.JDBC", "jdbc:sqlite::memory:", null, new HelperSQLite()),

  SQLiteLocal("org.sqlite.JDBC", "jdbc:sqlite:$path", null, new HelperSQLite()),

  HSQLDBMemory("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:$data", 9000,
      new HelperHSQLDB()),

  HSQLDBLocal("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:$path;hsqldb.lock_file=true",
      9000, new HelperHSQLDB()),

  HSQLDBClient("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://$path:$port/$data", 9000,
      new HelperHSQLDB()),

  DerbyInner("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:$path;create=true", 1527,
      new HelperDerby()),

  DerbyClient("org.apache.derby.jdbc.ClientDriver",
      "jdbc:derby://$path:$port/$data;create=true", 1527, new HelperDerby()),

  FirebirdLocal("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:local:$path", 3050,
      new HelperFirebird()),

  FirebirdInner("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:embedded:$path", 3050,
      new HelperFirebird()),

  FirebirdClient("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:$path:$port/$data",
      3050, new HelperFirebird()),

  MySQLClient("com.mysql.jdbc.Driver", "jdbc:mysql://$path:$port/$data", 3306,
      new HelperMySQL()),

  PostgreClient("org.postgresql.Driver", "jdbc:postgresql://$path:$port/$data", 5432,
      new HelperPostgre());

  public final String clazz;
  public final String formation;
  public final Integer defaultPort;
  public final Helper helper;

  private DataBase(String clazz, String formation, Integer defaultPort, Helper auxiliar) {
    this.clazz = clazz;
    this.formation = formation;
    this.defaultPort = defaultPort;
    this.helper = auxiliar;
  }

  public String getUrlIdenty() {
    var dollarAt = this.formation.indexOf("$");
    if (dollarAt == -1) {
      return this.formation;
    }
    return this.formation.substring(0, dollarAt);
  }

  public static DataBase fromURL(String jdbc) {
    for (DataBase data : DataBase.values()) {
      if (jdbc.startsWith(data.getUrlIdenty())) {
        return data;
      }
    }
    return null;
  }

  public static Helper getHelperFromURL(String jdbc) {
    for (DataBase data : DataBase.values()) {
      if (jdbc.startsWith(data.getUrlIdenty())) {
        return data.helper;
      }
    }
    return Helper.instance;
  }

  public static DataBase fromString(String string) {
    if (string == null || string.isBlank()) {
      return null;
    }
    for (DataBase dataBase : DataBase.values()) {
      if (dataBase.name().equalsIgnoreCase(string)) {
        return dataBase;
      }
    }
    return null;
  }
}
