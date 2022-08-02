package br.net.pin.qin_sunset.work;

import java.io.StringWriter;
import java.util.Objects;

import br.net.pin.qin_sunset.data.Allow;
import br.net.pin.qin_sunset.data.Authed;
import br.net.pin.qin_sunset.data.Way;
import br.net.pin.qin_sunwiz.data.Delete;
import br.net.pin.qin_sunwiz.data.Insert;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Select;
import br.net.pin.qin_sunwiz.data.Update;
import br.net.pin.qin_sunwiz.flow.CSVMaker;
import br.net.pin.qin_sunwiz.flow.CSVWrite;
import jakarta.servlet.ServletException;

public class OrdersREG {

  public static Allow.REG regCan(Authed authed, Registry registry) {
    var result = new Allow.REG();
    result.registry = registry;
    if (authed.isMaster()) {
      result.all = true;
      result.insert = true;
      result.select = true;
      result.update = true;
      result.delete = true;
      return result;
    }
    result.all = false;
    result.insert = false;
    result.select = false;
    result.update = false;
    result.delete = false;
    for (var allow : authed.getAccess()) {
      if (allow.reg != null && allow.reg.registry != null) {
        if (canAllowResource(allow.reg.registry, registry)) {
          if (allow.reg.all != null) {
            result.all = allow.reg.all;
          }
          if (allow.reg.insert != null) {
            result.insert = allow.reg.insert;
          }
          if (allow.reg.select != null) {
            result.select = allow.reg.select;
          }
          if (allow.reg.update != null) {
            result.update = allow.reg.update;
          }
          if (allow.reg.delete != null) {
            result.delete = allow.reg.delete;
          }
        }
      }
    }
    return result;
  }

  private static boolean canAllowResource(Registry guarantor, Registry requester) {
    if (Objects.equals(guarantor.name, requester.name)) {
      if (checkWeighted(guarantor.base, requester.base) &&
          checkWeighted(guarantor.catalog, requester.catalog) &&
          checkWeighted(guarantor.schema, requester.schema)) {
        return true;
      }
    }
    return false;
  }

  private static boolean checkWeighted(String strong, String weak) {
    if (strong == null || strong.isEmpty()) {
      return true;
    }
    return strong.equals(weak);
  }

  public static String regNew(Way way, Insert insert) throws ServletException {
    try {
      way.logStep(insert);
      var helped = way.stores.getHelp(insert.registry.base);
      var result = helped.helper.insert(helped.link, insert);
      return "Inserted: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regAsk(Way way, Select select) throws ServletException {
    try {
      way.logStep(select);
      var helped = way.stores.getHelp(select.registry.base);
      var result = helped.helper.select(helped.link, select);
      var maker = new CSVMaker(result, select.fields);
      var build = new StringWriter();
      try (var write = new CSVWrite(build)) {
        String[] line;
        while ((line = maker.makeLine()) != null) {
          write.writeLine(line);
        }
      }
      return build.toString();
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regSet(Way way, Update update) throws ServletException {
    try {
      way.logStep(update);
      var helped = way.stores.getHelp(update.registry.base);
      var result = helped.helper.update(helped.link, update);
      return "Updated: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regDel(Way way, Delete delete) throws ServletException {
    try {
      way.logStep(delete);
      var helped = way.stores.getHelp(delete.registry.base);
      var result = helped.helper.delete(helped.link, delete);
      return "Deleted: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
