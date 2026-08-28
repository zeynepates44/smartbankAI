package SmartBankAI.dto;

import SmartBankAI.model.customer;

public class CustomerResponseDTO {
    private Integer customerId;
    private Integer age;
    private Double monthlyIncome;
    private Integer creditScore;
    private Double debtAmount;
    private Double accountBalance;
    private Double monthlyExpense;
    private Integer transactionCount;
    private Double avgTransactionAmount;
    private Integer latePaymentCount;
    private String offerType;

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(customer c) {
        if (c != null) {
            this.customerId = c.getCustomerId();
            this.age = c.getAge() != null ? ((Number) c.getAge()).intValue() : null;
            this.monthlyIncome = c.getMonthlyIncome() != null ? ((Number) c.getMonthlyIncome()).doubleValue() : null;
            this.creditScore = c.getCreditScore() != null ? ((Number) c.getCreditScore()).intValue() : null;
            this.debtAmount = c.getDebtAmount() != null ? ((Number) c.getDebtAmount()).doubleValue() : null;
            this.accountBalance = c.getAccountBalance() != null ? ((Number) c.getAccountBalance()).doubleValue() : null;
            this.monthlyExpense = c.getMonthlyExpense() != null ? ((Number) c.getMonthlyExpense()).doubleValue() : null;
            this.transactionCount = c.getTransactionCount() != null ? ((Number) c.getTransactionCount()).intValue() : null;
            this.avgTransactionAmount = c.getAvgTransactionAmount() != null ? ((Number) c.getAvgTransactionAmount()).doubleValue() : null;
            this.latePaymentCount = c.getLatePaymentCount() != null ? ((Number) c.getLatePaymentCount()).intValue() : null;
            this.offerType = c.getOfferType();
        }
    }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public Double getDebtAmount() { return debtAmount; }
    public void setDebtAmount(Double debtAmount) { this.debtAmount = debtAmount; }

    public Double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(Double accountBalance) { this.accountBalance = accountBalance; }

    public Double getMonthlyExpense() { return monthlyExpense; }
    public void setMonthlyExpense(Double monthlyExpense) { this.monthlyExpense = monthlyExpense; }

    public Integer getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }

    public Double getAvgTransactionAmount() { return avgTransactionAmount; }
    public void setAvgTransactionAmount(Double avgTransactionAmount) { this.avgTransactionAmount = avgTransactionAmount; }

    public Integer getLatePaymentCount() { return latePaymentCount; }
    public void setLatePaymentCount(Integer latePaymentCount) { this.latePaymentCount = latePaymentCount; }

    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }
}