package md.speedy.developer.servlets;

import md.speedy.developer.helpers.AuthController;
import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.model.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eduard Albu on 07.12.15 12 2015
 * project Speedy Backend
 */
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONParser<User> parser = new JSONParser<>();
        JSONObject income = parser.parseRequest(req);
        JSONObject outgoing = AuthController.getUserData(income);
        writer.println(outgoing);
    }
}
