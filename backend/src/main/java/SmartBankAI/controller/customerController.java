package SmartBankAI.controller;

import SmartBankAI.dto.CustomerPageResponseDTO;
import SmartBankAI.dto.CustomerResponseDTO;
import SmartBankAI.dto.RecommendationDTO;
import SmartBankAI.exception.ResourceNotFoundException;
import SmartBankAI.model.customer;
import SmartBankAI.model.PredictionResponse;
import SmartBankAI.repository.customerRepository;
import SmartBankAI.service.AiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("customerId").ascending());
        Page<customer> customerPage = customerRepo.findAll(pageable);

        List<CustomerResponseDTO> dtos = customerPage.getContent()
                .stream()
                .map(CustomerResponseDTO::new)
                .collect(Collectors.toList());

        CustomerPageResponseDTO response = new CustomerPageResponseDTO(
                dtos,
                customerPage.getNumber(),
                customerPage.getTotalPages(),
                customerPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı ID: " + id));
        return ResponseEntity.ok(new CustomerResponseDTO(c));
    }

    @GetMapping("/{id}/recommendation")
    public ResponseEntity<RecommendationDTO> getCustomerRecommendation(@PathVariable Integer id) {
        customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı ID: " + id));

        PredictionResponse prediction = aiService.getPrediction(c);
        RecommendationDTO response = new RecommendationDTO(new CustomerResponseDTO(c), prediction);

        return ResponseEntity.ok(response);
    }
}