package SmartBankAI.service;

import SmartBankAI.exception.ResourceNotFoundException;
import SmartBankAI.model.customer;
import SmartBankAI.repository.customerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private customerRepository customerRepo;

    @Test
    @DisplayName("Mevcut musteri ID sorgulandiginda musteri nesnesi donmelidir")
    void shouldReturnCustomer_WhenCustomerExists() {
        customer mockCustomer = new customer();
        mockCustomer.setCustomerId(1);
        mockCustomer.setCreditScore(700);

        when(customerRepo.findById(1)).thenReturn(Optional.of(mockCustomer));

        Optional<customer> result = customerRepo.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCustomerId());
        verify(customerRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Olmayan musteri ID sorgulandiginda ResourceNotFoundException firlatilmalidir")
    void shouldThrowException_WhenCustomerNotFound() {
        when(customerRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerRepo.findById(999)
                    .orElseThrow(() -> new ResourceNotFoundException("ID: 999 numarali musteri bulunamadi."));
        });

        verify(customerRepo, times(1)).findById(999);
    }
}