package ru.otus.shw16.frontend;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.app.messages.base.Envelope;
import ru.otus.shw16.client.ProcessingMessageSocketClient;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class WebSocketUserDbServlet extends WebSocketServlet implements FrontendService, Addressee {
    private final static long LOGOUT_TIME = TimeUnit.MINUTES.toMillis(10);

    private final Address nodeId;
    private final String destNodeId;
    private final String host;
    private final int port;

    private final UserDbWebSocketCreator wsCreator;

    @Getter
    private final ProcessingMessageSocketClient socketCl;

    public WebSocketUserDbServlet(String nodeId, String destNodeId, String host, int port) throws Exception {
        this.nodeId = new Address(nodeId);
        this.destNodeId = destNodeId;
        this.host = host;
        this.port = port;

        socketCl = new ProcessingMessageSocketClient(this, host, port, destNodeId);
        socketCl.start();

        wsCreator = new UserDbWebSocketCreator(this);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        try {
            factory.setCreator(wsCreator);
        } catch (Exception e){
            log.error("Failed to create web socket: " + e.getMessage());
        }
    }

    @Override
    public void userAddStatus(String dbStatus) {
        wsCreator.userAddStatus(dbStatus);
    }

    @Override
    public void viewUserReport(Collection<UserDataSet> users) {
        wsCreator.viewUserReport(users);
    }

    @Override
    public void sendMessage(Envelope envelope) {
        socketCl.sendMessage(envelope);
    }

    @Override
    public Address getAddress() {
        return nodeId;
    }
}
