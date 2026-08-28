package SmartBankAI.dto;

import SmartBankAI.model.PredictionResponse;

public class RecommendationDTO {
    private CustomerResponseDTO customer;
    private PredictionResponse aiRecommendation;

    public RecommendationDTO() {}

    public RecommendationDTO(CustomerResponseDTO customer, PredictionResponse aiRecommendation) {
        this.customer = customer;
        this.aiRecommendation = aiRecommendation;
    }

    public CustomerResponseDTO getCustomer() { return customer; }
    public void setCustomer(CustomerResponseDTO customer) { this.customer = customer; }
    public PredictionResponse getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(PredictionResponse aiRecommendation) { this.aiRecommendation = aiRecommendation; }
}