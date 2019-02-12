package ru.otus.shw16.app.messages.user;

import lombok.*;
import ru.otus.shw16.frontend.FrontendService;
import ru.otus.shw16.app.messages.base.MsgToFrontend;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRs extends MsgToFrontend {
    private String dbStatus;

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.userAddStatus(dbStatus);
    }
}
