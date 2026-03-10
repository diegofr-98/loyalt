package com.loyalt.loyalt.service;


import com.loyalt.loyalt.dto.customer.CreateCustomerRequest;
import com.loyalt.loyalt.dto.customer.CreateCustomerResponse;
import com.loyalt.loyalt.exception.ConflictException;
import com.loyalt.loyalt.model.entity.Customer;
import com.loyalt.loyalt.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CreateCustomerResponse createCustomer (CreateCustomerRequest request){

        if(customerRepository.existsByEmail(request.email())){
            throw new ConflictException("This email is already registered");
        }

        Customer customer = new Customer();
        customer.setEmail(request.email().toLowerCase().trim());
        customer.setPhoneNumber(request.phoneNumber());

        Customer saved = customerRepository.save(customer);
        return new CreateCustomerResponse(saved.getUuid(), saved.getEmail(), saved.getPhoneNumber());



    }
}
