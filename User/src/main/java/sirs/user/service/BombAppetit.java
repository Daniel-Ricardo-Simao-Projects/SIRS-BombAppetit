package sirs.user.service;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.grpc.ManagedChannel;
import main.java.secure_document.Check;
import main.java.secure_document.Protect;
import main.java.secure_document.Unprotect;
import proto.bombappetit.BombAppetitGrpc;

public class BombAppetit {

    private ManagedChannel channel;

    private BombAppetitGrpc.BombAppetitBlockingStub stub;

    private final String user;

    private final String KEYPATH = "../Secure-document/inputs/keys/";

    private ArrayList<String> nonces; 

    public BombAppetit(ManagedChannel channel, String user) {
        this.channel = channel;
        this.user = user;
        stub = BombAppetitGrpc.newBlockingStub(channel);
        nonces = new ArrayList<String>();
    }

    public ArrayList<String> getAllRestaurantsJson() throws Exception {

        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest request = proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest
            .newBuilder()
            .setUser(user)
            .build();

        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsResponse response = stub.allRestaurants(request);

        var restListSecure = response.getRestaurantsList();
        var restList = new ArrayList<String>();
        for (var restaurant : restListSecure) {
            var nonce = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").get("nonce").getAsString();
            String[] splitStrings = nonce.split(" ");
            String random = splitStrings[1];
            if (!Check.checkString(restaurant, KEYPATH+"restaurantPub.key", nonces)) {
                nonces.add(random);
                return new ArrayList<String>();
            }
            nonces.add(random);
            var json = Unprotect.unprotectString(restaurant, KEYPATH+user+"Priv.key");
            restList.add(json.toString());
        }
        return restList;
        
    }
    
    
    public String getRestaurantJson(String restaurantName) {
        
        proto.bombappetit.BombAppetitOuterClass.RestaurantRequest request = proto.bombappetit.BombAppetitOuterClass.RestaurantRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setUser(user)
            .build();
        
        proto.bombappetit.BombAppetitOuterClass.RestaurantResponse response = stub.restaurant(request);
        
        var restSecure = response.getRestaurant();
        JsonObject json = new JsonObject();
        try {
            var nonce = JsonParser.parseString(restSecure).getAsJsonObject().getAsJsonObject("restaurantInfo").get("nonce").getAsString();
            String[] splitStrings = nonce.split(" ");
            String random = splitStrings[1];
            if (!Check.checkString(restSecure, KEYPATH+"restaurantPub.key", nonces)) {
                nonces.add(random);
                return json.toString();
            }
            json = Unprotect.unprotectString(restSecure, KEYPATH+user+"Priv.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
        
    }

    public void sendReview(String restaurantJson, String restaurantName) {
        JsonObject json = new JsonObject();
        try {
            json = Protect.protect(restaurantJson, KEYPATH+user+"Priv.key", KEYPATH+"restaurantPub.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
        proto.bombappetit.BombAppetitOuterClass.SendReviewRequest request = proto.bombappetit.BombAppetitOuterClass.SendReviewRequest
            .newBuilder()
            .setUser(user)
            .setRestaurantName(restaurantName)
            .setRestaurantJson(json.toString())
            .build();

        stub.sendReview(request);
    }

    public void useVoucher(String voucherJson, String restaurantName) {
        JsonObject json = new JsonObject();
        try {
            json = Protect.protect(voucherJson, KEYPATH+user+"Priv.key", KEYPATH+"restaurantPub.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
        proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest request = proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setVoucherJson(json.toString())
            .setUser(user)
            .build();

        stub.useVoucher(request);
    }

    public void sendVoucherToOtherUser(String destUser, String voucherJson, String restaurantName) {
        JsonObject json = new JsonObject();
        try {
            json = Protect.protect(voucherJson, KEYPATH+user+"Priv.key", KEYPATH+"restaurantPub.key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest request = proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest
            .newBuilder()
            .setRestaurantName(restaurantName)
            .setVoucherJson(json.toString())
            .setDestUser(destUser)
            .setUser(user)
            .build();

        System.out.println(stub.sendVoucher(request));
    }

    
    public ArrayList<String> listAllRestaurants() {

        var restaurantNames = new ArrayList<String>();
        ArrayList<String> restaurants;
        try {
            restaurants = getAllRestaurantsJson();
        } catch (Exception e) {
            e.printStackTrace();
            return restaurantNames;
        }

        System.out.println("\nRestaurants Available:");
        for (String restaurant : restaurants) {
            var restaurantName = JsonParser.parseString(restaurant).getAsJsonObject().getAsJsonObject("restaurantInfo").get("restaurant").getAsString();
            System.out.println("-> " + restaurantName);
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
        if (rating < 0 || rating > 5) {
            System.out.println("Invalid rating");
            return;
        }
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
