package sirs.user.service;

import io.grpc.ManagedChannel;
import proto.bombappetit.BombAppetitGrpc;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.ProtocolStringList;

public class BombAppetit {

    private ManagedChannel channel;

    private BombAppetitGrpc.BombAppetitBlockingStub stub;

    private final String user;

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

    public void sendReview(String restaurantJson, String restaurantName) {
        proto.bombappetit.BombAppetitOuterClass.SendReviewRequest request = proto.bombappetit.BombAppetitOuterClass.SendReviewRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setRestaurantJson(restaurantJson)
            .build();

        stub.sendReview(request);
    }

    public void useVoucher(String voucherJson, String restaurantName) {
        proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest request = proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setVoucherJson(voucherJson)
            .setUser(user)
            .build();

        stub.useVoucher(request);
    }

    public void sendVoucherToOtherUser(String destUser, String voucherJson, String restaurantName) {
        proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest request = proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setVoucherJson(voucherJson)
            .setDestUser(destUser)
            .setUser(user)
            .build();

        System.out.println(stub.sendVoucher(request));
    }

    
    public ArrayList<String> listAllRestaurants() {

        var restaurants = getAllRestaurantsJson();
        var restaurantNames = new ArrayList<String>();

        System.out.println("\nRestaurants Available:");
        for (String restaurant : restaurants) {
            var restaurantName = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").get("restaurant").getAsString();
            System.out.println("- " + restaurantName);
            restaurantNames.add(restaurantName);
        }
        return restaurantNames;

    }

    public void getRestaurantMenu(String restaurantName) {
        var restaurant = getRestaurantJson(restaurantName);
        if (restaurant.equals("")) {
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
        if (restaurant.equals("")) {
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
        if (restaurant.equals("")) {
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

    public void doReviewOfRestaurant(String restaurantName) {
        System.out.println("\nUser: " + user);
        System.out.print("Rating (0-5): ");
        int rating = Integer.parseInt(System.console().readLine());
        System.out.print("Comment: ");
        String comment = System.console().readLine();

        var clientRestaurant = getRestaurantJson(restaurantName);
        if (clientRestaurant.equals("")) {
            return;
        }
        // add a review inside json string
        JsonObject restaurant = JsonParser.parseString(clientRestaurant).getAsJsonObject();
        JsonObject restaurantInfo = restaurant.getAsJsonObject("restaurantInfo");
        JsonArray reviews = restaurantInfo.getAsJsonArray("reviews");
        JsonObject review = new JsonObject();
        review.addProperty("user", user);
        review.addProperty("rating", rating);
        review.addProperty("comment", comment);
        reviews.add(review);
        restaurantInfo.add("reviews", reviews);
        String restaurantJson = restaurant.toString();
        System.out.println(restaurantJson);
        // send json string to server
        sendReview(restaurantJson, restaurantName);
    }


    public String buildVoucherJson(String restaurantName, String voucherCode) {
        var clientRestaurant = getRestaurantJson(restaurantName);
        if (clientRestaurant.equals("")) {
            return "";
        }
        // send voucher code to server
        JsonObject restaurant = JsonParser.parseString(clientRestaurant).getAsJsonObject();
        JsonObject restaurantInfo = restaurant.getAsJsonObject("restaurantInfo");
        // remove everything from restaurant info except vouchers
        restaurantInfo.remove("owner");
        restaurantInfo.remove("menu");
        restaurantInfo.remove("address");
        restaurantInfo.remove("genre");
        restaurantInfo.remove("reviews");
        restaurantInfo.remove("restaurant");

        JsonArray vouchers = restaurantInfo.getAsJsonArray("mealVouchers");
        var newVouchers = new JsonArray();
        Boolean found = false;
        for (JsonElement item : vouchers) {
            JsonObject voucher = item.getAsJsonObject();
            String code = voucher.get("code").getAsString();
            if (code.equals(voucherCode)) {
                newVouchers.add(item);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Voucher not found");
            return "";
        }
        restaurantInfo.add("mealVouchers", newVouchers);
        String restaurantJson = restaurant.toString();
        return restaurantJson;
    }

    public void useVoucher(String restaurantName) {
        System.out.print("Voucher code: ");
        String voucherCode = System.console().readLine();

        var voucherJson = buildVoucherJson(restaurantName, voucherCode);
        if (voucherJson.equals("")) {
            return;
        }

        // send json string to server
        useVoucher(voucherJson, restaurantName);
    } 

    public void sendVoucher(String restaurantName) {
        System.out.print("Voucher code: ");
        String voucherCode = System.console().readLine();
        System.out.print("Destination user: ");
        String destUser = System.console().readLine();

        if (destUser.equals(user)) {
            System.out.println("You can't send a voucher to yourself");
            return;
        }

        var voucherJson = buildVoucherJson(restaurantName, voucherCode);
        if (voucherJson.equals("")) {
            return;
        }

        // send json string to server
        sendVoucherToOtherUser(destUser, voucherJson, restaurantName);
    }


    public boolean shutdown() {
        channel.shutdown();
        return true;
    }
}
