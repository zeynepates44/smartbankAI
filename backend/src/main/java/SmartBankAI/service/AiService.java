package SmartBankAI.service;

import SmartBankAI.dto.RecommendationDTO;
import SmartBankAI.model.customer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    private final RestTemplate restTemplate;
    private static final String FASTAPI_PREDICT_URL = "http://127.0.0.1:8000/predict";

    public AiService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1500);
        factory.setReadTimeout(1500);
        this.restTemplate = new RestTemplate(factory);
    }

    public RecommendationDTO getRecommendation(customer cust) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("age", cust.getAge());
            request.put("monthlyIncome", cust.getMonthlyIncome());
            request.put("monthlyExpense", cust.getMonthlyExpense());
            request.put("debtAmount", cust.getDebtAmount());
            request.put("accountBalance", cust.getAccountBalance());
            request.put("creditScore", cust.getCreditScore());
            request.put("latePaymentCount", cust.getLatePaymentCount());
            request.put("transactionCount", cust.getTransactionCount());

            Map<?, ?> response = restTemplate.postForObject(FASTAPI_PREDICT_URL, request, Map.class);

            if (response != null && response.containsKey("recommended_offer")) {
                String offer = (String) response.get("recommended_offer");
                Number confNum = (Number) response.get("confidence");
                Double confidence = confNum != null ? confNum.doubleValue() : 0.0;
                String explanation = (String) response.get("explanation");

                return new RecommendationDTO(offer, confidence, explanation, false);
            }
        } catch (Exception e) {
            System.err.println("[AiService] FastAPI erisilemedi, Fallback Kural Motoru devreye girdi: " + e.getMessage());
        }

        return fallbackRuleEngine(cust);
    }

    private RecommendationDTO fallbackRuleEngine(customer cust) {
        int creditScore = cust.getCreditScore() != null ? cust.getCreditScore() : 0;
        double balance = cust.getAccountBalance() != null ? cust.getAccountBalance() : 0.0;
        double income = cust.getMonthlyIncome() != null ? cust.getMonthlyIncome() : 0.0;
        double debt = cust.getDebtAmount() != null ? cust.getDebtAmount() : 0.0;
        int latePayment = cust.getLatePaymentCount() != null ? cust.getLatePaymentCount() : 0;
        int txCount = cust.getTransactionCount() != null ? cust.getTransactionCount() : 0;

        if (creditScore > 700 && balance > 100000) {
            return new RecommendationDTO("YATIRIM", 0.85, "Yüksek bakiye ve güçlü kredi skoru nedeniyle yatırım portföyü önerildi (Fallback).", true);
        } else if (creditScore > 600 && income > 25000 && debt < income * 4) {
            return new RecommendationDTO("KREDI", 0.80, "Gelir/borç dengesi ve kredi skoru kredi tahsisine uygundur (Fallback).", true);
        } else if (creditScore > 550 && txCount > 20 && latePayment <= 2) {
            return new RecommendationDTO("KREDI_KARTI", 0.75, "İşlem hareketliliği kredi kartı teklifi için uygundur (Fallback).", true);
        } else {
            return new RecommendationDTO("TEKLIF_YOK", 0.90, "Mevcut risk kriterleri nedeniyle teklif oluşturulmadı (Fallback).", true);
        }
    }
}