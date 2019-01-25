package ru.otus.shw15.frontend;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.shw15.app.MessageSystemContext;
import ru.otus.shw15.messageSystem.Address;

/**
 * @author v.chibrikov
 */
public class UserDbWebSocketCreator implements WebSocketCreator {

    private final Address address;
    private final MessageSystemContext context;

    public UserDbWebSocketCreator(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;

    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return new UserDbWebSocket(context, address);
    }


}
