package SmartBankAI.model;

public class PredictionResponse {
    private String recommended_offer;
    private Double confidence;
    private String status;
    private Boolean is_fallback;

    public PredictionResponse() {}

    public PredictionResponse(String recommended_offer, Double confidence, String status, Boolean is_fallback) {
        this.recommended_offer = recommended_offer;
        this.confidence = confidence;
        this.status = status;
        this.is_fallback = is_fallback;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIs_fallback() {
        return is_fallback;
    }

    public void setIs_fallback(Boolean is_fallback) {
        this.is_fallback = is_fallback;
    }
}