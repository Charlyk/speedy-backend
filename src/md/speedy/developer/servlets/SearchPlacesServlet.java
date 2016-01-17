package md.speedy.developer.servlets;

import md.speedy.developer.helpers.SearchController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class SearchPlacesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String filter = req.getParameter("filter");
        PrintWriter writer = resp.getWriter();
        writer.println(SearchController.search(filter));
    }
}
