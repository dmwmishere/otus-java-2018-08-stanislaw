package ru.otus.shw12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.shw10.base.DBService;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserDBServer extends Server {

    public UserDBServer(final String publicHtmlPath, final int port, DBService dbSrv) throws IOException {
        this(publicHtmlPath, port, new HashMap<HttpServlet, String>() {{
            TemplateProcessor templateProcessor = new TemplateProcessor();
            put(new AddUserServlet(dbSrv), "/useradd");
            put(new SearchUserServlet(dbSrv, templateProcessor), "/searchuser");
            put(new BrowseUserServlet(dbSrv, templateProcessor), "/browseuser");
        }});
    }

    public UserDBServer(final String publicHtmlPath, final int port, Map<HttpServlet, String> servletsMapping){
        super(port);
        ResourceHandler resourceHandler;
        resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(publicHtmlPath);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletsMapping.forEach((servlet, link) -> context.addServlet(new ServletHolder(servlet), link));
        setHandler(new HandlerList(resourceHandler, context));
    }

}
