package com.shopping.client;

import com.google.common.collect.ImmutableList;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;

import io.grpc.Channel;

// It is a simple class that contains a single instance variable for storing a reference to the blocking stub of the OrderServiceGrpc client
// create a channel on order server then grab the stub then call method on stub
public class OrderClient {

    // Instance variable for storing a reference to the blocking stub of the OrderServiceGrpc client 
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    // Constructor for creating an OrderClient with the given channel
    public OrderClient(Channel channel) {
        orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    // Method for getting the list of Orders associated with the given userId 
    public ImmutableList<Order> getOrders(int userId) {
        // Create an OrderRequest object containing the userId
        OrderRequest orderRequest = OrderRequest.newBuilder().setUserId(userId).build();
        // Call the getOrdersForUser() method on the blocking stub, passing in the OrderRequest
        OrderResponse orderResponse = orderServiceBlockingStub.getOrdersForUser(orderRequest);
        // Return an immutable list from the Orders contained in the OrderResponse
        return ImmutableList.copyOf(orderResponse.getOrdersList());
    }
}

