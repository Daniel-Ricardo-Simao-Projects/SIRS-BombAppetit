package service;

import io.grpc.ManagedChannel;
import proto.bombappetit.BombAppetitGrpc;

public class BombAppetit {

    private ManagedChannel channel;

    private BombAppetitGrpc.BombAppetitBlockingStub stub;

    public BombAppetit(ManagedChannel channel) {
        this.channel = channel;
        stub = BombAppetitGrpc.newBlockingStub(channel);
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
    
    public void getRestaurants() {
        
        proto.bombappetit.BombAppetitOuterClass.ListRestaurantsRequest request = proto.bombappetit.BombAppetitOuterClass.ListRestaurantsRequest
                .newBuilder()
                .build();

        proto.bombappetit.BombAppetitOuterClass.ListRestaurantsResponse response = stub.listRestaurants(request);

        System.out.println("\nRestaurants Available:");
        for (String restaurant : response.getRestaurantsList()) {
            System.out.println("- " + restaurant);
        }

    }

    public boolean shutdown() {
        channel.shutdown();
        return true;
    }
}
