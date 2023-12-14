package service;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {
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
    public void listRestaurants(BombAppetitOuterClass.ListRestaurantsRequest request,
            StreamObserver<BombAppetitOuterClass.ListRestaurantsResponse> responseObserver) {
        System.out.println(request);
        BombAppetitOuterClass.ListRestaurantsResponse response = BombAppetitOuterClass.ListRestaurantsResponse.newBuilder()
                .addRestaurants("Dona Maria")
                .addRestaurants("El Sabor Español")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
