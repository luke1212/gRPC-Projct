package com.server;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shopping.service.OrderServiceImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class OrderServer {
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());
    private Server server;

    public void startServer() throws InterruptedException {
        try {
            server = ServerBuilder.forPort(50052)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();
            logger.log(Level.INFO, "Server started, listening on " + server.getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.log(Level.INFO, "*** Clean server shutting down ***");
                try {
                    stopServer();
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Server did shut down interuppted", e);
                }
                logger.log(Level.INFO, "*** Clean server shutdown in case JVM was shut down ***");
            }));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server did not start", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final OrderServer orderServer = new OrderServer();
        orderServer.startServer();
        orderServer.blockUntilShutdown();
    }
}
