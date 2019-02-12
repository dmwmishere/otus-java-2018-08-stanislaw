package ru.otus.shw16.frontend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.shw10.data.UserDataSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class UserDbWebSocketCreator implements WebSocketCreator {

    private final Map<Integer, UserDbWebSocket> webSockets = new HashMap<>();

    private final FrontendService frontendService;


    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        UserDbWebSocket webSocket = new UserDbWebSocket(frontendService);
        webSockets.put(webSocket.hashCode(), webSocket);
        return webSocket;
    }

    public void userAddStatus(String dbStatus) {
        webSockets.forEach((k, v) -> {
            log.info("Set dbStatus = {} for {}", dbStatus, k);
            v.userAddStatus(dbStatus);
        });
    }

    public void viewUserReport(Collection<UserDataSet> users) {
        webSockets.forEach((k, v) -> {
            log.info("Set dbStatus = {} for {}", users, k);
            v.userReport(users);
        });
    }
}
