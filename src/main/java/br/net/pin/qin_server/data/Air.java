package br.net.pin.qin_server.data;

public class Air {
  public final Setup setup;
  public final Users users;
  public final Groups groups;
  public final Bases bases;

  public Air(Setup setup, Users users, Groups groups, Bases bases) throws Exception {
    this.setup = setup;
    this.users = users;
    this.groups = groups;
    this.bases = bases;
  }
}