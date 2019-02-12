package ru.otus.shw16.messageSystem;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw15.messageSystem.Addressee;
import ru.otus.shw16.app.messages.base.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * @author tully
 */
@Slf4j
public final class MessageSystem {

    private final List<Thread> workers;
    private final Map<Address, Map<String, BlockingQueue<Message>> > messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystem() {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAdressee(Address address, BlockingQueue<Message> inputQueue, BlockingQueue<Message> outputQueue){
        Map<String, BlockingQueue<Message> > queuePair = new HashMap<>();
        queuePair.put("in", inputQueue);
        queuePair.put("out", outputQueue);
        messagesMap.put(address, queuePair);
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).get("out").add(message);
    }


    public void start(Address address){
        String name = "MS-worker-" + address.getId();
        log.debug("Start " + name);
        Thread thread = new Thread(() -> {
            BlockingQueue<Message> queue = messagesMap.get(address).get("in");
            while (true) {
                try {
                    Message message = queue.take();
                    log.info("Process message {} >>> {}", message.getFrom().getId(), message.getTo().getId());
                    if(messagesMap.containsKey(message.getTo())){
                        messagesMap.get(message.getTo()).get("out").put(message);
                    } else {
                        log.warn("Cannot find destination queue for " + message.getTo() + "!");
                    }
                } catch (InterruptedException e) {
                    log.error("Thread interrupted. Finishing: " + name);
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
