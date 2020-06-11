package controller;


import com.sun.org.apache.regexp.internal.RE;
import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.CustomerService;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/overview", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> loadAllCustomer() {
        ResponseEntity<List<Customer>> listResponseEntity = null;
        List<Customer> customers;
        try {
            customers = customerService.findAll();
            if (customers.isEmpty()) {
                listResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                listResponseEntity = new ResponseEntity<>(customers, HttpStatus.OK);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return listResponseEntity;
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        HttpHeaders httpHeaders;
        ResponseEntity<Void> create = null;
        try {
            customerService.save(customer);
            httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(ucBuilder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri());
            create = new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return create;
    }

    @RequestMapping(value = "/customer/find/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Integer id) {
        Customer customer;
        ResponseEntity<Customer> find = null;
        try {
            customer = customerService.findById(id);
            if (customer == null) {
                find = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                find = new ResponseEntity<>(customer, HttpStatus.OK);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return find;
    }

    @RequestMapping(value = "/customer/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Integer id, @RequestBody Customer customer) {
        Customer currentCustomer;
        ResponseEntity<Customer> update = null;
        try {
            currentCustomer = customerService.findById(id);
            if (currentCustomer != null) {
                currentCustomer.setId(customer.getId());
                currentCustomer.setFirstName(customer.getFirstName());
                currentCustomer.setLastName(customer.getLastName());
                currentCustomer.setProvinceByProvinceId(customer.getProvinceByProvinceId());

                customerService.save(currentCustomer);

                update = new ResponseEntity<>(currentCustomer, HttpStatus.OK);
            } else {
                update = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return update;
    }

    @RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable("id") Integer id) {
        Customer customer;
        ResponseEntity<Boolean> delete = null;
        boolean flag = false;
        try {
            customer = customerService.findById(id);
            if (customer != null) {
                flag = customerService.delete(id);
                delete = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                delete = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return delete;
    }
}
