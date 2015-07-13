package no.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.FileInputStream;

public class Main {

    private final int port;
    private final String contextPath;

    public static void main(String[] args) throws Exception {
        Main sc = new Main();
		sc.start();
    }

    public Main() {
        System.setProperty("org.apache.jasper.compiler.disablejsr199","true");
        port = Integer.parseInt(System.getProperty("jetty.port", "8080"));
        contextPath = System.getProperty("jetty.contextPath", "/");
    }

    private void start() {
        try {
        	System.setProperty("org.eclipse.jetty.util.log.INFO", "true");

            Server srv = new Server(port);
            srv.setStopAtShutdown(true);

            // Add the warFile (this jar)
            WebAppContext context = new WebAppContext("jetty-pkg/target/lib/war-1.0.0-SNAPSHOT.war", contextPath);

            context.setAttribute(
                    "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                    ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );
            context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            context.setClassLoader(new WebAppClassLoader(getClass().getClassLoader(), context));
            context.setServer(srv);

            // Add the handlers
            HandlerList handlers = new HandlerList();
            handlers.addHandler(context);
            srv.setHandler(handlers);

            srv.start();
            srv.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
