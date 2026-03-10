package com.ford.demo.grpc;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class GreetingServiceImpl extends GreeterGrpc.GreeterImplBase {
    private static final Logger logger = Logger.getLogger(GreetingServiceImpl.class.getName());

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        logger.info("Greeting client request coming into server...");
        // Logic for generate response
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello " + request.getName())
                .build();

        responseObserver.onNext(reply); // sending message
        responseObserver.onCompleted(); // closing the stream
    }
}
