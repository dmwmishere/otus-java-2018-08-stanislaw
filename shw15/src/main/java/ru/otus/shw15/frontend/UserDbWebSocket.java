package ru.otus.shw15.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw15.app.FrontendService;
import ru.otus.shw15.app.MessageSystemContext;
import ru.otus.shw15.app.messages.UserAddRq;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw15.messageSystem.Message;
import ru.otus.shw15.messageSystem.MessageSystem;

import java.io.IOException;

@WebSocket
public class UserDbWebSocket implements FrontendService {
    private Session session;


    private final Address address;
    private final MessageSystemContext context;

    public UserDbWebSocket(MessageSystemContext context, Address address){
        this.context = context;
        this.address = address;
        context.getMessageSystem().addAddressee(this);
        context.getMessageSystem().start(address);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
            try {
                UserDataSet user = new ObjectMapper().readValue(data, UserDataSet.class);
                user.getPhones().forEach(phone -> phone.setUser(user));

                Message message = new UserAddRq(getAddress(), context.getDbAddress(), user);

                context.getMessageSystem().sendMessage(message);
            } catch (Exception e) {
                System.out.print(e.toString());
            }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public void userAddStatus(String dbStatus) {
        try {
            session.getRemote().sendString(dbStatus);
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
