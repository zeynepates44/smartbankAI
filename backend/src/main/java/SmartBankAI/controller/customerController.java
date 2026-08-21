package SmartBankAI.controller;

import SmartBankAI.model.customer;
import SmartBankAI.model.PredictionResponse;
import SmartBankAI.repository.customerRepository;
import SmartBankAI.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*") // İleride frontend bağlandığında CORS hatası almamak için
public class customerController {

    @Autowired
    private customerRepository customerRepository;

    @Autowired
    private AiService aiService;

    // 1. Mevcut tüm müşterileri getiren endpoint (KORUNDU)
    @GetMapping
    public List<customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 2. Mevcut tek müşteri getiren endpoint (KORUNDU)
    @GetMapping("/{id}")
    public ResponseEntity<customer> getCustomerById(@PathVariable Integer id) {
        Optional<customer> customerData = customerRepository.findById(id);
        return customerData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. YENİ EKLENEN: Müşteri bilgisi + AI Model Tahminini birleştiren endpoint
    @GetMapping("/{id}/recommendation")
    public ResponseEntity<Map<String, Object>> getCustomerRecommendation(@PathVariable Integer id) {
        Optional<customer> customerData = customerRepository.findById(id);

        if (customerData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        customer c = customerData.get();
        PredictionResponse prediction = aiService.getPrediction(c);

        Map<String, Object> response = new HashMap<>();
        response.put("customer", c);
        response.put("aiRecommendation", prediction);

        return ResponseEntity.ok(response);
    }
}