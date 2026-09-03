package SmartBankAI.service;

import SmartBankAI.dto.AnalysisResponseDto;
import SmartBankAI.model.customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.service.url:http://localhost:8000/analyze}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisResponseDto analyzeCustomer(customer cust) {
        Map<String, Object> request = new HashMap<>();
        request.put("id", cust.getId());
        request.put("fullName", cust.getFullName());
        request.put("age", cust.getAge());
        request.put("monthlyIncome", cust.getMonthlyIncome());
        request.put("monthlyExpenses", cust.getMonthlyExpenses());
        request.put("creditScore", cust.getCreditScore());
        request.put("accountBalance", cust.getAccountBalance());
        request.put("totalDebt", cust.getTotalDebt());
        request.put("activeCreditsCount", cust.getActiveCreditsCount() != null ? cust.getActiveCreditsCount() : 1);
        request.put("latePaymentsCount", cust.getLatePaymentsCount() != null ? cust.getLatePaymentsCount() : 0);
        request.put("monthlyTransactionCount", cust.getMonthlyTransactionCount() != null ? cust.getMonthlyTransactionCount() : 15);
        request.put("creditCardUsageRatio", cust.getCreditCardUsageRatio() != null ? cust.getCreditCardUsageRatio() : 0.40);
        request.put("mobileAppLoginsPerMonth", cust.getMobileAppLoginsPerMonth() != null ? cust.getMobileAppLoginsPerMonth() : 10);
        request.put("existingProducts", cust.getExistingProducts() != null ? cust.getExistingProducts() : "Vadesiz Hesap");

        return restTemplate.postForObject(aiServiceUrl, request, AnalysisResponseDto.class);
    }
}