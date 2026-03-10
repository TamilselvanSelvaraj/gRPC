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

// for marshalling and unmarshalling demo
//public class MyServer {
//    private static final Logger logger = LoggerFactory.getLogger(MyServer.class);
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        int port = 50051;
//
//        // using the default bindService
//        GreetingServiceImpl greetingServiceImpl = new GreetingServiceImpl();
//        ServerServiceDefinition greeterServiceDefinition = GreeterGrpc.bindService(greetingServiceImpl);
//
//
//        //FordGrpcDemo Service Setup with Custom Marshaller
//        FordGrpcDemoServiceImpl fordGrpcDemoServiceImpl = new FordGrpcDemoServiceImpl();
//
//        // Get the generated MethodDescriptor for SayFord
//        MethodDescriptor<requestFord, responseFord> sayFordMethod = FordGrpcDemoGrpc.getSayFordMethod();
//
//        // Create a new MethodDescriptor with your custom request marshaller
//        MethodDescriptor<requestFord, responseFord> customSayFordMethod = sayFordMethod.toBuilder()
//                // custom marshaller setup
//                .setRequestMarshaller(new LoggingRequestFordMarshaller())
//                .setResponseMarshaller(ProtoUtils.marshaller(responseFord.getDefaultInstance())) // Use default for response
//                .build();
//
//        // Manually build the ServerServiceDefinition for FordGrpcDemo
//        ServerServiceDefinition fordGrpcDemoServiceDefinition = ServerServiceDefinition.builder(FordGrpcDemoGrpc.SERVICE_NAME)
//                .addMethod(
//                        customSayFordMethod, // Use your method descriptor with the custom marshaller
//                        ServerCalls.asyncUnaryCall(
//                                new FordGrpcDemoGrpc.MethodHandlers<>(fordGrpcDemoServiceImpl, FordGrpcDemoGrpc.METHODID_SAY_FORD)))
//                .build();
//
//
//        // build and start the server
//        Server server = ServerBuilder.forPort(port)
//                // Add the Greeter service
//                .addService(greeterServiceDefinition)
//                // Add the FordGrpcDemo service with custom marshaller
//                .addService(fordGrpcDemoServiceDefinition)
//                .build();
//
//        server.start();
//        logger.info("Server started, listening on {}", port);
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            logger.info("*** shutting down gRPC server since JVM is shutting down");
//            try {
//                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                logger.error("Server shutdown interrupted", e);
//            }
//            logger.info("*** server shut down");
//        }));
//        server.awaitTermination();
//    }
//}
