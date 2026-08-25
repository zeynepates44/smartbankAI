package SmartBankAI.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    @JsonProperty("recommended_offer")
    private String recommended_offer;

    private Double confidence;

    public PredictionResponse() {
    }

    public PredictionResponse(String recommended_offer, Double confidence) {
        this.recommended_offer = recommended_offer;
        this.confidence = confidence;
    }

    public String getRecommended_offer() {
        return recommended_offer;
    }

    public void setRecommended_offer(String recommended_offer) {
        this.recommended_offer = recommended_offer;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}