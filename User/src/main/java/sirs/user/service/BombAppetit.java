package sirs.user.service;

import io.grpc.ManagedChannel;
import proto.bombappetit.BombAppetitGrpc;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.ProtocolStringList;

public class BombAppetit {

    private ManagedChannel channel;

    private BombAppetitGrpc.BombAppetitBlockingStub stub;

    private String user;

    public BombAppetit(ManagedChannel channel, String user) {
        this.channel = channel;
        this.user = user;
        stub = BombAppetitGrpc.newBlockingStub(channel);
    }

    public ProtocolStringList getAllRestaurantsJson() {
        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest request = proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest
                .newBuilder()
                .setUser(user)
                .build();

        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsResponse response = stub.allRestaurants(request);
        // System.out.println(response);
        return response.getRestaurantsList();
    }
    
    
    public String getRestaurantJson(String restaurantName) {
        
        proto.bombappetit.BombAppetitOuterClass.RestaurantRequest request = proto.bombappetit.BombAppetitOuterClass.RestaurantRequest
        .newBuilder()
        .setRestaurantName(restaurantName)
        .setUser(user)
        .build();
        
        proto.bombappetit.BombAppetitOuterClass.RestaurantResponse response = stub.restaurant(request);
        
        //System.out.println("\nRestaurant Information:");
        // System.out.println(response);
        
        return response.getRestaurant();
    }
    
    public void listAllRestaurants() {

        var restaurants = getAllRestaurantsJson();

        System.out.println("\nRestaurants Available:");
        for (String restaurant : restaurants) {
            var restaurantName = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").get("restaurant").toString();
            System.out.println("- " + restaurantName);
        }

    }

    public void getRestaurantMenu(String restaurantName) {
        var restaurant = getRestaurantJson(restaurantName);
        if (restaurant == "") {
            return;
        }
        var menu = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("menu");
        System.out.println("\nMenu: ");
        for (JsonElement item : menu) {
            JsonObject menuItem = item.getAsJsonObject();
            String itemName = menuItem.get("itemName").getAsString();
            String category = menuItem.get("category").getAsString();
            String description = menuItem.get("description").getAsString();
            double price = menuItem.get("price").getAsDouble();
            String currency = menuItem.get("currency").getAsString();

            System.out.println("Item Name: " + itemName);
            System.out.println("Category: " + category);
            System.out.println("Description: " + description);
            System.out.println("Price: " + price + " " + currency);
            System.out.println();
        }
    }

    public void getRestaurantReviews(String restaurantName) {
        var restaurant = getRestaurantJson(restaurantName);
        if (restaurant == "") {
            return;
        }
        var reviews = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("reviews");
        System.out.println("\nReviews: ");
        for (JsonElement item : reviews) {
            JsonObject reviewItem = item.getAsJsonObject();
            String user = reviewItem.get("user").getAsString();
            int rating = reviewItem.get("rating").getAsInt();
            String comment = reviewItem.get("comment").getAsString();
        
            System.out.println("User: " + user);
            System.out.println("Rating: " + rating);
            System.out.println("Comment: " + comment);
            System.out.println();
        }
    }

    public void getRestaurantVouchers(String restaurantName) {
        var restaurant = getRestaurantJson(restaurantName);
        if (restaurant == "") {
            return;
        }
        var vouchers = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers");
        System.out.println("\nVouchers: ");
        for (JsonElement item : vouchers) {
            JsonObject vouchersItem = item.getAsJsonObject();
            String code = vouchersItem.get("code").getAsString();
            String description = vouchersItem.get("description").getAsString();

            System.out.println("Code: " + code);
            System.out.println("Description " + description);
            System.out.println();
        }
    }


    public boolean shutdown() {
        channel.shutdown();
        return true;
    }
}
