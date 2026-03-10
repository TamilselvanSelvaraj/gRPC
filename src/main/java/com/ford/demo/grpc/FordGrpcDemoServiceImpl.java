package com.ford.demo.grpc;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class FordGrpcDemoServiceImpl extends FordGrpcDemoGrpc.FordGrpcDemoImplBase {
    private static final Logger logger = Logger.getLogger(FordGrpcDemoServiceImpl.class.getName());

    @Override
    public void sayFord(RequestFord request, StreamObserver<ResponseFord> responseObserver) {
        logger.info("Ford client request coming into server...");

        // Logic for generate response
        ResponseFord reply = ResponseFord.newBuilder()
                .setMessage("Hello " + request.getName())
                .build();

        responseObserver.onNext(reply); // sending message
        responseObserver.onCompleted(); // closing the stream
    }
}
