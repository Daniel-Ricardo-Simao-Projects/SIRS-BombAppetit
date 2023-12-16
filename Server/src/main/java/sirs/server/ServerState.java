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

    public ArrayList<String> getAllRestaurants() {
        ArrayList<String> restaurants = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.GET_ALL_RESTAURANTS)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String restaurant_name = resultSet.getString("restaurant_name");
                restaurants.add(restaurant_name);
                // Process the retrieved data, e.g., print it
                //System.out.println("User ID: " + userId + ", Username: " + username + ", Created: " + created);
            }
            resultSet.close();
            //System.out.println("Select query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public ArrayList<String> getRestaurantVouchers(String client) {
        ArrayList<String> vouchers = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.GET_RESTAURANT_VOUCHERS)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String restaurant_name = resultSet.getString("restaurant_name");
                vouchers.add(restaurant_name);
                // Process the retrieved data, e.g., print it
                //System.out.println("User ID: " + userId + ", Username: " + username + ", Created: " + created);
            }
            resultSet.close();
            //System.out.println("Select query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vouchers;
    }


    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
