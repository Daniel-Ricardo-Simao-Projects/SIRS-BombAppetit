package sirs.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import sirs.server.database.*;

public class ServerState {

    private Connection conn;

    public ServerState() {
        String url = "jdbc:postgresql://192.168.0.30:5432/restaurantsdb?sslmode=verify-full";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        props.setProperty("sslcert", "/home/daniel/user.pem");
        props.setProperty("sslkey", "/home/daniel/user.key.pk8");
        props.setProperty("sslrootcert", "/home/daniel/root.pem");

        try {
            conn = DriverManager.getConnection(url, props);
            System.out.println("connection started: " + conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all restaurant json associated with client
    public ArrayList<String> getAllRestaurants(String client) {
        ArrayList<String> restaurants = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.GET_ALL_RESTAURANTS)) {
            pstmt.setString(1, client);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String restaurant = resultSet.getString("restaurant");
                restaurants.add(restaurant);
            }
            resultSet.close();
            //System.out.println("Select query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // get restaurant json associated with client
    public String getRestaurant(String client, String restaurant) {
        String restaurantJson = "";
        
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.GET_RESTAURANT)) {
            pstmt.setString(1, client);
            pstmt.setString(2, restaurant);
            //System.out.println(pstmt.toString());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                restaurantJson = resultSet.getString("restaurant");
            }
            resultSet.close();
            //System.out.println("Select query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurantJson;
    }


    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
