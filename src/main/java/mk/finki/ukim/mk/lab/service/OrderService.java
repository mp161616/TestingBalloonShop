package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Balloon;
import mk.finki.ukim.mk.lab.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    Order placeOrder(String username, String deliveryAddress, List<Balloon> balloons, LocalDateTime date);

    List<Order> getPlacedOrdersForUser(String username);

    Integer countAll(String username);
}
