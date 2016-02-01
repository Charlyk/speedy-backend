package md.speedy.developer.servlets;

import md.speedy.developer.helpers.JSONParser;
import md.speedy.developer.model.Comment;
import md.speedy.developer.models.Comments;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
        resp.setContentType("application/json; UTF-8");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        String placeId = req.getParameter("id");
        ArrayList<Comments> comments = new Comments.Builder().getCommentsFromDB(placeId).build();
        writer.println(Comments.writeJSON(comments));
    }

    /**
     * Needs comment, userId and placeId to add the comment and update unread for all users
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; UTF-8");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        JSONParser parser = new JSONParser();
        JSONObject income = parser.parseRequest(req);
        Comments comment = new Comments();
        writer.println(comment.addComment(income));
    }
}

