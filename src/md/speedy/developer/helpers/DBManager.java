package md.speedy.developer.helpers;

import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class DBManager {

    private static String name;
    private static String password;
    private static String url;
    private static DBManager instance;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }


    /**
     * Set the DataBase username
     * @param incomeName database username
     */
    public void setName(String incomeName) {
        name = incomeName;
    }


    /**
     * Set the DataBase password
     * @param incomePassword database password
     */
    public void setPassword(String incomePassword) {
        password = incomePassword;
    }


    /**
     * Set the DataBase url
     * @param incomeUrl url to database
     */
    public void setUrl(String incomeUrl) {
        url = incomeUrl;
    }


    /**
     * Connect to DataBase
     */
    public void setConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(url, name, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Make a query to database
     * @param query SQL formated query
     * @return a ResultSet object with founded data or null
     */
    public ResultSet query(String query) {
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(String query) {
        int i = 0;
        try {
            i = statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }


    /**
     * Close all connections with the DataBase
     */
    public void disconnect() {
        try {connection.close();} catch (SQLException e) {e.printStackTrace();}
        try {statement.close();} catch (SQLException e) {e.printStackTrace();}
        try {resultSet.close();} catch (SQLException e) {e.printStackTrace();}
    }


    /**
     * Check if a record already exists in database for one column
     * @param table name of table to search in
     * @param column name of the column
     * @param target value of what to search for
     * @return true if the record already exists else false
     */
    public <T> boolean exists(String table, String column, T target) {
        String query = "select * from " + table + " where " + column + "=\"" + target + "\";";
        ResultSet resultSet = query(query);
        boolean exist = false;
        try {
            while (resultSet.next()) {
                String s = resultSet.getString(column);
                if (s.equalsIgnoreCase(String.valueOf(target))) {
                    exist = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }

    public <T> boolean exists(String query) {
        ResultSet resultSet = query(query);
        boolean exist = false;
        try {
            ArrayList<String> objects = new ArrayList<>();
            while (resultSet.next()) {
                objects.add(resultSet.getString("user_id"));
            }
            if (objects.size() > 0) {
                exist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }
}
