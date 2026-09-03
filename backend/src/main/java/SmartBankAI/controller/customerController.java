package SmartBankAI.controller;

import SmartBankAI.dto.AnalysisResponseDto;
import SmartBankAI.model.customer;
import SmartBankAI.repository.customerRepository;
import SmartBankAI.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class customerController {

    private final customerRepository customerRepo;
    private final AiService aiService;

    @GetMapping
    public List<customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<customer> getCustomerById(@PathVariable Long id) {
        return customerRepo.findById(id)
                .map(cust -> ResponseEntity.ok(cust))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<AnalysisResponseDto> analyzeCustomer(@PathVariable Long id) {
        return customerRepo.findById(id)
                .map(cust -> ResponseEntity.ok(aiService.analyzeCustomer(cust)))
                .orElse(ResponseEntity.notFound().build());
    }
}