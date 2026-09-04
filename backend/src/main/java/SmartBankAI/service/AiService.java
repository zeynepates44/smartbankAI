package SmartBankAI.service;

import SmartBankAI.dto.AnalysisResponseDto;
import SmartBankAI.model.customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.service.url:https://smartbank-ai-k1i9.onrender.com/analyze}")
    private String aiServiceUrl = "https://smartbank-ai-k1i9.onrender.com/analyze";

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisResponseDto analyzeCustomer(customer cust) {
        Map<String, Object> request = new HashMap<>();

        // Swagger dokümanındaki şemayla birebir örtüşen alanlar:
        request.put("id", cust.getId() != null ? cust.getId() : 0);
        request.put("fullName", cust.getFullName() != null ? cust.getFullName() : "Bilinmeyen Musteri");
        request.put("age", cust.getAge() != null ? cust.getAge() : 0);
        request.put("monthlyIncome", cust.getMonthlyIncome() != null ? cust.getMonthlyIncome().doubleValue() : 0.0);
        request.put("monthlyExpenses", cust.getMonthlyExpenses() != null ? cust.getMonthlyExpenses().doubleValue() : 0.0);
        request.put("creditScore", cust.getCreditScore() != null ? cust.getCreditScore() : 0);
        request.put("accountBalance", cust.getAccountBalance() != null ? cust.getAccountBalance().doubleValue() : 0.0);
        request.put("totalDebt", cust.getTotalDebt() != null ? cust.getTotalDebt().doubleValue() : 0.0);
        request.put("activeCreditsCount", cust.getActiveCreditsCount() != null ? cust.getActiveCreditsCount() : 1);
        request.put("latePaymentsCount", cust.getLatePaymentsCount() != null ? cust.getLatePaymentsCount() : 0);
        request.put("monthlyTransactionCount", cust.getMonthlyTransactionCount() != null ? cust.getMonthlyTransactionCount() : 15);
        request.put("creditCardUsageRatio", cust.getCreditCardUsageRatio() != null ? cust.getCreditCardUsageRatio() : 0.4);
        request.put("mobileAppLoginsPerMonth", cust.getMobileAppLoginsPerMonth() != null ? cust.getMobileAppLoginsPerMonth() : 10);
        request.put("existingProducts", cust.getExistingProducts() != null ? cust.getExistingProducts() : "Vadesiz Hesap");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            return restTemplate.postForObject(aiServiceUrl, entity, AnalysisResponseDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            // Eğer /analyze 404 dönerse FastAPI sonundaki eğik çizgiyi (/analyze/) arıyor olabilir:
            String fallbackUrl = aiServiceUrl.endsWith("/") ? aiServiceUrl.substring(0, aiServiceUrl.length() - 1) : aiServiceUrl + "/";
            System.out.println("404 alindi, fallback deneniyor: " + fallbackUrl);
            return restTemplate.postForObject(fallbackUrl, entity, AnalysisResponseDto.class);
        } catch (Exception e) {
            System.err.println("AI Servisi Istek Hatasi: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}