package ru.otus.shw16.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import ru.otus.shw12.*;
import ru.otus.shw16.frontend.WebSocketUserDbServlet;

import java.util.HashMap;
@Slf4j
public class ClientMainFrontEnd {

    private static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {

        if(args.length < 4){
            System.err.println("Usage: java -jar ClientMainFrontEnd.jar " +
                    "<NODE ID> <DEFAULT DESTINATION NODE ID> <MESSAGE SYSTEM HOST> " +
                    "<MESSAGE SYSTEM PORT> [PORT]");
            System.exit(1);
        }

        if(args.length > 4){
            PORT = Integer.parseInt(args[4]);
            log.warn("Custom frontend port = " + PORT);
        }

        UserDBServer uSrv = new UserDBServer(PUBLIC_HTML, PORT, MapUtils.putAll(new HashMap<>(),
                new Object[][]{
                        {new WebSocketUserDbServlet(
                                args[0],
                                args[1],
                                args[2],
                                Integer.parseInt(args[3])
                        ), "/asyncadduser"},
                }));

        uSrv.start();

        uSrv.join();

    }

}
