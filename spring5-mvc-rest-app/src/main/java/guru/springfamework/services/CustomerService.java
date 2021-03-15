package guru.springfamework.services;


import guru.springfamework.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<guru.springfamework.model.CustomerDTO> getAllCustomers();

    guru.springfamework.model.CustomerDTO getCustomerById(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

    CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO);

    CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO);

    void deleteCustomerById(Long id);

}
