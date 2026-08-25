package SmartBankAI.service;

import SmartBankAI.model.customer;
import SmartBankAI.model.PredictionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    @Autowired
    private RestTemplate restTemplate;

    private final String AI_API_URL = "http://127.0.0.1:8000/predict";

    public PredictionResponse getPrediction(customer c) {
        try {
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("age", c.getAge() != null ? c.getAge() : 0);
            requestPayload.put("monthly_income", c.getMonthlyIncome() != null ? c.getMonthlyIncome() : 0.0);
            requestPayload.put("credit_score", c.getCreditScore() != null ? c.getCreditScore() : 0);
            requestPayload.put("debt_amount", c.getDebtAmount() != null ? c.getDebtAmount() : 0.0);
            requestPayload.put("account_balance", c.getAccountBalance() != null ? c.getAccountBalance() : 0.0);
            requestPayload.put("monthly_expense", c.getMonthlyExpense() != null ? c.getMonthlyExpense() : 0.0);
            requestPayload.put("transaction_count", c.getTransactionCount() != null ? c.getTransactionCount() : 0);
            requestPayload.put("avg_transaction_amount", c.getAvgTransactionAmount() != null ? c.getAvgTransactionAmount() : 0.0);
            requestPayload.put("late_payment_count", c.getLatePaymentCount() != null ? c.getLatePaymentCount() : 0);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

            return restTemplate.postForObject(AI_API_URL, entity, PredictionResponse.class);
        } catch (Exception e) {
            // Python servisi kapalı veya timeout olduğunda uygulamanın çökmesini önleyen Fallback yanıtı
            PredictionResponse fallback = new PredictionResponse();
            fallback.setRecommended_offer("SERVIS_ERISILEMEZ");
            fallback.setConfidence(0.0);
            return fallback;
        }
    }
}