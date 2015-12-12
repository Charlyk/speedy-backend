package md.speedy.developer.servlets;

import md.speedy.developer.model.History;
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
public class HistoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        String userId = req.getParameter("userId");
        try {
            int offset = Integer.parseInt(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));
            History history = new History();
            writer.println(history.getHistory(userId, offset, limit));
        } catch (Exception e) {
            writer.println(new JSONObject().put("Status", false).put("Error", e.getMessage()));
        }
    }
}
