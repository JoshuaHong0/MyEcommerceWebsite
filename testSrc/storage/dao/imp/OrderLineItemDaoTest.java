package storage.dao.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.OrderLineItemDao;
import storage.domain.Goods;
import storage.domain.OrderLineItem;
import storage.domain.Orders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemDaoTest {

    OrderLineItemDao dao;

    @BeforeEach
    void setUp() {
       dao = new OrderLineItemDaoImpJdbc();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void findByPk() {

        OrderLineItem lineItem = dao.findByPk(1);
        assertNotNull(lineItem);
        assertEquals(1, lineItem.getGoods().getId());
        assertEquals("001",lineItem.getOrders().getId());
        assertEquals(500,lineItem.getQuantity());
        assertEquals(10,lineItem.getSubTotal());

    }

    @Test
    void findAll() {

        List<OrderLineItem> list = dao.findAll();
        assertEquals(1, list.size());

    }

    @Test
    void create() {

        OrderLineItem item = new OrderLineItem();

        item.setId(2);
        item.setQuantity(999);
        item.setSubTotal(11);

        Orders orders = new Orders();
        orders.setId("001");
        item.setOrders(orders);

        Goods goods = new Goods();
        goods.setId(1);
        item.setGoods(goods);

        dao.create(item);

        OrderLineItem result = dao.findByPk(2);

        assertEquals(999,result.getQuantity());

    }

    @Test
    void modify() {

        OrderLineItem item = new OrderLineItem();

        item.setId(2);
        item.setQuantity(888);
        item.setSubTotal(11);

        Orders orders = new Orders();
        orders.setId("001");
        item.setOrders(orders);

        Goods goods = new Goods();
        goods.setId(1);
        item.setGoods(goods);

        dao.modify(item);

        OrderLineItem result = dao.findByPk(2);

        assertEquals(888, result.getQuantity());

    }

    @Test
    void remove() {

        dao.remove(2);
        OrderLineItem result = dao.findByPk(2);
        assertNull(result);

    }
}