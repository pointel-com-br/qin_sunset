package br.net.pin.qin_sunset.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.net.pin.qin_sunwiz.data.Deed;
import br.net.pin.qin_sunwiz.data.Registry;

public class Authed {
  private final User user;
  private final Group group;
  private final IssuedMap issuedMap;
  private final List<Allow> access;

  public Authed(User user, Group group) {
    this.user = user;
    this.group = group;
    this.issuedMap = new IssuedMap();
    this.access = new ArrayList<>();
    this.initAccess();
  }

  private void initAccess() {
    if (this.group != null) {
      for (var group_allow : this.group.access) {
        this.access.add(group_allow);
      }
    }
    if (this.user.access != null) {
      for (var user_allow : this.user.access) {
        this.access.removeIf(on_group -> on_group.isOnSameResource(user_allow));
        this.access.add(user_allow);
      }
    }
  }

  public String getUserName() {
    return user.name;
  }

  public String getHome() {
    if (!this.user.home.isEmpty()) {
      return this.user.home;
    } else if (this.group != null) {
      return this.group.home;
    } else {
      return "";
    }
  }

  public String getLang() {
    if (!this.user.lang.isEmpty()) {
      return this.user.lang;
    } else if (this.group != null) {
      return this.group.lang;
    } else {
      return "";
    }
  }

  public Boolean isMaster() {
    if (this.user.master) {
      return true;
    } else if (this.group != null) {
      return this.group.master;
    } else {
      return false;
    }
  }

  public List<Allow> getAccess() {
    return this.access;
  }

  public boolean allowAPP(String name) {
    if (isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.app != null && access.app.name.equals(name)) {
        return true;
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.app != null && access.app.name.equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean allowDIR(String fullPath, boolean toMutate) {
    if (this.isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.dir != null && fullPath.startsWith(access.dir.path)) {
        if (toMutate) {
          if (access.dir.mutate) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.dir != null && fullPath.startsWith(access.dir.path)) {
          if (toMutate) {
            if (access.dir.mutate) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean allowCMD(String name) {
    if (isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.cmd != null && access.cmd.name.equals(name)) {
        return true;
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.cmd != null && access.cmd.name.equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean allowBAS(String name, boolean toMutate) {
    if (this.isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.bas != null && access.bas.name.equals(name)) {
        if (toMutate) {
          if (access.bas.mutate) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.bas != null && access.bas.name.equals(name)) {
          if (toMutate) {
            if (access.bas.mutate) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean allowREG(Registry registry, Deed deed) {
    if (this.isMaster()) {
      return true;
    }
    if (!this.allowBAS(registry.base, deed.mutates)) {
      return false;
    }
    for (var allow : this.getAccess()) {
      if (allow.reg != null && allow.reg.registry != null) {
        if (canAllowResource(allow.reg.registry, registry)) {
          if (allow.reg.all) {
            return true;
          }
          switch (deed) {
            case INSERT:
              if (allow.reg.insert) {
                return true;
              }
              break;
            case SELECT:
              if (allow.reg.select) {
                return true;
              }
              break;
            case UPDATE:
              if (allow.reg.update) {
                return true;
              }
              break;
            case DELETE:
              if (allow.reg.delete) {
                return true;
              }
              break;
          }
        }
      }
    }
    return false;
  }

  public static boolean canAllowResource(Registry guarantor, Registry requester) {
    if (Objects.equals(guarantor.name, requester.name)) {
      if (checkWeighted(guarantor.base, requester.base) &&
          checkWeighted(guarantor.catalog, requester.catalog) &&
          checkWeighted(guarantor.schema, requester.schema)) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkWeighted(String strong, String weak) {
    if (strong == null || strong.isEmpty()) {
      return true;
    }
    return strong.equals(weak);
  }

  public String getParam(String name) {
    if (this.user.params.containsKey(name)) {
      return this.user.params.get(name);
    }
    if (this.group != null && this.group.params.containsKey(name)) {
      return this.group.params.get(name);
    }
    return null;
  }

  public String newIssued(Issued issued) {
    return this.issuedMap.newIssued(issued);
  }

  public Issued getIssued(String token) {
    return this.issuedMap.get(token);
  }

  public void addIssued(String token, Issued issued) {
    this.issuedMap.put(token, issued);
  }

  public void delIssued(String token) {
    this.issuedMap.remove(token);
  }
}
