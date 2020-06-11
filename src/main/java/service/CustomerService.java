package service;

import model.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> findAll();

    public Customer findById(Integer id);

    public void save(Customer customer);

    public boolean delete(Integer id);
}
