package ru.otus.shw12;

import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddUserServlet extends HttpServlet {

    DBService dbSrv;

    public AddUserServlet(DBService dbSrv){
        this.dbSrv = dbSrv;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("Received = " + req.getParameterMap());
        dbSrv.save(mapParamsToUser(req.getParameterMap()));
        resp.sendRedirect("/browseuser");
    }

    private UserDataSet mapParamsToUser(Map<String, String[]> params){
        UserDataSet user = new UserDataSet();
        user.setName(params.get("name")[0]);
        user.setAge(Integer.parseInt(params.get("age")[0]));
        if(!"".equals(params.get("address")[0])){
            AddressDataSet address = new AddressDataSet();
            address.setStreet(params.get("address")[0]);
            user.setAdress(address);
        }
        List<PhoneDataSet> phones = new ArrayList<>();
        if(!"".equals(params.get("phone1")[0])){
            PhoneDataSet phone = new PhoneDataSet();
            phone.setPhone(params.get("phone1")[0]);
            phone.setUser(user);
            phones.add(phone);
        }
        if(!"".equals(params.get("phone2")[0])){
            PhoneDataSet phone = new PhoneDataSet();
            phone.setPhone(params.get("phone2")[0]);
            phone.setUser(user);
            phones.add(phone);
        }
        if(!"".equals(params.get("phone3")[0])){
            PhoneDataSet phone = new PhoneDataSet();
            phone.setPhone(params.get("phone3")[0]);
            phone.setUser(user);
            phones.add(phone);
        }
        if(phones.size()>0){
            user.setPhones(phones);
        }
        System.out.println("User to insert: " + user);
        return user;
    }

}
