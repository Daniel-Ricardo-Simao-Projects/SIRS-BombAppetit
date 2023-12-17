package sirs.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.ProtocolStringList;

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
    public String getClientRestaurant(String client, String restaurant) {
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

    // get restaurant json of all clients
    public Map<String, String> getRestaurantJsons(String restaurantName) {
        Map<String, String> restaurants = new HashMap<>();
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.GET_CLIENTS_RESTAURANT)) {
            pstmt.setString(1, restaurantName);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String restaurant = resultSet.getString("restaurant");
                restaurants.put(username, restaurant);
            }
            resultSet.close();
            //System.out.println("Select query executed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public void updateRestaurant(String user, String restaurantName, String RestaurantJson) {
        try (PreparedStatement pstmt = conn.prepareStatement(DBQueries.UPDATE_RESTAURANT)) {
            pstmt.setString(1, RestaurantJson);
            pstmt.setString(2, user);
            pstmt.setString(3, restaurantName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllRestaurantReviews(String restaurantName, String reviews) {
        var maplist = getRestaurantJsons(restaurantName);
        //System.out.println("reviews: " + reviews);
        JsonArray reviewsArray = JsonParser.parseString(reviews).getAsJsonArray();
        //System.out.println(reviewsArray);

        for (var mapEntry : maplist.entrySet()) {
            var restaurantJson = mapEntry.getValue();
            //System.out.println(restaurantJson);
            JsonObject restaurant = JsonParser.parseString(restaurantJson).getAsJsonObject();
            restaurant.getAsJsonObject("restaurantInfo").add("reviews", reviewsArray);
            //System.out.println(restaurant);
            //System.out.println();
            updateRestaurant(mapEntry.getKey(), restaurantName, restaurant.toString());
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
