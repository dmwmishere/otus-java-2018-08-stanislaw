package ru.otus.shw12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.shw10.base.DBService;

import java.io.IOException;

public class UserDBServer extends Server {

    public UserDBServer(final String publicHtmlPath, final int port, DBService dbSrv) throws IOException {
        super(port);
        ResourceHandler resourceHandler;
        resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(publicHtmlPath);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(new ServletHolder(new AddUserServlet(dbSrv)), "/useradd");
        context.addServlet(new ServletHolder(new SearchUserServlet(dbSrv, templateProcessor)), "/searchuser");
        context.addServlet(new ServletHolder(new BrowseUserServlet(dbSrv, templateProcessor)), "/browseuser");
        setHandler(new HandlerList(resourceHandler, context));
    }

}
