package proto.bombappetit;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: BombAppetit.proto")
public final class BombAppetitGrpc {

  private BombAppetitGrpc() {}

  public static final String SERVICE_NAME = "BombAppetit";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.bombappetit.BombAppetitOuterClass.BombRequest,
      proto.bombappetit.BombAppetitOuterClass.BombResponse> getBombMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Bomb",
      requestType = proto.bombappetit.BombAppetitOuterClass.BombRequest.class,
      responseType = proto.bombappetit.BombAppetitOuterClass.BombResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.bombappetit.BombAppetitOuterClass.BombRequest,
      proto.bombappetit.BombAppetitOuterClass.BombResponse> getBombMethod() {
    io.grpc.MethodDescriptor<proto.bombappetit.BombAppetitOuterClass.BombRequest, proto.bombappetit.BombAppetitOuterClass.BombResponse> getBombMethod;
    if ((getBombMethod = BombAppetitGrpc.getBombMethod) == null) {
      synchronized (BombAppetitGrpc.class) {
        if ((getBombMethod = BombAppetitGrpc.getBombMethod) == null) {
          BombAppetitGrpc.getBombMethod = getBombMethod = 
              io.grpc.MethodDescriptor.<proto.bombappetit.BombAppetitOuterClass.BombRequest, proto.bombappetit.BombAppetitOuterClass.BombResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BombAppetit", "Bomb"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.bombappetit.BombAppetitOuterClass.BombRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.bombappetit.BombAppetitOuterClass.BombResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BombAppetitMethodDescriptorSupplier("Bomb"))
                  .build();
          }
        }
     }
     return getBombMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BombAppetitStub newStub(io.grpc.Channel channel) {
    return new BombAppetitStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BombAppetitBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BombAppetitBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BombAppetitFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BombAppetitFutureStub(channel);
  }

  /**
   */
  public static abstract class BombAppetitImplBase implements io.grpc.BindableService {

    /**
     */
    public void bomb(proto.bombappetit.BombAppetitOuterClass.BombRequest request,
        io.grpc.stub.StreamObserver<proto.bombappetit.BombAppetitOuterClass.BombResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBombMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBombMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.bombappetit.BombAppetitOuterClass.BombRequest,
                proto.bombappetit.BombAppetitOuterClass.BombResponse>(
                  this, METHODID_BOMB)))
          .build();
    }
  }

  /**
   */
  public static final class BombAppetitStub extends io.grpc.stub.AbstractStub<BombAppetitStub> {
    private BombAppetitStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BombAppetitStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BombAppetitStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BombAppetitStub(channel, callOptions);
    }

    /**
     */
    public void bomb(proto.bombappetit.BombAppetitOuterClass.BombRequest request,
        io.grpc.stub.StreamObserver<proto.bombappetit.BombAppetitOuterClass.BombResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBombMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BombAppetitBlockingStub extends io.grpc.stub.AbstractStub<BombAppetitBlockingStub> {
    private BombAppetitBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BombAppetitBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BombAppetitBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BombAppetitBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.bombappetit.BombAppetitOuterClass.BombResponse bomb(proto.bombappetit.BombAppetitOuterClass.BombRequest request) {
      return blockingUnaryCall(
          getChannel(), getBombMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BombAppetitFutureStub extends io.grpc.stub.AbstractStub<BombAppetitFutureStub> {
    private BombAppetitFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BombAppetitFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BombAppetitFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BombAppetitFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.bombappetit.BombAppetitOuterClass.BombResponse> bomb(
        proto.bombappetit.BombAppetitOuterClass.BombRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBombMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BOMB = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BombAppetitImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BombAppetitImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BOMB:
          serviceImpl.bomb((proto.bombappetit.BombAppetitOuterClass.BombRequest) request,
              (io.grpc.stub.StreamObserver<proto.bombappetit.BombAppetitOuterClass.BombResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BombAppetitBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BombAppetitBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.bombappetit.BombAppetitOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BombAppetit");
    }
  }

  private static final class BombAppetitFileDescriptorSupplier
      extends BombAppetitBaseDescriptorSupplier {
    BombAppetitFileDescriptorSupplier() {}
  }

  private static final class BombAppetitMethodDescriptorSupplier
      extends BombAppetitBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BombAppetitMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BombAppetitGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BombAppetitFileDescriptorSupplier())
              .addMethod(getBombMethod())
              .build();
        }
      }
    }
    return result;
  }
}
