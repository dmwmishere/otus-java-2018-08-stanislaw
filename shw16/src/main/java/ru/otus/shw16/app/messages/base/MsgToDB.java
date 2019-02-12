package ru.otus.shw16.app.messages.base;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.db.DBService;

/**
 * Created by tully.
 */
@Slf4j
public abstract class MsgToDB extends Envelope {

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            log.debug("DB message received " + ((DBService) addressee).getClass().getName());
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
