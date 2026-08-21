package SmartBankAI.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    @JsonProperty("recommended_offer")
    private String recommendedOffer;

    @JsonProperty("confidence")
    private Double confidence;

    public PredictionResponse() {
    }

    public PredictionResponse(String recommendedOffer, Double confidence) {
        this.recommendedOffer = recommendedOffer;
        this.confidence = confidence;
    }

    public String getRecommendedOffer() {
        return recommendedOffer;
    }

    public void setRecommendedOffer(String recommendedOffer) {
        this.recommendedOffer = recommendedOffer;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}