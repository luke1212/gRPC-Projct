package com.shopping.service;

import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.util.Timestamps;
import com.shopping.db.OrderDao;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    private OrderDao orderDao = new OrderDao();

    @Override
    public void getOrdersForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        logger.info("Getting orders for user id: " + request.getUserId());
        responseObserver.onNext(OrderResponse.newBuilder()
                .addAllOrders(orderDao.getOrdersByUserId(request.getUserId()).stream()
                        .map(order -> Order.newBuilder()
                                .setOrderId(order.getOrderId())
                                .setNoOfItems(order.getNoOfItems())
                                .setTotalAmount(order.getTotalAmount())
                                .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                                .build())
                        .collect(ImmutableList.toImmutableList()))
                .build());
        responseObserver.onCompleted();
    }
}
