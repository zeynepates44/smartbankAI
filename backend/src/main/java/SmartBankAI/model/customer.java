package SmartBankAI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers", schema = "dbo")
public class customer {

    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "monthly_income")
    private Double monthlyIncome;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "debt_amount")
    private Double debtAmount;

    @Column(name = "account_balance")
    private Double accountBalance;

    @Column(name = "monthly_expense")
    private Double monthlyExpense;

    @Column(name = "transaction_count")
    private Integer transactionCount;

    @Column(name = "avg_transaction_amount")
    private Double avgTransactionAmount;

    @Column(name = "late_payment_count")
    private Integer latePaymentCount;

    @Column(name = "offer_type")
    private String offerType;

    public customer() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public Double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(Double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Double getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(Double monthlyExpense) {
        this.monthlyExpense = monthlyExpense;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getAvgTransactionAmount() {
        return avgTransactionAmount;
    }

    public void setAvgTransactionAmount(Double avgTransactionAmount) {
        this.avgTransactionAmount = avgTransactionAmount;
    }

    public Integer getLatePaymentCount() {
        return latePaymentCount;
    }

    public void setLatePaymentCount(Integer latePaymentCount) {
        this.latePaymentCount = latePaymentCount;
    }
    public String getOfferType() {
        return offerType;
    }
    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
}