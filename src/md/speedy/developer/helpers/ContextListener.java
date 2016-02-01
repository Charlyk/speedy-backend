package md.speedy.developer.helpers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String dbUserName = servletContextEvent.getServletContext().getInitParameter("db_username");
        String dbPassword = servletContextEvent.getServletContext().getInitParameter("db_password");
        String dbUrl = servletContextEvent.getServletContext().getInitParameter("db_url");
        DBManager manager = DBManager.getInstance();
        manager.setName(dbUserName);
        manager.setPassword(dbPassword);
        manager.setUrl(dbUrl);
        manager.setConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //manager.disconnect();
    }
}
