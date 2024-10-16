package diop.lucien.customerservice.repository;

import diop.lucien.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest @ActiveProfiles("test")
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("------------------------------------------------");
        customerRepository.save(Customer.builder().firstName("Lucien")
                .lastName("DIOP").email("lucienemmanueld@gmail.com").build());
        customerRepository.save(Customer.builder().firstName("Marie")
                .lastName("Artis").email("marie.artis53d@gmail.com").build());
        customerRepository.save(Customer.builder().firstName("François")
                .lastName("Nassalan").email("françois@gmail.com").build());
        System.out.println("------------------------------------------------");
    }

    @Test
    void shouldFindCustomerByMail(){
        String givenEmail = "lucienemmanueld@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isPresent();
    }

    @Test
    void shouldNotFindCustomerByMail(){
        String givenEmail = "cccccc@hhhhhhhhhhld@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindCustomersByFirstname(){
        String keyword = "e";
        List<Customer> expected = List.of(
                Customer.builder().firstName("Lucien").lastName("DIOP").email("lucienemmanueld@gmail.com").build(),
                Customer.builder().firstName("Marie").lastName("Artis").email("marie.artis53d@gmail.com").build()
        );
        List<Customer> result = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}