package SmartBankAI.model;

public class CustomerResponseDTO {
    private Long customerId;
    private int age;
    private double monthlyIncome;
    private int creditScore;
    private double debtAmount;
    private double accountBalance;
    private double monthlyExpense;
    private int transactionCount;
    private double avgTransactionAmount;
    private int latePaymentCount;

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(customer c) {
        this.customerId = c.getCustomerId();
        this.age = c.getAge();
        this.monthlyIncome = c.getMonthlyIncome();
        this.creditScore = c.getCreditScore();
        this.debtAmount = c.getDebtAmount();
        this.accountBalance = c.getAccountBalance();
        this.monthlyExpense = c.getMonthlyExpense();
        this.transactionCount = c.getTransactionCount();
        this.avgTransactionAmount = c.getAvgTransactionAmount();
        this.latePaymentCount = c.getLatePaymentCount();
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }

    public double getDebtAmount() { return debtAmount; }
    public void setDebtAmount(double debtAmount) { this.debtAmount = debtAmount; }

    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public double getMonthlyExpense() { return monthlyExpense; }
    public void setMonthlyExpense(double monthlyExpense) { this.monthlyExpense = monthlyExpense; }

    public int getTransactionCount() { return transactionCount; }
    public void setTransactionCount(int transactionCount) { this.transactionCount = transactionCount; }

    public double getAvgTransactionAmount() { return avgTransactionAmount; }
    public void setAvgTransactionAmount(double avgTransactionAmount) { this.avgTransactionAmount = avgTransactionAmount; }

    public int getLatePaymentCount() { return latePaymentCount; }
    public void setLatePaymentCount(int latePaymentCount) { this.latePaymentCount = latePaymentCount; }
}