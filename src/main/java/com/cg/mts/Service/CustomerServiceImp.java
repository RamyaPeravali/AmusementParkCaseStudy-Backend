package com.cg.mts.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.mts.Exception.CustomerNotFoundException;
import com.cg.mts.Repository.ICustomerRepository;
import com.cg.mts.Repository.ILoginRepository;
import com.cg.mts.dto.CustomerDto;
import com.cg.mts.entities.Customer;
import com.cg.mts.entities.Login;
import com.cg.mts.util.CustomerConstants;
import com.cg.mts.util.ValidateConstants;

@Service
public class CustomerServiceImp implements ICustomerService {

	@Autowired
	ICustomerRepository customerRepository;
	
	@Autowired
	ILoginRepository loginRepository;
	
	/*
	 * Method Name : validateCustomer Parameter : customerDto Return Type : boolean
	 * Author Name: Jyothsna G Created Date : 28-05-2021
	 */
	public boolean validateCustomer(CustomerDto customerDto) throws CustomerNotFoundException {

		if (!customerDto.getUsername().matches(ValidateConstants.USERNAME_PATTERN))
			throw new CustomerNotFoundException(CustomerConstants.CUSTOMER_CANNOT_BE_EMPTY);
		if (!customerDto.getEmail().matches(ValidateConstants.EMAIL_PATTERN))
			throw new CustomerNotFoundException(CustomerConstants.USEREMAIL_CANNOT_BE_EMPTY);
		if (!customerDto.getMobileNumber().matches(ValidateConstants.MOBILENUMBER_PATTERN))
			throw new CustomerNotFoundException(CustomerConstants.INVALID_PHONE);

		return true;
	}
	/*
	 * Method Name : insertCustomer Parameter : customerDto Return Type : Customer
	 * Author Name: Jyothsna G Created Date : 23-05-2021
	 */

	@Override
	public Customer insertCustomer(CustomerDto customerDto) throws CustomerNotFoundException {
		validateCustomer(customerDto);
		Customer cust = new Customer();
		Optional<Customer> findById = customerRepository.findById(customerDto.getUserId());
		if (findById.isPresent())
			throw new CustomerNotFoundException(CustomerConstants.CUSTOMER_EXISTS);
		cust.setUserId(customerDto.getUserId());
		cust.setUsername(customerDto.getUsername());
		cust.setPassword(customerDto.getPassword());
		cust.setMobileNumber(customerDto.getMobileNumber());
		cust.setEmail(customerDto.getEmail());
		cust.setAddress(customerDto.getAddress());
		Login login = new Login();
		login.setPassword(customerDto.getPassword());
		login.setUserName(customerDto.getUsername());
		login.setRole(customerDto.getRole());
		Customer customer1 = customerRepository.save(cust);
		login.setUserId(customer1.getUserId());
		loginRepository.save(login);
		return customer1;
	}

	/*
	 * Method Name : updateCustomer Parameter : customerDto Return Type : Customer
	 * Author Name: Jyothsna G Created Date : 26-05-2021
	 */
	@Override
	public Customer updateCustomer(CustomerDto customerDto) throws CustomerNotFoundException {
		Optional<Customer> optCustomer = customerRepository.findById(customerDto.getUserId());

		if (!optCustomer.isPresent())
			throw new CustomerNotFoundException(CustomerConstants.CUSTOMER_NOT_FOUND);
		Customer customer = optCustomer.get();
		customer.setUserId(customerDto.getUserId());
		customer.setUsername(customerDto.getUsername());
		customer.setPassword(customerDto.getPassword());
		customer.setMobileNumber(customerDto.getMobileNumber());
		customer.setEmail(customerDto.getEmail());
		customer.setAddress(customerDto.getAddress());
		customer.setRole(customerDto.getRole());
		return customerRepository.save(customer);

	}

	/*
	 * Method Name : viewCustomers Parameter : No Parameter Return Type : List
	 * Author Name: Jyothsna G Created Date : 23-05-2021
	 */
	@Override
	public List<Customer> viewCustomers() throws CustomerNotFoundException {
		List<Customer> customerlist = customerRepository.findAll();
		if (customerlist.isEmpty())
			throw new CustomerNotFoundException(CustomerConstants.CUSTOMER_IS_EMPTY);
		return customerlist;
	}
	
	@Override
	public Customer viewCustomerById(Integer userId) throws CustomerNotFoundException {
		Optional<Customer> optcustomer = customerRepository.findById(userId);
		if (!optcustomer.isPresent()) {
			throw new CustomerNotFoundException(CustomerConstants.CUSTOMER_INVALID_ID);

		}
		return optcustomer.get();
	}

}