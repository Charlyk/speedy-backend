package md.speedy.developer;

import md.speedy.developer.helpers.JSONParser;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONObject income = new JSONParser<>().parseRequest(req);
        GoogleAuth googleAuth = new GoogleAuth();
        googleAuth.verify(income.getString("ServerCode"));
        writer.println(income);
    }
}