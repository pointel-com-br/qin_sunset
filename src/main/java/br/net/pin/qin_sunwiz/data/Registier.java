package br.net.pin.qin_sunwiz.data;

import java.util.Objects;

import com.google.gson.Gson;

public class Registier {
  public String base;
  public Registry registry;

  public Registier() {
  }

  public Registier(String base) {
    this.base = base;
  }

  public Registier(Registry registry) {
    this.registry = registry;
  }

  public Registier(String base, Registry registry) {
    this.base = base;
    this.registry = registry;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Registier)) {
      return false;
    }
    Registier registryer = (Registier) o;
    return Objects.equals(base, registryer.base) && Objects.equals(registry, registryer.registry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, registry);
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Registier fromString(String json) {
    return new Gson().fromJson(json, Registier.class);
  }
}
