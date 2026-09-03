package SmartBankAI.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 20)
    private String identityNumber;

    private Integer age;

    @Column(length = 50)
    private String occupation;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyExpenses;

    private Integer creditScore;

    @Column(precision = 15, scale = 2)
    private BigDecimal accountBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalDebt;

    private Integer activeCreditsCount;

    private Integer latePaymentsCount;

    // Davranissal ve Islemsel Veriler
    private Integer monthlyTransactionCount;

    @Column
    private Double creditCardUsageRatio;

    private Integer mobileAppLoginsPerMonth;

    @Column(length = 255)
    private String existingProducts;
}