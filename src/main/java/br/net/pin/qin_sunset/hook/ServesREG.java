package br.net.pin.qin_sunset.hook;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.Gson;

import br.net.pin.qin_sunset.data.Authed;
import br.net.pin.qin_sunset.data.Params;
import br.net.pin.qin_sunset.data.Way;
import br.net.pin.qin_sunset.work.OrdersREG;
import br.net.pin.qin_sunset.work.OrdersUtils;
import br.net.pin.qin_sunset.work.Runner;
import br.net.pin.qin_sunwiz.data.Deed;
import br.net.pin.qin_sunwiz.data.Delete;
import br.net.pin.qin_sunwiz.data.Insert;
import br.net.pin.qin_sunwiz.data.Order;
import br.net.pin.qin_sunwiz.data.Registry;
import br.net.pin.qin_sunwiz.data.Select;
import br.net.pin.qin_sunwiz.data.Update;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesREG {
  public static void init(ServletContextHandler context) {
    initRegCan(context);
    initRegNew(context);
    initRegAsk(context);
    initRegSet(context);
    initRegDel(context);
  }

  private static void initRegCan(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var registry = Registry.fromString(body);
        if (registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (registry.base == null || registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (registry.name == null || registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        resp.setContentType("application/json");
        resp.getWriter().print(new Gson().toJson(OrdersREG.regCan(authed, registry)));
      }
    }), "/reg/can");
  }

  private static void initRegNew(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var insert = Insert.fromString(body);
        if (insert.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (insert.registry.base == null || insert.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (insert.registry.name == null || insert.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        var allowed = authed.allowREG(insert.registry, Deed.INSERT);
        if (!allowed.head) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regNew(way, insert, allowed.tail));
      }
    }), "/reg/new");
  }

  private static void initRegAsk(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var select = Select.fromString(body);
        if (select.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (select.registry.base == null || select.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (select.registry.name == null || select.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        var allowed = authed.allowREG(select.registry, Deed.SELECT);
        if (!allowed.head) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        applyAlwaysOrderByIfHas(way, authed, select);
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regAsk(way, select, allowed.tail));
      }

      private void applyAlwaysOrderByIfHas(Way way, Authed authed, Select select) {
        var always_order = OrdersUtils.askParams(way, authed, Params.ALWAYS_ORDER_BY_IF_HAS.toString());
        if (always_order != null && !always_order.isEmpty()) {
          for (var always_order_by : always_order.split(",")) {
            var always_order_by_parts = always_order_by.split(" ");
            var always_order_by_name = always_order_by_parts[0].trim();
            var always_order_by_desc = false;
            if (always_order_by_parts.length > 1) {
              if (always_order_by_parts[0].trim().toUpperCase() == "desc") {
                always_order_by_desc = true;
              }
            }
            var found = false;
            for (var field : select.fields) {
              if (always_order_by_name.equals(field.name)) {
                if (select.orders == null) {
                  select.orders = new ArrayList<>();
                }
                select.orders.add(new Order(always_order_by_name, always_order_by_desc));
                found = true;
                break;
              }
            }
            if (found) {
              break;
            }
          }
        }
      }
    }), "/reg/ask");
  }

  private static void initRegSet(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var update = Update.fromString(body);
        if (update.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (update.registry.base == null || update.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (update.registry.name == null || update.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        var allowed = authed.allowREG(update.registry, Deed.UPDATE);
        if (!allowed.head) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regSet(way, update, allowed.tail));
      }
    }), "/reg/set");
  }

  private static void initRegDel(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var delete = Delete.fromString(body);
        if (delete.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (delete.registry.base == null || delete.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (delete.registry.name == null || delete.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        var allowed = authed.allowREG(delete.registry, Deed.DELETE);
        if (!allowed.head) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regDel(way, delete, allowed.tail));
      }
    }), "/reg/del");
  }
}
