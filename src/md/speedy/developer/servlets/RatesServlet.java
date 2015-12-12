package md.speedy.developer.servlets;

import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.model.Rate;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eduard Albu on 12.12.15 12 2015
 * project Speedy Backend
 */
public class RatesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONParser parser = new JSONParser();
        JSONObject income = parser.parseRequest(req);
        Rate rate = new Rate();
        writer.println(rate.addRate(income));
    }
}
