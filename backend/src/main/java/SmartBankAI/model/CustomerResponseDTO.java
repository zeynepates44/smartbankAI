package SmartBankAI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    private Long id;
    private String fullName;
    private String identityNumber;
    private Integer age;
    private String occupation;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private Integer creditScore;
    private BigDecimal accountBalance;
    private BigDecimal totalDebt;
    private Integer activeCreditsCount;
    private Integer latePaymentsCount;
    private Integer monthlyTransactionCount;
    private Double creditCardUsageRatio;
    private Integer mobileAppLoginsPerMonth;
    private String existingProducts;

    public static CustomerResponseDTO fromEntity(customer c) {
        if (c == null) return null;

        return CustomerResponseDTO.builder()
                .id(c.getId())
                .fullName(c.getFullName())
                .identityNumber(c.getIdentityNumber())
                .age(c.getAge())
                .occupation(c.getOccupation())
                .monthlyIncome(c.getMonthlyIncome())
                .monthlyExpenses(c.getMonthlyExpenses())
                .creditScore(c.getCreditScore())
                .accountBalance(c.getAccountBalance())
                .totalDebt(c.getTotalDebt())
                .activeCreditsCount(c.getActiveCreditsCount())
                .latePaymentsCount(c.getLatePaymentsCount())
                .monthlyTransactionCount(c.getMonthlyTransactionCount())
                .creditCardUsageRatio(c.getCreditCardUsageRatio())
                .mobileAppLoginsPerMonth(c.getMobileAppLoginsPerMonth())
                .existingProducts(c.getExistingProducts())
                .build();
    }
}