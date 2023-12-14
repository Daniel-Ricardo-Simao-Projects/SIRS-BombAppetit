package service;

import io.grpc.ManagedChannel;
import proto.bombappetit.BombAppetitGrpc;

public class BombAppetit {

    private ManagedChannel channel;

    private BombAppetitGrpc.BombAppetitBlockingStub stub;

    private String user;

    public BombAppetit(ManagedChannel channel) {
        this.channel = channel;
        stub = BombAppetitGrpc.newBlockingStub(channel);
        user = "user";
    }

    public boolean sendMessage() {

        proto.bombappetit.BombAppetitOuterClass.BombRequest request = proto.bombappetit.BombAppetitOuterClass.BombRequest
                .newBuilder()
                .setMessage("This is a message")
                .build();

        proto.bombappetit.BombAppetitOuterClass.BombResponse response = stub.bomb(request);

        System.out.println(response);

        return true;
    }
    
    public void listAllRestaurants() {

        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest request = proto.bombappetit.BombAppetitOuterClass.AllRestaurantsRequest
                .newBuilder()
                .setUser(user)
                .build();

        proto.bombappetit.BombAppetitOuterClass.AllRestaurantsResponse response = stub.allRestaurants(request);

        System.out.println("\nRestaurants Available:");
        for (String restaurant : response.getRestaurantsList()) {
            System.out.println("- " + restaurant);
        }

    }
    
    public void getRestaurantInfo(String restaurantName) {

        proto.bombappetit.BombAppetitOuterClass.RestaurantRequest request = proto.bombappetit.BombAppetitOuterClass.RestaurantRequest
                .newBuilder()
                .setRestaurantName(restaurantName)
                .setUser(user)
                .build();

        proto.bombappetit.BombAppetitOuterClass.RestaurantResponse response = stub.restaurant(request);

        System.out.println("\nRestaurant Information:");
        System.out.println(response);
    }

    public boolean shutdown() {
        channel.shutdown();
        return true;
    }
}
