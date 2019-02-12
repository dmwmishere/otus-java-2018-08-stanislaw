package ru.otus.shw16.app.messages.user;

import lombok.*;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw16.app.messages.base.MsgToDB;
import ru.otus.shw16.db.DBService;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRq extends MsgToDB {
    private UserDataSet user;

    @Override
    public void exec(DBService dbService) {
        user.getPhones().forEach(phone -> phone.setUser(user));
        dbService.save(this);
    }
}
