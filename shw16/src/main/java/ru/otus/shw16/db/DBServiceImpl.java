package ru.otus.shw16.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw11.dbService.DBServiceHibernateImpl;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.app.messages.user.UserAddRq;
import ru.otus.shw16.app.messages.user.UserReportRq;
import ru.otus.shw16.app.messages.user.UserReportRs;
import ru.otus.shw16.client.ProcessingMessageSocketClient;

import java.util.List;

@Slf4j
public class DBServiceImpl extends DBServiceHibernateImpl implements DBService {

    @Getter
    private final Address address;

    @Getter
    private final ProcessingMessageSocketClient socketCl;

    public DBServiceImpl(String nodeId, String destNodeId, String host, int port) throws Exception {
        this.address = new Address(nodeId);

        this.socketCl = new ProcessingMessageSocketClient(this,
                host, port, destNodeId);
        init();
    }

    public void init() {
        socketCl.start();
    }

    @Override
    public void save(UserAddRq userAddRq) {
        UserDataSet dataSet = userAddRq.getUser();
        log.info("Saving user to database: " + dataSet.getName());
        super.save(dataSet);
        List<UserDataSet> users = readAll();
        log.info("Response correlationId = {}", userAddRq.getCorrelationId());
        UserReportRs response = new UserReportRs(users);
        response.setCorrelationId(userAddRq.getCorrelationId());
        socketCl.sendMessage(response);
    }

    @Override
    public void userReport(UserReportRq userReportRs) {
        List<UserDataSet> users = readAll();
    }

    @Override
    public void close() throws Exception {
        super.close();
        socketCl.close();
    }
}
