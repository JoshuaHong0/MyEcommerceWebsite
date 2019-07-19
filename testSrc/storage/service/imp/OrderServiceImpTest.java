package storage.service.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.OrderDao;
import storage.dao.OrderLineItemDao;
import storage.dao.imp.OrderDaoImpJdbc;
import storage.dao.imp.OrderLineItemDaoImpJdbc;
import storage.domain.OrderLineItem;
import storage.domain.Orders;
import storage.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImpTest {

    OrderService orderService;
    OrderDao orderDao;
    OrderLineItemDao lineItemDao;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImp();
        orderDao = new OrderDaoImpJdbc();
        lineItemDao = new OrderLineItemDaoImpJdbc();
    }

    @AfterEach
    void tearDown() {
        orderService = null;
        orderDao = null;
        lineItemDao = null;
    }

    @Test
    void submitOrders() {

        List<Map<String, Object>> cart = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("goodsid", 3l);
        item1.put("quantity", 2);
        cart.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("goodsid", 8l);
        item2.put("quantity", 3);
        cart.add(item2);

        String ordersid = orderService.submitOrders(cart);
        assertNotNull(ordersid);

        Orders orders = orderDao.findByPk(ordersid);
        assertNotNull(orders);
        assertEquals(1, orders.getStatus());
        double total = 3099 * 2 + 1888 * 3;
        assertEquals(total, orders.getTotal());

        List<OrderLineItem> list = lineItemDao.findAll();
        List<OrderLineItem> orderLineItemList = new ArrayList<>();
        for (OrderLineItem lineItem: list) {
            if (lineItem.getOrders().getId().equals(ordersid)) {
                orderLineItemList.add(lineItem);
                if (lineItem.getGoods().getId() == 3L) {
                    assertEquals(2,lineItem.getQuantity());
                    assertEquals(2*3099,lineItem.getSubTotal());
                }
            }
        }
        assertEquals(2,orderLineItemList.size());
    }
}