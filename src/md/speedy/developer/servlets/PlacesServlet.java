package md.speedy.developer.servlets;

import md.speedy.developer.models.Place;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 07.12.15 12 2015
 * project Speedy Backend
 */
public class PlacesServlet extends HttpServlet {


    // TODO: add post method and generate an id for each place on register time
    /**
     * Requires an offset starts from 0 and an limit of how many elements you want
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        try {
            int offset = Integer.parseInt(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));
            String userId = req.getParameter("userId");
            ArrayList<Place> places = new Place.Builder().getPlacesFromDB(offset, limit, userId);
            JSONArray jsonPlaces = Place.writeShortPlace(places);
            JSONObject response = new JSONObject().put("Status", true).put("ResponseData", jsonPlaces);
            writer.println(response);
        } catch (Exception e) {
            writer.println(new JSONObject().put("Error", "Something went wrong, please try again").put("Status", false));
        }
    }
}
