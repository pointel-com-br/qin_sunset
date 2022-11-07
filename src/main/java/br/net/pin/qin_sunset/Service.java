package br.net.pin.qin_sunset;

import java.io.File;
import java.nio.file.Files;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import br.net.pin.qin_sunset.core.Way;
import br.net.pin.qin_sunset.hook.ServerAuth;
import br.net.pin.qin_sunset.hook.ServerUtils;
import br.net.pin.qin_sunset.hook.ServesAPP;
import br.net.pin.qin_sunset.hook.ServesBAS;
import br.net.pin.qin_sunset.hook.ServesCMD;
import br.net.pin.qin_sunset.hook.ServesDIR;
import br.net.pin.qin_sunset.hook.ServesGIZ;
import br.net.pin.qin_sunset.hook.ServesPUB;
import br.net.pin.qin_sunset.hook.ServesREG;

public class Service {

  private final Way runny;
  private final QueuedThreadPool threadPool;
  private final Server server;
  private final HttpConfiguration httpConfig;
  private final HttpConnectionFactory httpFactory;
  private final ServerConnector connector;
  private final ServletContextHandler context;

  public Service(Way runny) throws Exception {
    this.runny = runny;
    this.threadPool = new QueuedThreadPool(this.runny.air.setup.threadsMax,
        this.runny.air.setup.threadsMin, this.runny.air.setup.threadsIdleTimeout);
    this.server = new Server(this.threadPool);
    this.httpConfig = new HttpConfiguration();
    this.httpConfig.setSendDateHeader(false);
    this.httpConfig.setSendServerVersion(false);
    this.httpFactory = new HttpConnectionFactory(this.httpConfig);
    this.connector = new ServerConnector(this.server, httpFactory);
    connector.setHost(this.runny.air.setup.serverHost);
    connector.setPort(this.runny.air.setup.serverPort);
    this.server.setConnectors(new Connector[] { this.connector });
    this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    this.context.setContextPath("");
    this.context.setAttribute("QinSunset.Way", this.runny);
    this.server.setHandler(this.context);
    this.init_serves();
  }

  private void init_serves() throws Exception {
    this.server_auth();
    if (this.runny.air.setup.servesPUB) {
      this.serves_pub();
    }
    if (this.runny.air.setup.servesAPP) {
      this.serves_app();
    }
    if (this.runny.air.setup.servesDIR) {
      this.serves_dir();
    }
    if (this.runny.air.setup.servesCMD) {
      this.serves_cmd();
    }
    if (this.runny.air.setup.servesBAS) {
      this.serves_bas();
    }
    if (this.runny.air.setup.servesBAS && this.runny.air.setup.servesREG) {
      this.serves_reg();
    }
    if (this.runny.air.setup.servesGIZ) {
      this.serves_giz();
    }
    this.server_utils();
  }

  private void server_auth() {
    this.runny.logInfo("Serving Auth...");
    ServerAuth.init(this.context);
  }

  private void serves_pub() throws Exception {
    this.runny.logInfo("Serving PUB...");
    var holder = new ServletHolder(new ServesPUB());
    var pubDir = new File("pub");
    if (!pubDir.exists()) {
      Files.createDirectories(pubDir.toPath());
    }
    holder.setInitParameter("basePath", pubDir.getAbsolutePath());
    this.context.addServlet(holder, "/pub/*");
  }

  private void serves_app() {
    this.runny.logInfo("Serving APP...");
    ServesAPP.init(this.context);
  }

  private void serves_dir() {
    this.runny.logInfo("Serving DIR...");
    ServesDIR.init(this.context);
  }

  private void serves_cmd() {
    this.runny.logInfo("Serving CMD...");
    ServesCMD.init(this.context);
  }

  private void serves_bas() {
    this.runny.logInfo("Serving BAS...");
    ServesBAS.init(this.context);
  }

  private void serves_reg() {
    this.runny.logInfo("Serving REG...");
    ServesREG.init(this.context);
  }

  private void serves_giz() {
    this.runny.logInfo("Serving GIZ...");
    ServesGIZ.init(this.context);
  }

  private void server_utils() {
    this.runny.logInfo("Serving Utils...");
    ServerUtils.init(this.context, this.runny.air.setup);
  }

  public void start() throws Exception {
    this.runny.logInfo("Starting Server...");
    this.runny.logInfo("Setup On-Air: " + this.runny.air.setup);
    this.runny.logInfo("Users On-Air: " + this.runny.air.users);
    this.runny.logInfo("Bases On-Air: " + this.runny.air.bases);
    this.server.start();
    this.server.join();
  }

}
