package md.speedy.developer.servlets;

import md.speedy.developer.model.Favorites;
import md.speedy.developer.model.Place;

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
        Place place = new Place();
        Favorites favorites = new Favorites();
        favorites.changeReadStatus(userId, placeId);
        writer.println(place.getDetailedPlace(placeId, userId));
    }
}
