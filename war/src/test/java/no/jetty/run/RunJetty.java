package no.jetty.run;


import org.eclipse.jetty.annotations.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.ServletContainerInitializer;
import java.io.File;

public class RunJetty {


    public RunJetty() {
        System.setProperty("org.apache.jasper.compiler.disablejsr199","false");
    }


    public static void main(String ...args){
        new RunJetty().start();
    }

    private void start() {
        try {
            System.setProperty("org.eclipse.jetty.LEVEL", "INFO");

            Server srv = new Server(8080);
            srv.setStopAtShutdown(true);

            Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(srv);
            classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

            WebAppContext context = new WebAppContext();
            context.setContextPath("/");

            String dir;
            if (new File("src/main/webapp").exists()) dir = "src/main/webapp";
            else dir = "war/src/main/webapp";

            context.setResourceBase(dir);
            context.setDescriptor(dir + "/WEB-INF/web.xml");

            context.setAttribute(
                    "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                    ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$|.*/classes/" );
            context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

            context.setParentLoaderPriority(true);
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
