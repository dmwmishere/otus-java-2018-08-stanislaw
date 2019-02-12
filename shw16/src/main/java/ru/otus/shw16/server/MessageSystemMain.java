package ru.otus.shw16.server;

import ru.otus.shw16.messageSystem.MessageSystem;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MessageSystemMain {
    public static void main(String [] argz) throws Exception{
        MessageSystem messageSystem = new MessageSystem();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        //startClient(executorService);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Server");
        ProcessingMessageSocketServer server = new ProcessingMessageSocketServer(messageSystem);
        mbs.registerMBean(server, name);

        server.start();

        executorService.shutdown();


    }
}
