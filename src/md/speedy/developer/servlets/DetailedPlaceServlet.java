package md.speedy.developer.servlets;

import md.speedy.developer.models.Place;
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
public class DetailedPlaceServlet extends HttpServlet {


    /**
     * Needs the placeId and userId to get all comments for one place
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        String placeId = req.getParameter("placeId");
        String userId = req.getParameter("userId");

        Place place = new Place.Builder().getPlaceDataFromDB(placeId, userId).buildDetailed();
        JSONObject response = new JSONObject();
        JSONObject placeJSON = Place.writeDetailed(place);
        boolean status = placeJSON.optBoolean("Status", true);
        if (status) {
            response.put("Status", true).put("ResponseData", placeJSON);
        } else {
            response.put("Status", false).put("Error", "Something went wrong, please try again");
        }
        writer.println(response);
    }
}
