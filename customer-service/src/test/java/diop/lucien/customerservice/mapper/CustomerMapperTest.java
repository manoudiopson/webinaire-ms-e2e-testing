package diop.lucien.customerservice.mapper;

import diop.lucien.customerservice.dto.CustomerDTO;
import diop.lucien.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerMapperTest {
    CustomerMapper underTest = new CustomerMapper();
    @Test
    public void shouldMapCustomerToCustomerDTO(){
        Customer givenCustomer = Customer.builder()
                .id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder()
                .id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        CustomerDTO result = underTest.fromCustomer(givenCustomer);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldMapCustomerToCustomer(){
        CustomerDTO givenCustomerDTO = CustomerDTO.builder()
                .id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();
        Customer expected = Customer.builder()
                .id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build();

        Customer result = underTest.fromCustomerDTO(givenCustomerDTO);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    @Test
    public void shouldMapListOfCustomersToListOfCustomers(){
        List<Customer> givenCustomers = List.of(
                Customer.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                Customer.builder().id(2L).lastName("Artis").firstName("Marie").email("marie.artis53d@gmail.com").build(),
                Customer.builder().id(3L).lastName("NASSALAN").firstName("François").email("françois@gmail.com").build()
        );

        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().id(1L).lastName("DIOP").firstName("Lucien").email("lucienemmanueld@gmail.com").build(),
                CustomerDTO.builder().id(2L).firstName("Marie").lastName("Artis").email("marie.artis53d@gmail.com").build(),
                CustomerDTO.builder().id(3L).lastName("NASSALAN").firstName("François").email("françois@gmail.com").build()
        );
        List<CustomerDTO> result = underTest.fromListCustomers(givenCustomers);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullCustomerToCustomerDTO(){
        Customer givenCustomer = null;

        CustomerDTO expected = CustomerDTO.builder()
                .id(1L).lastName("DIOP").firstName("Lucien").email("luceinemmanueld@gmail.com").build();
        AssertionsForClassTypes.assertThatThrownBy( () -> underTest.fromCustomer(givenCustomer))
                .isInstanceOf(IllegalArgumentException.class);
    }
}