package servlets;

import dbService.DBException;
import dbService.dao.UsersDAOhibernate;
import dbService.dao.UsersDAOjdbc;
import dbService.dataSets.UsersDataSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SignInServlet extends HttpServlet {

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
            UsersDataSet user = dao.getUserByLogin(login);

            try {
                if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                    response.setContentType("text/html;charset=utf-8");
                    response.getWriter().println("Authorized: " + login);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setContentType("text/html;charset=utf-8");
                    response.getWriter().println("Unauthorized");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (NullPointerException e) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println("Unauthorized");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
