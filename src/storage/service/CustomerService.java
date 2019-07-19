package storage.service;

import storage.domain.Customer;

public interface CustomerService {

    /**
     * Handling login attempts
     *
     * @param customer
     * @return
     */
    boolean login(Customer customer);

    /**
     * Handling register attempts
     *
     * @param customer
     * @throws ServiceException
     */
    void register(Customer customer) throws ServiceException;
}
