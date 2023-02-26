package com.shopping.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;

public class OrderDao {
    private Logger logger = Logger.getLogger(OrderDao.class.getName());

    public ImmutableList<Order> getOrdersByUserId(int userId) {
        Connection connection = null;
        List<Order> orders = new ArrayList<>();
        try {
            logger.info("Getting orders for user id: " + userId);
            connection = H2DatabaseConnection.getConnectionToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ORDERS WHERE USER_ID = ?");
            preparedStatement.setInt(1, userId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setUserId(result.getInt("USER_ID"));
                order.setOrderId(result.getInt("ORDER_ID"));
                order.setNoOfItems(result.getInt("NO_OF_ITEMS"));
                order.setTotalAmount(result.getDouble("TOTAL_AMOUNT"));
                order.setOrderDate(result.getDate("ORDER_DATE"));
                orders.add(order);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while getting orders for user id: " + userId, e);
        }
        return ImmutableList.copyOf(orders);
    }
}
