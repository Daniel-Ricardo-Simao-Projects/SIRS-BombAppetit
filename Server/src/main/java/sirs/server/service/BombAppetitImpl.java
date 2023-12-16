package sirs.server.service;

import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
import sirs.server.ServerState;

public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {

    private static ServerState server;

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
        String restaurantJson = server.getRestaurant(user, restaurantName);
        // System.out.println(restaurantJson);

        BombAppetitOuterClass.RestaurantResponse response = BombAppetitOuterClass.RestaurantResponse
                .newBuilder()
                .setRestaurant(restaurantJson)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
