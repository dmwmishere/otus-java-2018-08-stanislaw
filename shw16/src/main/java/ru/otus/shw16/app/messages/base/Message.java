package ru.otus.shw16.app.messages.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.otus.shw15.messageSystem.Address;

import java.io.IOException;


@Slf4j
@Data
@NoArgsConstructor
public class Message {

    private Address from;
    private Address to;

    private Class envelopeClass;
    private String envelope;

    public Message(Address from, Address to, Object payload){
        this.from = from;
        this.to = to;
        if(payload!=null) {
            envelopeClass = payload.getClass();
            log.debug("Envelope class: " + envelopeClass.getName());
        }
        try {
            this.envelope = new ObjectMapper().writeValueAsString(payload);
            log.debug("Envelope str: " + envelope);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Object parseEnvelope(){
        try {
            return new ObjectMapper().readValue(envelope, envelopeClass);
        } catch (IOException e){
            e.printStackTrace();
            return e;
        }
    }
}
