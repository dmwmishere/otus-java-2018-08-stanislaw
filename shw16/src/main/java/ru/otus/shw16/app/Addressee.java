package ru.otus.shw16.app;

import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.client.ProcessingMessageSocketClient;

/**
 * @author tully
 */
public interface Addressee {

    Address getAddress();

    ProcessingMessageSocketClient getSocketCl();

}
