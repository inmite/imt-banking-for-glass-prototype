package eu.inmite.glass.model;

import java.math.BigDecimal;

/**
 * @author Tomas Vondracek
 */
public class AccountInfo {

	private final String prefix;
	private final String number;
	private final String bankCode;

	private final BigDecimal balance;

	public AccountInfo(String prefix, String number, String bankCode, BigDecimal balance) {
		this.prefix = prefix;
		this.number = number;
		this.bankCode = bankCode;
		this.balance = balance;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getNumber() {
		return number;
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public String toString() {
		return "AccountInfo{" +
				"prefix='" + prefix + '\'' +
				", number='" + number + '\'' +
				", bankCode='" + bankCode + '\'' +
				", balance=" + balance +
				"} " + super.toString();
	}
}
