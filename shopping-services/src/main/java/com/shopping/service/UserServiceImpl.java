package com.shopping.service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.shopping.client.OrderClient;
import com.shopping.db.User;
import com.shopping.db.UserDao;
import com.shopping.stubs.user.GetUserRequest;
import com.shopping.stubs.user.GetUserResponse;
import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserServiceGrpc;
import com.shopping.stubs.order.Order;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

// UserServiceImpl encapsulates the implementation of the gRPC service which gets User details
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    UserDao userDao = new UserDao();

    // This method is called whenever a client calls the getUser() RPC
    @Override
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {

        // Get an instance of UserDao to fetch user data from the database

        User user = userDao.getDetails(request.getUsername());

        // Build a response object for the user with the required information
        GetUserResponse.Builder userResponseBuilder = GetUserResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(Gender.valueOf(user.getGender()));

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50052")
                .usePlaintext()
                .build();

        OrderClient orderClient = new OrderClient(channel);
        ImmutableList<Order> orders = orderClient.getOrders(userResponseBuilder.getId());
        GetUserResponse userResponse = userResponseBuilder.setNoOfOrders(orders.size()).build();

        try {
            channel.shutdown().awaitTermination(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "cannot shutdown channel", e);
        }

        // Send the response and complete the stream
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
