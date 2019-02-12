package ru.otus.shw16;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw16.app.messages.base.Message;
import ru.otus.shw16.app.messages.user.UserAddRq;
import ru.otus.shw16.app.messages.user.UserAddRs;
import ru.otus.shw15.messageSystem.Address;

import java.util.ArrayList;

public class Test1 {

    @Test
    public void test0_TEST_DESC() throws Exception{

        // FRONT 2 DB --> MsgToDB

        UserDataSet user = new UserDataSet();
        user.setName("qwe rty");
        user.setAge(34);
        user.setPhones(new ArrayList<>());
        user.getPhones().add(new PhoneDataSet());
        user.getPhones().get(0).setPhone("1234567890");
        UserAddRq userAddRq = new UserAddRq(user);

        // DB 2 FRONT --> MsgToFrontEnd

        UserAddRs userAddRs = new UserAddRs("OK");

        ObjectMapper om = new ObjectMapper();

        String jsonFromFront = om.writeValueAsString(userAddRq);
        String jsonFromDB = om.writeValueAsString(userAddRs);

        System.out.println(jsonFromFront);

        System.out.println(jsonFromDB);

        // MS View:

        System.out.println(om.writeValueAsString(om.readValue(jsonFromFront, UserAddRq.class)));

    }


    @Test
    public void test1_TEST_DESC() throws Exception{

        UserDataSet user = new UserDataSet();
        user.setName("qwe rty");
        user.setAge(34);
        user.setPhones(new ArrayList<>());
        user.getPhones().add(new PhoneDataSet());
        user.getPhones().get(0).setPhone("1234567890");

        Message msgIn = new Message(new Address("FROM-DB"), new Address("TO-FRONT"), user);

        ObjectMapper om = new ObjectMapper();

        om.readValue(om.writeValueAsString(user), UserDataSet.class);

        String jsonFromFront = om.writerWithDefaultPrettyPrinter().writeValueAsString(msgIn);

        System.out.println("MSG_IN = " + jsonFromFront);

        Message msgOut = om.readValue(jsonFromFront, Message.class);

        System.out.println("MSG_OUT = " + msgOut.parseEnvelope().toString());

    }

    @Test
    public void test2_TEST_DESC() throws Exception{
        UserDataSet user = new UserDataSet();
        user.setName("qwe rty");
        user.setAge(34);
        user.setPhones(new ArrayList<>());
        user.getPhones().add(new PhoneDataSet());
        user.getPhones().get(0).setPhone("1234567890");

        Message msgIn = new Message(new Address("FROM-DB"), new Address("TO-FRONT"), new UserAddRq(user));

        ObjectMapper om = new ObjectMapper();

        String jsonFromFront = om.writerWithDefaultPrettyPrinter().writeValueAsString(msgIn);

        System.out.println("MSG_IN = " + jsonFromFront);

        Message msgOut = om.readValue(jsonFromFront, Message.class);

        System.out.println("MSG_OUT = " + msgOut.parseEnvelope().toString());
    }

}
