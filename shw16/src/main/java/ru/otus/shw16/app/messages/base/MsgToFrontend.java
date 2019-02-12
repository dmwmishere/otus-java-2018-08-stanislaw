package ru.otus.shw16.app.messages.base;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.frontend.FrontendService;
/**
 * Created by tully.
 */

@Slf4j
public abstract class MsgToFrontend extends Envelope {

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            log.debug("FrontEnd message received");
            exec((FrontendService) addressee);
        } else {
            //todo error!
        }
    }

    public abstract void exec(FrontendService frontendService);
}