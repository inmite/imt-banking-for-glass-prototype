package eu.inmite.glass.banking.view.model;

import java.math.BigDecimal;

public class AccountBalanceVO {
	
	private String accountPrefix;
	private String accountNumber;
	private String bankCode;
	private BigDecimal amount;
	private String currencyCode;
	
	public String getAccountPrefix() {
		return accountPrefix;
	}
	public void setAccountPrefix(String accountPrefix) {
		this.accountPrefix = accountPrefix;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getFullBankAccountNumber() {
		if (this.accountPrefix != null && this.accountPrefix.trim().length() > 0) {
			return this.accountPrefix + "-" + this.accountNumber + "/" + this.bankCode;
		} else {
			return this.accountNumber + "/" + this.bankCode;
		}
	}
	
}
