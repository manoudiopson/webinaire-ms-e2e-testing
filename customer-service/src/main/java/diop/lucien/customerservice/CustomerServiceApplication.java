package diop.lucien.customerservice;

import diop.lucien.customerservice.entities.Customer;
import diop.lucien.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean @Profile("!test")
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(Customer.builder().firstName("Lucien")
                    .lastName("DIOP").email("lucienemmanueld@gmail.com").build());
            customerRepository.save(Customer.builder().firstName("Marie")
                    .lastName("Artis").email("marie.artis53d@gmail.com").build());
            customerRepository.save(Customer.builder().firstName("François")
                    .lastName("Nassalan").email("françois@gmail.com").build());
        };
    }
}
