package ru.otus.shw15.frontend;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.shw15.app.MessageSystemContext;
import ru.otus.shw15.messageSystem.Address;

import java.util.concurrent.TimeUnit;

/**
 * This class represents a servlet starting a webSocket application
 */
public class WebSocketUserDbServlet extends WebSocketServlet {
    private final static long LOGOUT_TIME = TimeUnit.MINUTES.toMillis(10);

    private final Address address;
    private final MessageSystemContext context;

    public WebSocketUserDbServlet(Address address, MessageSystemContext context) {
        this.address = address;
        this.context = context;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new UserDbWebSocketCreator(context, address));
    }
}
