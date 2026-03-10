package com.ford.demo.grpc;

import io.grpc.*;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.ServerCalls;
import io.grpc.protobuf.ProtoUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MyServer {
    private static final Logger logger = Logger.getLogger(MyServer.class.getName());

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(new GreetingServiceImpl())
                .addService(new FordGrpcDemoServiceImpl())
                .build();

        logger.info("Server started on port 9090...");
        server.start();
        server.awaitTermination();
    }
}