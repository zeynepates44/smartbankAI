package SmartBankAI.dto;

public class RecommendationDTO {
    private String recommendedOffer;
    private Double confidence;
    private String explanation;
    private Boolean isFallback;

    public RecommendationDTO() {
    }

    public RecommendationDTO(String recommendedOffer, Double confidence, String explanation, Boolean isFallback) {
        this.recommendedOffer = recommendedOffer;
        this.confidence = confidence;
        this.explanation = explanation;
        this.isFallback = isFallback;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getIsFallback() {
        return isFallback;
    }

    public void setIsFallback(Boolean isFallback) {
        this.isFallback = isFallback;
    }
}