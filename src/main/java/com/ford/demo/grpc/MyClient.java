package com.ford.demo.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MyClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloReply response = stub.sayHello(HelloRequest.newBuilder().setName("Team").build());

        System.out.println(response.getMessage());
        channel.shutdown();
    }
}
