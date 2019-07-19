package storage.service.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import storage.domain.Customer;
import storage.service.CustomerService;
import storage.service.ServiceException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImpTest {

    CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImp();
    }

    @AfterEach
    void tearDown() {
        customerService = null;
    }

    @Test
    @DisplayName("Login succeed")
    void login1() {
        Customer customer = new Customer();
        customer.setId("007");
        customer.setPassword("123456");
        assertTrue(customerService.login(customer));
        assertEquals("Josh", customer.getName());
        assertEquals("Rochester NY", customer.getAddress());
        assertEquals("888888",customer.getPhone());
        assertEquals(1111111111l, customer.getBirthday().getTime());
    }

    @Test
    @DisplayName("Login failed")
    void login2() {
        Customer customer = new Customer();
        customer.setId("007");
        customer.setPassword("123");
        assertFalse(customerService.login(customer));
    }


    @Test
    @DisplayName("Register succeed")
    void register1() throws ServiceException {

        Customer customer = new Customer();
        customer.setId("009");
        customer.setName("Hong");
        customer.setPassword("123456");
        customer.setAddress("Rochester NY");
        customer.setPhone("999999");
        customer.setBirthday(new Date(2222222222l));
        customerService.register(customer);

        Customer testCustomer = new Customer();
        testCustomer.setId("009");
        testCustomer.setPassword("123456");

        assertTrue(customerService.login(customer));
        assertEquals("Hong", customer.getName());
        assertEquals("Rochester NY", customer.getAddress());
        assertEquals("999999",customer.getPhone());
        assertEquals(2222222222l, customer.getBirthday().getTime());


    }

    @Test
    @DisplayName("Register failed - UserID exists")
    void register2() {

        Customer customer = new Customer();
        customer.setId("009");
        customer.setPassword("123456");

        assertThrows(ServiceException.class, ()->{
           customerService.register(customer);
        });

    }
}