package storage.dao.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.CustomerDao;
import storage.domain.Customer;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoImpJdbcTest {

    CustomerDao dao;

    @BeforeEach
    void setUp() {
        dao = new CustomerDaoImpJdbc();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void findByPk() {

        // Run below sql in your database first before run this test
        // insert into Customers (id, name, password, address, phone, birthday) values ('007','Josh','123456','Rochester NY','888888',1111111111);

        Customer customer = dao.findByPk("007");
        assertNotNull(customer);
        assertEquals("007", customer.getId());
        assertEquals("Josh", customer.getName());
        assertEquals("123456", customer.getPassword());
        assertEquals("Rochester NY", customer.getAddress());
        assertEquals("888888", customer.getPhone());
        assertEquals(1111111111, customer.getBirthday().getTime());

    }

    @Test
    void findAll() {

        List<Customer> list = dao.findAll();
        assertEquals(list.size(), 1);

    }

    @Test
    void create() {

        Customer customer = new Customer();
        customer.setId("008");
        customer.setName("Max");
        customer.setPassword("123");
        customer.setAddress("NewJersey");
        customer.setPhone("777777");
        customer.setBirthday(new Date(1111111111));
        // Insert into database
        dao.create(customer);
        // Query
        Customer result = dao.findByPk("008");
        assertNotNull(customer);
        assertEquals("008", result.getId());
        assertEquals("Max", result.getName());
        assertEquals("123", result.getPassword());
        assertEquals("NewJersey", result.getAddress());
        assertEquals("777777", result.getPhone());
        assertEquals(1111111111, result.getBirthday().getTime());

    }

    @Test
    void modify() {

        Customer customer = new Customer();
        customer.setId("008");
        customer.setName("Max");
        customer.setPassword("123");
        customer.setAddress("NewJersey");
        customer.setPhone("999999");
        customer.setBirthday(new Date(1111111111));

        dao.modify(customer);

        Customer result = dao.findByPk("008");
        assertNotNull(result);
        assertEquals("999999",result.getPhone());
    }

    @Test
    void remove() {

        dao.remove("008");
        // Query
        Customer result = dao.findByPk("008");
        assertNull(result);

    }
}