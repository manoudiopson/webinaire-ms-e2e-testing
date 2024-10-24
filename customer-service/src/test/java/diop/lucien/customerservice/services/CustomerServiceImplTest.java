package diop.lucien.customerservice.services;

import diop.lucien.customerservice.dto.CustomerDTO;
import diop.lucien.customerservice.entities.Customer;
import diop.lucien.customerservice.execptions.CustomerNotFoundException;
import diop.lucien.customerservice.execptions.EmailAlreadyExistException;
import diop.lucien.customerservice.mapper.CustomerMapper;
import diop.lucien.customerservice.repository.CustomerRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    void shouldSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Reine").lastName("DIOP").email("reine@gmail.com").build();
        Customer customer = Customer.builder().firstName("Reine").lastName("DIOP").email("reine@gmail.com").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("Reine").lastName("DIOP").email("reine@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).firstName("Reine").lastName("DIOP").email("reine@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);
        CustomerDTO result = underTest.saveNewCustomer(customerDTO);
        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldNotSaveNewCustomerWhenEmailExist() {
        CustomerDTO customerDTO = CustomerDTO.builder().
                firstName("Reine").lastName("DIOP").email("xxxxxx@gmail.com").build();
        Customer customer = Customer.builder()
                .id(5L).firstName("Reine").lastName("DIOP").email("xxxxxx@gmail.com").build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        AssertionsForClassTypes.assertThatThrownBy( () -> underTest.saveNewCustomer(customerDTO))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = List.of(
                Customer.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                Customer.builder().id(2L).lastName("Artis").firstName("Marie").email("marie.artis53d@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                CustomerDTO.builder().id(2L).lastName("Artis").firstName("Marie").email("marie.artis53d@gmail.com").build()
        );
        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.getAllCustomers();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldFindCustomerById() {
        Long CustomerId = 1L;
        Customer customer = Customer.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        Mockito.when(customerRepository.findById(CustomerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(expected);
        CustomerDTO result = underTest.findCustomerById(CustomerId);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldNotFindCustomerById() {
        Long CustomerId = 8L;
        Mockito.when(customerRepository.findById(CustomerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.findCustomerById(CustomerId))
                .isInstanceOf(CustomerNotFoundException.class).hasMessage(null);
    }

    @Test
    void shouldSearchCustomers() {
        String keyword = "m";
        List<Customer> customers = List.of(
                Customer.builder().lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                Customer.builder().lastName("Artis").firstName("Marie").email("marie.artis53d@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                CustomerDTO.builder().lastName("Artis").firstName("Marie").email("marie.artis53d@gmail.com").build()
        );
        Mockito.when(customerRepository.findByFirstNameContainingIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.searchCustomers(keyword);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldDeleteCustomer() {
        Long customerId = 1L;
        Customer customer = Customer.builder().id(6L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }

    @Test
    void shouldNotDeleteCustomerIfNotExist(){
        Long customerId = 9L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}