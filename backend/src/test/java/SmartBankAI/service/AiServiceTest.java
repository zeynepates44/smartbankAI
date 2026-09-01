package SmartBankAI.service;

import SmartBankAI.dto.RecommendationDTO;
import SmartBankAI.model.customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiServiceTest {

    private AiService aiService;

    @BeforeEach
    void setUp() {
        aiService = new AiService();
    }

    @Test
    @DisplayName("FastAPI kapaliyken yuksek bakiye ve skorda YATIRIM fallback teklifi uretilmelidir")
    void shouldReturnInvestmentFallback_WhenFastApiIsOffline() {
        customer testCustomer = new customer();
        testCustomer.setCreditScore(750);
        testCustomer.setAccountBalance(150000.0);
        testCustomer.setMonthlyIncome(30000.0);
        testCustomer.setDebtAmount(5000.0);
        testCustomer.setLatePaymentCount(0);
        testCustomer.setTransactionCount(30);

        RecommendationDTO result = aiService.getRecommendation(testCustomer);

        assertNotNull(result);
        assertEquals("YATIRIM", result.getRecommendedOffer());
        assertTrue(result.getConfidence() >= 0.70);
    }

    @Test
    @DisplayName("Dusuk kredi skorunda TEKLIF_YOK fallback sonucu donmelidir")
    void shouldReturnNoOfferFallback_WhenCustomerIsHighRisk() {
        customer testCustomer = new customer();
        testCustomer.setCreditScore(400);
        testCustomer.setAccountBalance(1000.0);
        testCustomer.setMonthlyIncome(10000.0);
        testCustomer.setDebtAmount(50000.0);
        testCustomer.setLatePaymentCount(5);
        testCustomer.setTransactionCount(2);

        RecommendationDTO result = aiService.getRecommendation(testCustomer);

        assertNotNull(result);
        assertEquals("TEKLIF_YOK", result.getRecommendedOffer());
    }
}