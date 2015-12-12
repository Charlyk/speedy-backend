package md.speedy.developer.servlets;

import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.model.Comment;
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
public class CommentsServlet extends HttpServlet {

    /**
     * Needs placeId to get all comments for it
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        Comment comment = new Comment();
        String placeId = req.getParameter("id");
        writer.println(comment.build(placeId));
    }

    /**
     * Needs comment, userId and placeId to add the comment and update unread for all users
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JSONParser parser = new JSONParser();
        JSONObject income = parser.parseRequest(req);
        Comment comment = new Comment();
        writer.println(comment.addComment(income));
    }
}

