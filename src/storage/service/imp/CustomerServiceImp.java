package storage.service.imp;

import storage.dao.CustomerDao;
import storage.dao.imp.CustomerDaoImpJdbc;
import storage.domain.Customer;
import storage.service.CustomerService;
import storage.service.ServiceException;

public class CustomerServiceImp implements CustomerService {

    CustomerDao customerDao = new CustomerDaoImpJdbc();

    @Override
    public boolean login(Customer customer) {

        Customer dbCustomer = customerDao.findByPk(customer.getId());

        if (dbCustomer == null) {
            return false;
        }

        if (dbCustomer.getPassword().equals(customer.getPassword())) {
            //Login succeed;
            customer.setName(dbCustomer.getName());
            customer.setBirthday(dbCustomer.getBirthday());
            customer.setPhone(dbCustomer.getPhone());
            customer.setAddress(dbCustomer.getAddress());
            return true;
        }

        return false;
    }

    @Override
    public void register(Customer customer) throws ServiceException {

        Customer dbCustomer = customerDao.findByPk(customer.getId());

        if (dbCustomer != null) {
            // UserID exists
            throw new ServiceException("UserID: " +customer.getId()+" already exists!");
        }

        // Register
         customerDao.create(customer);

    }

}
