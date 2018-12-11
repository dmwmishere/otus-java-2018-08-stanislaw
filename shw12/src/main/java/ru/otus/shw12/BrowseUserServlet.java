package ru.otus.shw12;

import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BrowseUserServlet extends HttpServlet {

    private final DBService dbSrv;

    private final TemplateProcessor templateProcessor;

    public BrowseUserServlet(DBService dbSrv, TemplateProcessor templateProcessor){
        this.dbSrv = dbSrv;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        List<UserDataSet> users = dbSrv.readAll();
        System.out.println("User count = " + users.size());
        System.out.println("User = " + users.get(0));

        StringBuilder table = new StringBuilder();

        users.forEach(user -> table.append(mapUserToHtmlTable(user)));

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userCount", users.size());
        pageVariables.put("userinfotable", table.toString());

        String page = templateProcessor.getPage("browseuser.html", pageVariables);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    private String mapUserToHtmlTable(UserDataSet user){
        try {
            return "<tr>" +
                    "<td>" + user.getId() + "</td>" +
                    "<td>" + user.getName() + "</td>" +
                    "<td>" + user.getAge() + "</td>" +
                    "<td>" + user.getAdress().getStreet() + "</td>" +
                    "<td>" + String.join(", ", user.getPhones().stream()
                    .map(PhoneDataSet::getPhone).collect(Collectors.toList())) +
                    "</td>" +
                    "</tr>";
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
