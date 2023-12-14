package sirs.server.service;

import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
import sirs.server.ServerState;

public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {

    private static ServerState server;

    @Override
    public void bomb(BombAppetitOuterClass.BombRequest request,
            StreamObserver<BombAppetitOuterClass.BombResponse> responseObserver) {
        System.out.println(request);
        BombAppetitOuterClass.BombResponse response = BombAppetitOuterClass.BombResponse.newBuilder()
                .setMessage("Hello " + request.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void allRestaurants(BombAppetitOuterClass.AllRestaurantsRequest request,
            StreamObserver<BombAppetitOuterClass.AllRestaurantsResponse> responseObserver) {

        // grab all restaurants from the database from the user who is requesting
        String user = request.getUser();
        System.out.println(user);

        server.getAllRestaurants();
            
        BombAppetitOuterClass.AllRestaurantsResponse response = BombAppetitOuterClass.AllRestaurantsResponse
                .newBuilder()
                .addRestaurants("Dona Maria")
                .addRestaurants("El Sabor Español")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void restaurant(BombAppetitOuterClass.RestaurantRequest request,
            StreamObserver<BombAppetitOuterClass.RestaurantResponse> responseObserver) {
        System.out.println(request);

        // grab the restaurant name from the request and look for it in the database
        String restaurantName = request.getRestaurantName();
        String user = request.getUser();
        System.out.println(restaurantName);
        System.out.println(user);

        BombAppetitOuterClass.RestaurantResponse response = BombAppetitOuterClass.RestaurantResponse
                .newBuilder()
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
