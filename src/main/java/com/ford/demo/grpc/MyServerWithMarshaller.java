package com.ford.demo.grpc;

import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServerCallHandler;
import io.grpc.stub.ServerCalls;

import java.util.logging.Logger;

public class MyServerWithMarshaller {


    private static final Logger logger = Logger.getLogger(MyServerWithMarshaller.class.getName());

    public static void main(String[] args) throws Exception {

        // ── Step 1: Get the original generated MethodDescriptor for SayFord ─────
        MethodDescriptor<RequestFord, ResponseFord> originalMethod =
                FordGrpcDemoGrpc.getSayFordMethod();

        // ── Step 2: Rebuild it with your custom request marshaller ───────────────
        MethodDescriptor<RequestFord, ResponseFord> customMethod =
                originalMethod.toBuilder()
                        .setRequestMarshaller(new LoggingRequestFordMarshaller()) // 👈 KEY LINE
                        // Leave response marshaller as default (Protobuf standard)
                        .build();

        // ── Step 3: Create the service impl ──────────────────────────────────────
        FordGrpcDemoServiceImpl fordImpl = new FordGrpcDemoServiceImpl();

        // ── Step 4: Build the ServerCallHandler (unary RPC) ──────────────────────
        ServerCallHandler<RequestFord, ResponseFord> callHandler =
                ServerCalls.asyncUnaryCall(
                        (request, responseObserver) -> fordImpl.sayFord(request, responseObserver)
                );

        // ── Step 5: Build the ServerServiceDefinition with the custom method ──────
        ServerServiceDefinition fordServiceDefinition =
                ServerServiceDefinition.builder(FordGrpcDemoGrpc.SERVICE_NAME)
                        .addMethod(customMethod, callHandler)  // 👈 Custom method wired here
                        .build();

        // ── Step 6: Build and start the server ───────────────────────────────────
        Server server = ServerBuilder.forPort(9090)
                .addService(new GreetingServiceImpl())   // Standard marshaller (no changes needed)
                .addService(fordServiceDefinition)        // Custom marshaller applied here
                .build();

        logger.info("MyServerWithMarshaller started on port 9090...");
        server.start();
        server.awaitTermination();
    }
}
