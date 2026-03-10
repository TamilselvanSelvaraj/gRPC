package com.ford.demo.grpc;

import io.grpc.MethodDescriptor;
import io.grpc.protobuf.ProtoUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LoggingRequestFordMarshaller implements MethodDescriptor.Marshaller<RequestFord> {

    private static final Logger logger = Logger.getLogger(LoggingRequestFordMarshaller.class.getName());
    private final MethodDescriptor.Marshaller<RequestFord> delegate;

    public LoggingRequestFordMarshaller() {
        this.delegate = ProtoUtils.marshaller(RequestFord.getDefaultInstance());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b)); // Uppercase HEX for readability
            sb.append(" ");                       // Space between each byte
        }
        return sb.toString().trim();
    }

    private static String bytesToBinary(byte[] bytes) {
        return IntStream.range(0, bytes.length)
                .mapToObj(i ->
                        String.format("%8s",
                                        Integer.toBinaryString(bytes[i] & 0xFF))  // Mask to unsigned
                                .replace(' ', '0')                         // Pad with leading zeros
                )
                .collect(Collectors.joining(" "));
    }

    private static String buildByteTable(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌────────┬──────────────┬──────────┐\n");
        sb.append(  "│ Byte # │     HEX      │  BINARY  │\n");
        sb.append(  "├────────┼──────────────┼──────────┤\n");
        for (int i = 0; i < bytes.length; i++) {
            String hex    = String.format("0x%02X", bytes[i]);
            String binary = String.format("%8s",
                    Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
            sb.append(String.format("│ %6d │ %-12s │ %s │%n", i, hex, binary));
        }
        sb.append("└────────┴──────────────┴──────────┘");
        return sb.toString();
    }

    @Override
    public InputStream stream(RequestFord value) {
        return delegate.stream(value);
    }

    @Override
    public RequestFord parse(InputStream stream) {
        try {
            // Step 1: Capture all raw bytes before delegation
            byte[] rawBytes = stream.readAllBytes();

            // Step 2: Build hex string (Java 17+ preferred, fallback for older)
            String hexString;
            try {
                // Java 17+ — formats each byte as "0A 0B 0C" style
                hexString = java.util.HexFormat.ofDelimiter(" ")
                        .withUpperCase()
                        .formatHex(rawBytes);
            } catch (NoClassDefFoundError e) {
                hexString = bytesToHex(rawBytes); // Fallback: Java 8-16
            }

            // Step 3: Build binary string
            String binaryString = bytesToBinary(rawBytes);

            // Step 4: Build pretty table
            String byteTable = buildByteTable(rawBytes);

            // Step 5: Log everything clearly
            logger.info(String.format(
                    "%n" +
                            "╔══════════════════════════════════════════════════════╗%n" +
                            "║     SERVER: Incoming RequestFord — RAW WIRE DATA     ║%n" +
                            "╚══════════════════════════════════════════════════════╝%n" +
                            "  Total Bytes : %d%n" +
                            "  HEX         : %s%n" +
                            "  BINARY      : %s%n" +
                            "  Byte Table  : %s",
                    rawBytes.length,
                    hexString,
                    binaryString,
                    byteTable
            ));

            // Step 6: Delegate to the real Protobuf marshaller using captured bytes
            return delegate.parse(new ByteArrayInputStream(rawBytes));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read/log incoming RequestFord stream", e);
            // Last-resort fallback — stream is likely consumed at this point
            return delegate.parse(stream);
        }
    }
}
