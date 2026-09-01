package SmartBankAI.controller;

import SmartBankAI.dto.CustomerPageResponseDTO;
import SmartBankAI.dto.CustomerResponseDTO;
import SmartBankAI.dto.RecommendationDTO;
import SmartBankAI.exception.ResourceNotFoundException;
import SmartBankAI.model.customer;
import SmartBankAI.repository.customerRepository;
import SmartBankAI.service.AiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class customerController {

    private final customerRepository customerRepo;
    private final AiService aiService;

    public customerController(customerRepository customerRepo, AiService aiService) {
        this.customerRepo = customerRepo;
        this.aiService = aiService;
    }

    @GetMapping
    public ResponseEntity<CustomerPageResponseDTO> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("customerId").ascending());
        Page<customer> customerPage = customerRepo.findAll(pageable);

        var customerDTOs = customerPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        CustomerPageResponseDTO response = new CustomerPageResponseDTO(
                customerDTOs,
                customerPage.getNumber(),
                customerPage.getTotalPages(),
                customerPage.getTotalElements(),
                customerPage.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        customer foundCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID: " + id + " numaralı müşteri bulunamadı."));

        return ResponseEntity.ok(mapToDTO(foundCustomer));
    }

    @GetMapping("/{id}/recommendation")
    public ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable Integer id) {
        customer foundCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID: " + id + " numaralı müşteri bulunamadı."));

        RecommendationDTO recommendation = aiService.getRecommendation(foundCustomer);
        return ResponseEntity.ok(recommendation);
    }

    private CustomerResponseDTO mapToDTO(customer entity) {
        return new CustomerResponseDTO(
                entity.getCustomerId(),
                entity.getAge(),
                entity.getMonthlyIncome(),
                entity.getMonthlyExpense(),
                entity.getDebtAmount(),
                entity.getAccountBalance(),
                entity.getCreditScore(),
                entity.getLatePaymentCount(),
                entity.getTransactionCount()
        );
    }
}