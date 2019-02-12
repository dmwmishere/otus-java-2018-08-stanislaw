package ru.otus.shw16.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw16.app.messages.base.Envelope;
import ru.otus.shw16.app.messages.user.UserAddRq;

import java.io.IOException;
import java.util.Collection;

@WebSocket
@RequiredArgsConstructor
public class UserDbWebSocket {

    private Session session;

    @Getter
    private final FrontendService myClient;

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            UserDataSet user = new ObjectMapper().readValue(data, UserDataSet.class);
            user.getPhones().forEach(phone -> phone.setUser(null));

            Envelope message = new UserAddRq(user);

            myClient.sendMessage(message);
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

    public void userAddStatus(String dbStatus) {
        try {
            session.getRemote().sendString(dbStatus);
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void userReport(Collection<UserDataSet> users){
        try {
            session.getRemote().sendString(new ObjectMapper().writeValueAsString(users));
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}

