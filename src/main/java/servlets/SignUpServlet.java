package servlets;

import dbService.DBException;
import dbService.dao.UsersDAOhibernate;
import dbService.dao.UsersDAOjdbc;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SignUpServlet extends HttpServlet {

//    private final UsersDAOjdbc dao = new UsersDAOjdbc();
    private final UsersDAOhibernate dao = new UsersDAOhibernate();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {

            if (login.contentEquals("") || login.contentEquals("")) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println("Error!\nEmpty fields found\nPlease try again.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            } else {
                dao.insertUser(login, password);

                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println("Success! Created new account : Login = " + login + " Password = "
                        + password);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
