package ru.otus.shw15.app;

import ru.otus.shw15.messageSystem.Addressee;

/**
 * Created by tully.
 */
public interface FrontendService extends Addressee {

    void userAddStatus(String dbStatus);
}

