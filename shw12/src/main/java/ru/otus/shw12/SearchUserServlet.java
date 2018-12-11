package ru.otus.shw12;

import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchUserServlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;

    private final DBService dbSrv;

    public SearchUserServlet(DBService dbSrv, TemplateProcessor templateProcessor) {
        this.dbSrv = dbSrv;
        this.templateProcessor = templateProcessor;
    }

    private Map<String, Object> mapUserToMapParameters(UserDataSet user) {
        Map<String, Object> pageParams = new HashMap<>();
        pageParams.put("userId", user.getId());
        pageParams.put("userName", user.getName());
        pageParams.put("userAge", user.getAge());
        pageParams.put("userAddress", user.getAdress().getStreet());
        pageParams.put("userPhones", String.join(", ", user.getPhones().stream()
        .map(PhoneDataSet::getPhone).collect(Collectors.toList())));
        return pageParams;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, String[]> pageVariables = request.getParameterMap();

        System.out.println("Search criteria = " + pageVariables.toString());

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        UserDataSet user = null;
        if(!"".equals(pageVariables.get("id")[0])) {
            System.out.println("search by id");
            user = dbSrv.read(Long.parseLong(pageVariables.get("id")[0]));
        } else {
            if (!"".equals(pageVariables.get("name")[0])) {
                System.out.println("search by name");
                user = dbSrv.read(pageVariables.get("name")[0]);
            }
        }
        if(user == null) {
            response.getWriter().println("FAILED TO FIND USER BY CRITERIA = " + pageVariables.toString());
        } else {
            response.getWriter().println(templateProcessor.
                    getPage("searchresult.html",
                            mapUserToMapParameters(user)));
        }
    }
}
