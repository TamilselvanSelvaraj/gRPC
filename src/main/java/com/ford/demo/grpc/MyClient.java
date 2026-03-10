package com.ford.demo.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

public class MyClient {
    private static final Logger logger = Logger.getLogger(MyClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        // This stub acts as a local proxy for the remote service
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloReply response = stub.sayHello(HelloRequest.newBuilder().setName("Team").build());

        logger.info("LOGGER GREETER -> " + response.getMessage());

        // for demo ford service
        FordGrpcDemoGrpc.FordGrpcDemoBlockingStub stubFord = FordGrpcDemoGrpc.newBlockingStub(channel);
        ResponseFord responseFord = stubFord.sayFord(RequestFord.newBuilder().setName("Ford Team").build());

        logger.info("LOGGER FORD -> " + responseFord.getMessage());

        channel.shutdown();
    }
}
