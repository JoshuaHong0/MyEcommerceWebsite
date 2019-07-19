package storage.dao.imp;

import com.JoshHong.db.core.JdbcTemplate;
import storage.dao.CustomerDao;
import storage.domain.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerDaoImpJdbc implements CustomerDao {

    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    /**
     * Find a specific customer with id
     *
     * @param pk
     * @return
     */
    @Override
    public Customer findByPk(String pk) {

        List<Customer> list = new ArrayList<>();
        String sql = "select id, name, password, address, phone, birthday from Customers where id = ?";

        jdbcTemplate.query(conn -> {
            PreparedStatement ps  = conn.prepareStatement(sql);
            ps.setString(1, pk);
            return ps;
        }, rs -> populate(list, rs));

        if (list.size() == 1) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Find all customers
     *
     * @return
     */
    @Override
    public List<Customer> findAll() {

        List<Customer> list = new ArrayList<>();
        String sql = "select id, name, password, address, phone, birthday from Customers";

        jdbcTemplate.query(conn -> {
            PreparedStatement ps  = conn.prepareStatement(sql);
            return ps;
        }, rs -> populate(list, rs));

        return list;
    }

    /**
     * Create a new customer in the database
     *
     * @param customer
     */
    @Override
    public void create(Customer customer) {

        String sql = "insert into Customers (id, name, password, address, phone, birthday) values (?,?,?,?,?,?)";

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPassword());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getPhone());
            ps.setLong(6, customer.getBirthday().getTime());
            return ps;
        });
    }

    /**
     * Update the info of a specific customer
     *
     * @param customer
     */
    @Override
    public void modify(Customer customer) {

        String sql = "update Customers set name=?, password=?, address=?,phone=?,birthday=? where id=?";

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPhone());
            ps.setLong(5, customer.getBirthday().getTime());
            ps.setString(6, customer.getId());
            return ps;
        });

    }

    /**
     * Delete the information of a customer from the database
     *
     * @param pk
     */
    @Override
    public void remove(String pk) {

        String sql = "delete from Customers where id=?";

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pk);
            return ps;
        });
    }

    /**
     * Helper: Create a Customer object to hold the info of a customer
     * @param list
     * @param rs
     * @throws SQLException
     */
    private void populate(List<Customer> list, ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString("id"));
        customer.setName(rs.getString("name"));
        customer.setPassword(rs.getString("password"));
        customer.setAddress(rs.getString("address"));
        customer.setPhone(rs.getString("phone"));
        customer.setBirthday(new Date(rs.getLong("birthday")));
        list.add(customer);
    }

}
