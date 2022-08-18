package br.net.pin.qin_sunset.data;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;

import br.net.pin.qin_sunwiz.data.Registier;
import br.net.pin.qin_sunwiz.data.Strain;

public class Allow {
  public APP app;
  public DIR dir;
  public CMD cmd;
  public BAS bas;
  public REG reg;
  public SQL sql;
  public LIZ liz;
  public GIZ giz;

  public static class APP {
    public String name;
  }

  public static class DIR {
    public String path;
    public Boolean mutate;
  }

  public static class CMD {
    public String name;
    public List<String> args;
  }

  public static class BAS {
    public String name;
    public Boolean mutate;
  }

  public static class REG {
    public Registier registier;
    public Boolean all;
    public Boolean insert;
    public Boolean select;
    public Boolean update;
    public Boolean delete;
    public Strain strain;
  }

  public static class SQL {
    public String base;
    public String path;
  }

  public static class LIZ {
    public String path;
  }

  public static class GIZ {
    public String base;
    public String path;
  }

  public void fixDefaults() {
    if (this.app != null) {
      if (this.app.name == null || this.app.name.isEmpty()) {
        this.app = null;
      }
    }
    if (this.dir != null) {
      if (this.dir.path == null || this.dir.path.isEmpty()) {
        this.dir = null;
      } else {
        this.dir.path = new File(this.dir.path).getAbsolutePath();
        this.dir.mutate = this.dir.mutate != null ? this.dir.mutate : false;
      }
    }
    if (this.cmd != null) {
      if (this.cmd.name == null || this.cmd.name.isEmpty()) {
        this.cmd = null;
      }
    }
    if (this.bas != null) {
      if (this.bas.name == null || this.bas.name.isEmpty()) {
        this.bas = null;
      }
    }
    if (this.reg != null) {
      if (this.reg.registier == null) {
        this.reg = null;
      } else {
        this.reg.all = this.reg.all != null ? this.reg.all : false;
        this.reg.insert = this.reg.insert != null ? this.reg.insert : false;
        this.reg.select = this.reg.select != null ? this.reg.select : false;
        this.reg.update = this.reg.update != null ? this.reg.update : false;
        this.reg.delete = this.reg.delete != null ? this.reg.delete : false;
      }
    }
    if (this.sql != null) {
      if (this.sql.base == null || this.sql.base.isEmpty() || this.sql.path == null
          || this.sql.path.isEmpty()) {
        this.sql = null;
      } else {
        this.sql.path = new File(this.sql.path).getAbsolutePath();
      }
    }
    if (this.liz != null) {
      if (this.liz.path == null || this.liz.path.isEmpty()) {
        this.liz = null;
      } else {
        this.liz.path = new File(this.liz.path).getAbsolutePath();
      }
    }
    if (this.giz != null) {
      if (this.giz.base == null || this.giz.base.isEmpty() || this.giz.path == null
          || this.giz.path.isEmpty()) {
        this.giz = null;
      } else {
        this.giz.path = new File(this.giz.path).getAbsolutePath();
      }
    }
  }

  public boolean isOnSameResource(Allow than) {
    if (this.app != null && than.app != null) {
      return Objects.equals(this.app.name, than.app.name);
    }
    if (this.dir != null && than.dir != null) {
      return Objects.equals(this.dir.path, than.dir.path);
    }
    if (this.cmd != null && than.cmd != null) {
      return Objects.equals(this.cmd.name, than.cmd.name);
    }
    if (this.bas != null && than.bas != null) {
      return Objects.equals(this.bas.name, than.bas.name);
    }
    if (this.reg != null && than.reg != null) {
      return Objects.equals(this.reg.registier, than.reg.registier);
    }
    if (this.sql != null && than.sql != null) {
      return Objects.equals(this.sql.base, than.sql.base) && Objects.equals(this.sql.path, than.sql.path);
    }
    if (this.liz != null && than.liz != null) {
      return Objects.equals(this.liz.path, than.liz.path);
    }
    if (this.giz != null && than.giz != null) {
      return Objects.equals(this.giz.base, than.giz.base) && Objects.equals(this.giz.path, than.giz.path);
    }
    return false;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Allow fromString(String json) {
    return new Gson().fromJson(json, Allow.class);
  }
}
