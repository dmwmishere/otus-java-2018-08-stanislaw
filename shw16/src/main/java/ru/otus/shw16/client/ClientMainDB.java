package ru.otus.shw16.client;

import ru.otus.shw16.db.DBServiceImpl;

public class ClientMainDB {

    public static void main(String[] args) throws Exception {

        if(args.length != 4){
            System.err.println("Usage: java -jar ClientMainFrontEnd.jar " +
                    "<NODE ID> <DESTINATION NODE ID> <MESSAGE SYSTEM HOST> " +
                    "<MESSAGE SYSTEM PORT>");
            System.exit(1);
        }

        new DBServiceImpl(
                args[0],
                args[1],
                args[2],
                Integer.parseInt(args[3])
        );
    }

}
