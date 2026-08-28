package SmartBankAI.service;

import SmartBankAI.model.customer;
import SmartBankAI.model.PredictionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    private final RestTemplate restTemplate;
    private final String FASTAPI_URL = "http://127.0.0.1:8000/predict";

    public AiService() {
        // 1.5 saniye baglanti ve okuma zaman asimi
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1500);
        factory.setReadTimeout(1500);
        this.restTemplate = new RestTemplate(factory);
    }

    public PredictionResponse getPrediction(customer c) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("age", c.getAge());
        body.put("monthly_income", c.getMonthlyIncome() != null ? c.getMonthlyIncome().doubleValue() : 0.0);
        body.put("credit_score", c.getCreditScore());
        body.put("debt_amount", c.getDebtAmount() != null ? c.getDebtAmount().doubleValue() : 0.0);
        body.put("account_balance", c.getAccountBalance() != null ? c.getAccountBalance().doubleValue() : 0.0);
        body.put("monthly_expense", c.getMonthlyExpense() != null ? c.getMonthlyExpense().doubleValue() : 0.0);
        body.put("transaction_count", c.getTransactionCount());
        body.put("avg_transaction_amount", c.getAvgTransactionAmount() != null ? c.getAvgTransactionAmount().doubleValue() : 0.0);
        body.put("late_payment_count", c.getLatePaymentCount());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<PredictionResponse> response = restTemplate.postForEntity(
                    FASTAPI_URL,
                    requestEntity,
                    PredictionResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PredictionResponse result = response.getBody();
                result.setIs_fallback(false);
                return result;
            }
        } catch (Exception e) {
            System.err.println("[!] FastAPI servisine erisilemedi/zaman asimi: " + e.getMessage() + " -> Fallback Rule Engine devreye giriyor.");
        }

        return getFallbackRuleRecommendation(c);
    }

    private PredictionResponse getFallbackRuleRecommendation(customer c) {
        PredictionResponse fallback = new PredictionResponse();
        fallback.setIs_fallback(true);
        fallback.setStatus("FALLBACK_SUCCESS");

        int latePayments = c.getLatePaymentCount() != null ? c.getLatePaymentCount() : 0;
        int score = c.getCreditScore() != null ? c.getCreditScore() : 0;
        double balance = c.getAccountBalance() != null ? c.getAccountBalance().doubleValue() : 0.0;
        double income = c.getMonthlyIncome() != null ? c.getMonthlyIncome().doubleValue() : 0.0;

        if (latePayments >= 3 || score < 520) {
            fallback.setRecommended_offer("TEKLIF_YOK");
            fallback.setConfidence(0.90);
        } else if (score >= 720 && balance >= 60000) {
            fallback.setRecommended_offer("YATIRIM");
            fallback.setConfidence(0.88);
        } else if (score >= 620 && income >= 35000) {
            fallback.setRecommended_offer("KREDI");
            fallback.setConfidence(0.85);
        } else {
            fallback.setRecommended_offer("KREDI_KARTI");
            fallback.setConfidence(0.80);
        }

        return fallback;
    }
}