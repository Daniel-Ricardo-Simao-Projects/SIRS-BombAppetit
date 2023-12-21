package sirs.server.service;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
import proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest;
import proto.bombappetit.BombAppetitOuterClass.SendVoucherResponse;
import proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest;
import proto.bombappetit.BombAppetitOuterClass.UseVoucherResponse;
import sirs.server.ServerState;
import main.java.secure_document.*;


public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {

    public static ServerState server;

    private final String KEYPATH = "../Secure-document/inputs/keys/";

    private ArrayList<String> nonces;

    public BombAppetitImpl() {
        server = new ServerState();
        nonces = new ArrayList<String>();
    }
    
    @Override
    public void allRestaurants(BombAppetitOuterClass.AllRestaurantsRequest request,
            StreamObserver<BombAppetitOuterClass.AllRestaurantsResponse> responseObserver) {

        // grab all restaurants from the database from the user who is requesting
        String user = request.getUser();

        ArrayList<String> restaurantsProtect = new ArrayList<>();
        var restaurants = server.getAllRestaurants(user);
        try {
            for (var restaurant : restaurants) {
                var json = Protect.protect(restaurant, KEYPATH+"restaurantPriv.key", KEYPATH+user+"Pub.key");
                restaurantsProtect.add(json.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
            
        BombAppetitOuterClass.AllRestaurantsResponse response = BombAppetitOuterClass.AllRestaurantsResponse
                .newBuilder()
                .addAllRestaurants(restaurantsProtect)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void restaurant(BombAppetitOuterClass.RestaurantRequest request,
            StreamObserver<BombAppetitOuterClass.RestaurantResponse> responseObserver) {

        // grab the restaurant name from the request and look for it in the database
        String restaurantName = request.getRestaurantName();
        String user = request.getUser();
        String restaurantJson = server.getClientRestaurant(user, restaurantName);
        JsonObject json = new JsonObject();
        try {
			json = Protect.protect(restaurantJson, KEYPATH+"restaurantPriv.key", KEYPATH+user+"Pub.key");
		} catch (Exception e) {
			e.printStackTrace();
		}


        BombAppetitOuterClass.RestaurantResponse response = BombAppetitOuterClass.RestaurantResponse
                .newBuilder()
                .setRestaurant(json.toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendReview(BombAppetitOuterClass.SendReviewRequest request,
            StreamObserver<BombAppetitOuterClass.SendReviewResponse> responseObserver) {

        String restaurantName = request.getRestaurantName();
        String restaurantJson = request.getRestaurantJson();
        String user = request.getUser();
        JsonObject json = new JsonObject();
        try {
            var nonce = JsonParser.parseString(restaurantJson).getAsJsonObject().getAsJsonObject("restaurantInfo").get("nonce").getAsString();
            String[] splitStrings = nonce.split(" ");
            String random = splitStrings[1];
            if (!Check.checkString(restaurantJson, KEYPATH+user+"Pub.key", nonces)) {
                nonces.add(random);
                return;
            }
            nonces.add(random);
            json = Unprotect.unprotectString(restaurantJson, KEYPATH+"restaurantPriv.key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        var reviews = JsonParser.parseString(json.toString()).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("reviews");

        server.updateAllRestaurantReviews(restaurantName, reviews.toString());

        BombAppetitOuterClass.SendReviewResponse response = BombAppetitOuterClass.SendReviewResponse
                .newBuilder()
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void useVoucher(UseVoucherRequest request, StreamObserver<UseVoucherResponse> responseObserver) {
        
        var restaurantName = request.getRestaurantName();
        var voucherJson = request.getVoucherJson();
        var user = request.getUser();

        var restaurantJson = server.getClientRestaurant(user, restaurantName);

        JsonObject voucherUnprotect = new JsonObject();
        try {
            var nonce = JsonParser.parseString(voucherJson).getAsJsonObject().getAsJsonObject("restaurantInfo").get("nonce").getAsString();
            String[] splitStrings = nonce.split(" ");
            String random = splitStrings[1];
            if (!Check.checkString(voucherJson, KEYPATH+user+"Pub.key", nonces)) {
                nonces.add(random);
                return;
            }
            nonces.add(random);
            voucherUnprotect = Unprotect.unprotectString(voucherJson, KEYPATH+"restaurantPriv.key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        var voucher = JsonParser.parseString(voucherUnprotect.toString()).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers").get(0);


        // add voucher to restaurant
        var restaurant = JsonParser.parseString(restaurantJson).getAsJsonObject();
        var vouchers = restaurant.getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers");
        vouchers.remove(voucher);

        server.updateRestaurant(user, restaurantName, restaurant.toString());


        UseVoucherResponse response = UseVoucherResponse
                .newBuilder()
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendVoucher(SendVoucherRequest request, StreamObserver<SendVoucherResponse> responseObserver) {
        
        var user = request.getUser();
        var destUser = request.getDestUser();
        var restaurantName = request.getRestaurantName();
        var voucherJson = request.getVoucherJson();
        
        SendVoucherResponse response;
        
        JsonObject voucherUnprotect = new JsonObject();
        try {
            var nonce = JsonParser.parseString(voucherJson).getAsJsonObject().getAsJsonObject("restaurantInfo").get("nonce").getAsString();
            String[] splitStrings = nonce.split(" ");
            String random = splitStrings[1];
            if (!Check.checkString(voucherJson, KEYPATH+user+"Pub.key", nonces)) {
                nonces.add(random);
                return;
            }
            nonces.add(random);
            voucherUnprotect = Unprotect.unprotectString(voucherJson, KEYPATH+"restaurantPriv.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
        var voucher = JsonParser.parseString(voucherUnprotect.toString()).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers").get(0);
        
        var validUsers = server.getUsers();
        if (!validUsers.contains(destUser)) {
            response = SendVoucherResponse
                .newBuilder()
                .setResponse("Invalid user")
                .build(); 
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        var userRestaurantJson = server.getClientRestaurant(user, restaurantName);
        var restaurant = JsonParser.parseString(userRestaurantJson).getAsJsonObject();
        var vouchers = restaurant.getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers");
        vouchers.remove(voucher);
        server.updateRestaurant(user, restaurantName, restaurant.toString());

        var destUserRestaurantJson = server.getClientRestaurant(destUser, restaurantName);
        restaurant = JsonParser.parseString(destUserRestaurantJson).getAsJsonObject();
        vouchers = restaurant.getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers");
        vouchers.add(voucher);
        server.updateRestaurant(destUser, restaurantName, restaurant.toString());
    
        
        response = SendVoucherResponse
                .newBuilder()
                .setResponse("Voucher sent")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    

}
