package br.net.pin.qin_server.work;

import java.io.StringWriter;

import br.net.pin.qin_server.data.Way;
import br.net.pin.qin_sunjar.data.Delete;
import br.net.pin.qin_sunjar.data.Insert;
import br.net.pin.qin_sunjar.data.Select;
import br.net.pin.qin_sunjar.data.Update;
import br.net.pin.qin_sunjar.flow.CSVMaker;
import br.net.pin.qin_sunjar.flow.CSVWrite;
import jakarta.servlet.ServletException;

public class OrdersREG {
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
