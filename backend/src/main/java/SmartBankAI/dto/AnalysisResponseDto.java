package SmartBankAI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponseDto {

    private String recommendedProduct;
    private Integer recommendationScore;
    private RiskMetricDto creditRisk;
    private RiskMetricDto fraudRisk;
    private RiskMetricDto churnRisk;
    private List<String> explanationReasons;
    private String aiNaturalLanguageSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskMetricDto {
        private String level;       // LOW, MEDIUM, HIGH
        private Integer probability; // 0 - 100
        private String statusColor; // success, warning, danger
    }
}