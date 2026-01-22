package com.ford.demo.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class MyServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(new GreetingServiceImpl())
                .build();

        System.out.println("Server started on port 9090...");
        server.start();
        server.awaitTermination();
    }
}
