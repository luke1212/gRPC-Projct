package com.shopping.service;

import com.shopping.db.User;
import com.shopping.db.UserDao;
import com.shopping.stubs.user.GetUserRequest;
import com.shopping.stubs.user.GetUserResponse;
import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserServiceGrpc;

import io.grpc.stub.StreamObserver;

// UserServiceImpl encapsulates the implementation of the gRPC service which gets User details
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    // This method is called whenever a client calls the getUser() RPC
    @Override
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {

        // Get an instance of UserDao to fetch user data from the database
        UserDao userDao = new UserDao();
        User user = userDao.getDetails(request.getUsername());

        // Build a response object for the user with the required information
        GetUserResponse userResponse = GetUserResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(Gender.valueOf(user.getGender()))
                .build();

        // Send the response and complete the stream
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
