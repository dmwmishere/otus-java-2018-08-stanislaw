package ru.otus.shw15.messageSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tully
 */
public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final List<Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystem() {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }


    public void start(Address address){
        String name = "MS-worker-" + address.getId();
        System.out.println("Start " + name);
        Thread thread = new Thread(() -> {
            LinkedBlockingQueue<Message> queue = messagesMap.get(address);
            while (true) {
                try {
                    Message message = queue.take();
                    System.out.println(String.format("Message system process %s >>> %s", message.getFrom().getId(), message.getTo().getId()));
                    message.exec(addresseeMap.get(address));
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    public void start() {
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
            start(entry.getKey());
        }
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
