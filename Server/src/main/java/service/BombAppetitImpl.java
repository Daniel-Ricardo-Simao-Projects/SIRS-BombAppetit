package main.java.service;

import io.grpc.stub.StreamObserver;
import proto.bombappetit.BombAppetitGrpc;
import proto.bombappetit.BombAppetitOuterClass;
public class BombAppetitImpl extends BombAppetitGrpc.BombAppetitImplBase {
    @Override
    public void bomb(BombAppetitOuterClass.BombRequest request, StreamObserver<BombAppetitOuterClass.BombResponse> responseObserver) {
        System.out.println(request);
        BombAppetitOuterClass.BombResponse response = BombAppetitOuterClass.BombResponse.newBuilder()
                .setMessage("Hello " + request.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
