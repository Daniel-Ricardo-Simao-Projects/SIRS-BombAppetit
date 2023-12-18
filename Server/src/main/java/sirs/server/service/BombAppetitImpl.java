package sirs.server.service;

import java.util.ArrayList;

import com.google.gson.JsonParser;
import com.google.gson.annotations.JsonAdapter;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
import proto.bombappetit.BombAppetitOuterClass.SendVoucherRequest;
import proto.bombappetit.BombAppetitOuterClass.SendVoucherResponse;
import proto.bombappetit.BombAppetitOuterClass.UseVoucherRequest;
import proto.bombappetit.BombAppetitOuterClass.UseVoucherResponse;
import sirs.server.ServerState;

public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {

    public static ServerState server;

    public BombAppetitImpl() {
        server = new ServerState();
    }
    
    @Override
    public void allRestaurants(BombAppetitOuterClass.AllRestaurantsRequest request,
            StreamObserver<BombAppetitOuterClass.AllRestaurantsResponse> responseObserver) {

        // grab all restaurants from the database from the user who is requesting
        String user = request.getUser();
        //System.out.println(user);

        ArrayList<String> restaurants = server.getAllRestaurants(user);
            
        BombAppetitOuterClass.AllRestaurantsResponse response = BombAppetitOuterClass.AllRestaurantsResponse
                .newBuilder()
                .addAllRestaurants(restaurants)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void restaurant(BombAppetitOuterClass.RestaurantRequest request,
            StreamObserver<BombAppetitOuterClass.RestaurantResponse> responseObserver) {
        //System.out.println(request);

        // grab the restaurant name from the request and look for it in the database
        String restaurantName = request.getRestaurantName();
        String user = request.getUser();
        String restaurantJson = server.getClientRestaurant(user, restaurantName);
        // System.out.println(restaurantJson);

        BombAppetitOuterClass.RestaurantResponse response = BombAppetitOuterClass.RestaurantResponse
                .newBuilder()
                .setRestaurant(restaurantJson)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendReview(BombAppetitOuterClass.SendReviewRequest request,
            StreamObserver<BombAppetitOuterClass.SendReviewResponse> responseObserver) {

        String restaurantName = request.getRestaurantName();
        String restaurantJson = request.getRestaurantJson();
        //System.out.println(restaurantJson);

        //server.updateRestaurant(user, restaurantName, restaurantJson);
        var reviews = JsonParser.parseString(restaurantJson).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("reviews");

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
        var voucher = JsonParser.parseString(voucherJson).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers").get(0);
        var user = request.getUser();

        var restaurantJson = server.getClientRestaurant(user, restaurantName);

        // add voucher to restaurant
        var restaurant = JsonParser.parseString(restaurantJson).getAsJsonObject();
        var vouchers = restaurant.getAsJsonObject("restaurantInfo").getAsJsonArray("mealVouchers");
        vouchers.remove(voucher);

        server.updateRestaurant(user, restaurantName, restaurant.toString());

        //System.out.println(voucher.getAsJsonObject().get("code").getAsString());
        //server.removeVoucher(user, restaurantName, voucher.getAsJsonObject().get("code").getAsString());

        UseVoucherResponse response = UseVoucherResponse
                .newBuilder()
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendVoucher(SendVoucherRequest request, StreamObserver<SendVoucherResponse> responseObserver) {
        
    }

    

}
