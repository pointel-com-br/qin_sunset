package br.net.pin.qin_sunset.hook;

import java.io.IOException;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesGIZ {
  public static void init(ServletContextHandler context) {
    initRun(context);
    initList(context);
  }

  private static void initRun(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/giz/run");
  }

  private static void initList(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/giz");
  }
}
