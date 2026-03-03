package com.ford.demo.grpc;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        // Logic for generate response
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello " + request.getName())
                .build();

        responseObserver.onNext(reply); // sending message
        responseObserver.onCompleted(); // closing the stream
    }
}
