package br.net.pin.qin_server.data;

import java.util.concurrent.ConcurrentHashMap;

import br.net.pin.qin_server.work.Utils;

public class IssuedMap extends ConcurrentHashMap<String, Issued> {
  public String newIssued(Issued issued) {
    String token = Utils.newRandomToken();
    this.put(token, issued);
    return token;
  }

  public Issued getIssued(String token) {
    return this.get(token);
  }

  public void addIssued(String token, Issued issued) {
    this.put(token, issued);
  }

  public void delIssued(String token) {
    this.remove(token);
  }
}
