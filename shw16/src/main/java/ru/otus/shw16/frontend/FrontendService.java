package ru.otus.shw16.frontend;

import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.app.messages.base.Envelope;

import java.util.Collection;

/**
 * Created by tully.
 */
public interface FrontendService extends Addressee {

    void userAddStatus(String dbStatus);

    void viewUserReport(Collection<UserDataSet> users);

    void sendMessage(Envelope envelope);

}

