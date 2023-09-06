package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private Integer payments;
    private String accountNumber;

    public LoanApplicationDTO(Long loanId,double amount, Integer payments,String toAccountNumber){
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = toAccountNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccount() {
        return accountNumber;
    }
}
