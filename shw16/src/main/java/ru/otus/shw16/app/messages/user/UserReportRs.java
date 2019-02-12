package ru.otus.shw16.app.messages.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw16.app.messages.base.MsgToFrontend;
import ru.otus.shw16.frontend.FrontendService;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReportRs extends MsgToFrontend {

    List<UserDataSet> users;

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.viewUserReport(users);
    }
}
