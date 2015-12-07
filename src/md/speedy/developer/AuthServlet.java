package md.speedy.developer;

import md.speedy.developer.helpers.AuthController;
import md.speedy.developer.helpers.JSONParser;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<h1 align=\"center\">It's working<h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONObject income = new JSONParser<>().parseRequest(req);
        JSONObject outgoing = new JSONObject();
        User user = new User(income);
        if (AuthController.isUserRegistered(income)) {
            String query = "select * from users where user_id=\"" + user.getId() + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            outgoing.put("NewUser", false);
            outgoing.put("ResponseData", user.getObject(resultSet));
        } else {
            String query = "insert into users (name, photo, email, user_id) values (\""
                    + user.getName() + "\", \""
                    + user.getPhoto() + "\", \""
                    + user.getEmail() + "\", \""
                    + user.getId() + "\");";
            DBManager.getInstance().update(query);
            ResultSet resultSet = DBManager.getInstance().query("select * from users where user_id=\"" + user.getId() + "\";");
            outgoing.put("NewUser", true);
            outgoing.put("ResponseData", user.getObject(resultSet));
        }
        writer.println(outgoing);
    }
}