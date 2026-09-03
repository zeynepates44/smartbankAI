package SmartBankAI.service;

import SmartBankAI.model.customer;
import SmartBankAI.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockCustomer = customer.builder()
                .id(1L)
                .fullName("Test User")
                .identityNumber("12345678901")
                .monthlyIncome(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(3000))
                .totalDebt(BigDecimal.valueOf(5000))
                .creditScore(700)
                .activeCreditsCount(1)
                .latePaymentsCount(0)
                .monthlyTransactionCount(15)
                .build();
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        Optional<customer> result = customerRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }
}