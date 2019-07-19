package storage.dao.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.OrderDao;
import storage.domain.Orders;

import javax.xml.crypto.Data;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoImpJdbcTest {

    OrderDao dao;

    @BeforeEach
    void setUp() {
        dao = new OrderDaoImpJdbc();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void findByPk() {

        Orders result = dao.findByPk("001");
        assertEquals("001",result.getId());
        assertEquals(11111111,result.getOrderDate().getTime());
        assertEquals(2,result.getStatus());
        assertEquals(100, result.getTotal());

    }

    @Test
    void findAll() {

        List<Orders> resultSet = dao.findAll();
        assertEquals(1, resultSet.size());
    }

    @Test
    void create() {

        Orders orders = new Orders();
        orders.setId("002");
        orders.setOrderDate(new Date(11111111));
        orders.setStatus(3);
        orders.setTotal(500);

        dao.create(orders);

        Orders result = dao.findByPk("002");
        assertNotNull(result);
        assertEquals(500, orders.getTotal());

    }

    @Test
    void modify() {

        Orders orders = new Orders();
        orders.setId("002");
        orders.setOrderDate(new Date(11111111));
        orders.setStatus(3);
        orders.setTotal(1000);

        dao.modify(orders);

        Orders result = dao.findByPk("002");
        assertNotNull(result);
        assertEquals(1000, result.getTotal());

    }

    @Test
    void remove() {

        dao.remove("002");
        Orders result = dao.findByPk("002");
        assertNull(result);

    }
}