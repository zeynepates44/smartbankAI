package SmartBankAI.dto;

public class CustomerResponseDTO {
    private Integer customerId;
    private Integer age;
    private Double monthlyIncome;
    private Double monthlyExpense;
    private Double debtAmount;
    private Double accountBalance;
    private Integer creditScore;
    private Integer latePaymentCount;
    private Integer transactionCount;

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(Integer customerId, Integer age, Double monthlyIncome, Double monthlyExpense,
                               Double debtAmount, Double accountBalance, Integer creditScore,
                               Integer latePaymentCount, Integer transactionCount) {
        this.customerId = customerId;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpense = monthlyExpense;
        this.debtAmount = debtAmount;
        this.accountBalance = accountBalance;
        this.creditScore = creditScore;
        this.latePaymentCount = latePaymentCount;
        this.transactionCount = transactionCount;
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

    public Double getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(Double monthlyExpense) {
        this.monthlyExpense = monthlyExpense;
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

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public Integer getLatePaymentCount() {
        return latePaymentCount;
    }

    public void setLatePaymentCount(Integer latePaymentCount) {
        this.latePaymentCount = latePaymentCount;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }
}