package md.speedy.developer.servlets;

import md.speedy.developer.model.Place;
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
public class PlacesServlet extends HttpServlet {


    /**
     * Requires an offset starts from 0 and an limit of how many elements you want
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        try {
            int offset = Integer.parseInt(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));
            Place place = new Place();
            writer.println(place.build(offset, limit));
        } catch (Exception e) {
            writer.println(new JSONObject().put("Error", e.getMessage()).put("Status", false));
        }
    }
}
