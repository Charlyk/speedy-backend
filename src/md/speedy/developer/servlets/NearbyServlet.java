package md.speedy.developer.servlets;

import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.models.NearbyPlaces;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 24.01.16 01 2016
 * project Speedy Backend
 */
public class NearbyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        double latitude = Double.parseDouble(req.getParameter("latitude"));
        double longitude = Double.parseDouble(req.getParameter("longitude"));
        String userId = req.getParameter("userId");
        NearbyPlaces nearbyPlace = new NearbyPlaces(latitude, longitude, userId);
        ArrayList<NearbyPlaces> nearbyPlaces = nearbyPlace.getPlaces();
        writer.println(NearbyPlaces.writePlace(nearbyPlaces));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        JSONObject income = new JSONParser().parseRequest(req);
        double latitude = income.getDouble("latitude");
        double longitude = income.getDouble("longitude");
        String userId = income.getString("userId");
        NearbyPlaces nearbyPlace = new NearbyPlaces(latitude, longitude, userId);
        ArrayList<NearbyPlaces> nearbyPlaces = nearbyPlace.getPlaces();
        writer.println(NearbyPlaces.writePlace(nearbyPlaces));
    }
}
