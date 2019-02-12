package ru.otus.shw16.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.app.messages.base.Message;
import ru.otus.shw16.app.MsgWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Created by tully.
 */
@Slf4j
public class SocketMessageWorker implements MsgWorker<Message> {
    private static final int WORKERS_COUNT = 2;

    @Getter
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();

    @Getter
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    private String nodeId = null;

    public SocketMessageWorker(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    private void initBase(){
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public void initClient(String nodeId) {
        log.info("Init client \'{}\'", nodeId);
        initBase();
        Message signOn = new Message(new Address(nodeId), new Address("MessageSystemSignOn"), null);
        log.debug(signOn.toString());
        send(signOn);

    }

    public String initServer() {
        initBase();
        Message signOn = null;
        while (true) {
            log.debug("Someone connected. Wait for signon request.");
            try {
                signOn = input.poll(1000, TimeUnit.MILLISECONDS);
                if (signOn != null) {
                    if ("MessageSystemSignOn".equals(signOn.getTo().getId())) {
                        break;
                    } else {
                        log.info("Non signOn request received from " + signOn.getFrom().getId() + "! Will ignore.");
                        signOn = null;
                    }
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        nodeId = signOn.getFrom().getId();
        log.info("Got singon from " + nodeId);
        return nodeId;
    }

    @Blocks
    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                Message msg = output.take(); //blocks
                String json = new ObjectMapper().writeValueAsString(msg);
                log.debug("sending: " + json);
                out.println(json);
                out.println(); //line with json + an empty line
            }
            log.warn("Exit sender loop");
        } catch (InterruptedException | IOException e) {
            log.error("Failed to send message: " + e.getMessage());
        }
    }

    @Blocks
    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) { //blocks
                log.trace("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    String json = stringBuilder.toString();
                    Message msg = getMsgFromJSON(json);
                    log.debug("Message received from {}: {}", Optional.ofNullable(nodeId).orElse("Unauthorized"), msg);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
            log.warn("Exit receiver loop");
        } catch (IOException e) {
            log.error("Failed to receive message:" + e.getMessage());
        } finally {
            close();
        }
    }

    private static Message getMsgFromJSON(String json) throws IOException {
        return new ObjectMapper().readValue(json, Message.class);
    }
}
