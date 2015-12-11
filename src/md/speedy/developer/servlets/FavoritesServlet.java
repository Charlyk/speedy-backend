package md.speedy.developer.servlets;

import md.speedy.developer.helpers.AuthController;
import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.model.Favorites;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eduard Albu on 12/11/15.
 * Speedy-API project
 */
public class FavoritesServlet extends HttpServlet {

    /**
     * Needs userId as url parameter
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        String userId = req.getParameterValues("id")[0];
        if (AuthController.exists(userId)) {
            Favorites favorites = new Favorites();
            writer.println(favorites.build(userId));
        } else {
            writer.println(AuthController.getUnregisteredErrorMessage());
        }
    }

    /**
     * Needs a body that contains the userId, placeId and action (add) to favorites or (delete) from favorites
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONParser parser = new JSONParser();
        JSONObject income = parser.parseRequest(req);
        Favorites favorites = new Favorites();
        String userId = income.getString("userId");
        String placeId = income.getString("placeId");
        String action = income.getString("action");
        if (action.equalsIgnoreCase("add") && AuthController.exists(userId)) {
            writer.println(favorites.addToFavorites(userId, placeId));
        } else if (action.equalsIgnoreCase("delete") && AuthController.exists(userId)) {
            writer.println(favorites.deleteFromFavorites(userId, placeId));
        } else if (!AuthController.exists(userId)){
            writer.println(AuthController.getUnregisteredErrorMessage());
        } else {
            writer.println(new JSONObject().put("Status", false).put("Error", "Please specify a valid method name"));
        }
    }
}
