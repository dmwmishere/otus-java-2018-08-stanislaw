package ru.otus.shw16.client;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.app.messages.base.Message;
import ru.otus.shw16.app.messages.base.Envelope;
import ru.otus.shw16.channel.SocketMessageWorker;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ProcessingMessageSocketClient {

    private final Addressee clientSrv;

    private final SocketMessageWorker client;

    private final Address destinationNode;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Map<UUID, Address> correlations = new ConcurrentHashMap<>();

    public ProcessingMessageSocketClient(Addressee clientSrv, String messageSystemHost, int messageSystemPort, String destNodeId) throws Exception {
        this.clientSrv = clientSrv;
        client = new ClientSocketMessageWorker(messageSystemHost, messageSystemPort);
        this.destinationNode = new Address(destNodeId);
    }

    public void start() {
        client.initClient(clientSrv.getAddress().getId());

        executorService.submit(() -> {
            try {
                while (true) {
                    log.debug("Take next...");
                    process(client.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("Executor submitted");
    }

    private void process(Message msg) {
        log.info("Message received: " + msg.toString());
        UUID uuid = UUID.randomUUID();
        correlations.put(uuid, msg.getFrom());
        Envelope env = (Envelope) msg.parseEnvelope();
        env.setCorrelationId(uuid);
        env.exec(clientSrv);
    }

    public void sendMessage(Envelope msg) {
        if (msg.getCorrelationId() == null) {
            log.info("Send to default: " + destinationNode.getId());
            client.send(new Message(clientSrv.getAddress(), destinationNode, msg));
        } else if (correlations.containsKey(msg.getCorrelationId())) {
            log.info("Correlation {} --> {}", msg.getCorrelationId(), correlations.get(msg.getCorrelationId()).getId());
            client.send(new Message(clientSrv.getAddress(), correlations.get(msg.getCorrelationId()), msg));
            correlations.remove(msg.getCorrelationId());
        } else {
            log.warn("FAILED TO FIND DESTINATION ADDRESS BY CORRELATION ID!");
        }
    }

    public void close() {
        client.close();
        executorService.shutdown();
    }
}
