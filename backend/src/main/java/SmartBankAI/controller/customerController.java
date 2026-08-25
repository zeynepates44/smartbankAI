package SmartBankAI.controller;

import SmartBankAI.model.customer;
import SmartBankAI.model.PredictionResponse;
import SmartBankAI.repository.customerRepository;
import SmartBankAI.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class customerController {

    @Autowired
    private customerRepository repository;

    @Autowired
    private AiService aiService;

    @GetMapping
    public List<customer> getAllCustomers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Optional<customer> cust = repository.findByCustomerId(id);
        if (cust.isPresent()) {
            return ResponseEntity.ok(cust.get());
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "Müşteri bulunamadı");
        error.put("customerId", id.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @GetMapping("/{id}/recommendation")
    public ResponseEntity<?> getCustomerRecommendation(@PathVariable Long id) {
        Optional<customer> custOpt = repository.findByCustomerId(id);
        if (!custOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Müşteri bulunamadı");
            error.put("customerId", id.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        customer c = custOpt.get();
        PredictionResponse aiResponse = aiService.getPrediction(c);

        Map<String, Object> response = new HashMap<>();
        response.put("customer", c);
        response.put("aiRecommendation", aiResponse);

        return ResponseEntity.ok(response);
    }
}