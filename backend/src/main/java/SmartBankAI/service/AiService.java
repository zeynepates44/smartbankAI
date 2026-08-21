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
        // FastAPI'nin beklediği JSON formatındaki parametreleri hazırla
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("age", c.getAge());
        requestPayload.put("monthly_income", c.getMonthlyIncome());
        requestPayload.put("credit_score", c.getCreditScore());
        requestPayload.put("debt_amount", c.getDebtAmount());
        requestPayload.put("account_balance", c.getAccountBalance());
        requestPayload.put("monthly_expense", c.getMonthlyExpense());
        requestPayload.put("transaction_count", c.getTransactionCount());
        requestPayload.put("avg_transaction_amount", c.getAvgTransactionAmount());
        requestPayload.put("late_payment_count", c.getLatePaymentCount());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        // Python FastAPI endpoint'ine POST isteği gönder
        return restTemplate.postForObject(AI_API_URL, entity, PredictionResponse.class);
    }
}
