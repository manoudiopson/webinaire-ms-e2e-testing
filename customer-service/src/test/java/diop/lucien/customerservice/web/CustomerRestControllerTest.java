package diop.lucien.customerservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import diop.lucien.customerservice.dto.CustomerDTO;
import diop.lucien.customerservice.execptions.CustomerNotFoundException;
import diop.lucien.customerservice.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@ActiveProfiles("test") @WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    List<CustomerDTO> customers;
    @BeforeEach
    void setUp() {
        this.customers = List.of(
                CustomerDTO.builder().id(1L).firstName("Lucien").lastName("DIOP").email("lucienemmanueld@gmail.com").build(),
                CustomerDTO.builder().id(2L).firstName("Marie").lastName("Artis").email("marie.artis53d@gmail.com").build(),
                CustomerDTO.builder().id(3L).firstName("François").lastName("Nassalan").email("françois@gmail.com").build()
        );
    }
    @Test
    void shouldGetAllCustomers() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }
    @Test
    void shouldGetCustomerById() throws Exception {
        Long id = 1L;
        Mockito.when(customerService.findCustomerById(id)).thenReturn(customers.getFirst());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writer().writeValueAsString(customers.getFirst())));
    }
    @Test
    void shouldNotGetCustomerByInvalidId() throws Exception {
        Long id = 9L;
        Mockito.when(customerService.findCustomerById(id)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    void searchCustomers() throws Exception {
        String keyword = "i";
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers?keyword=keyword", keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }
    @Test
    void shouldSaveCustomer() throws Exception {
        CustomerDTO customerDTO = customers.getFirst();
        String expected = """
                {"id":1,"firstName":"Lucien","lastName":"DIOP","email":"lucienemmanueld@gmail.com"}
                """;
        Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customers.getFirst());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }
    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerDTO customerDTO = customers.getFirst();
        Mockito.when(customerService.updateCustomer(Mockito.eq(customerId), Mockito.any())).thenReturn(customers.getFirst());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }
    @Test
    void testDeleteCustomer() throws Exception {
        Long customerId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}", customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}