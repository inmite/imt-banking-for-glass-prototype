package eu.inmite.glass.banking.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Tomas Vondracek
 */
public class Transaction {

	private final String contraAccountPrefix;
	private final String contraAccountNumber;
	private final String contraBankCode;

	private final BigDecimal amount;
	private final Date date;

	private final String message;

	public Transaction(BigDecimal amount, Date date, String contraAccountPrefix, String contraAccountNumber, String contraBankCode, String message) {
		this.amount = amount;
		this.date = date;
		this.contraAccountPrefix = contraAccountPrefix;
		this.contraAccountNumber = contraAccountNumber;
		this.contraBankCode = contraBankCode;
		this.message = message;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getContraAccountPrefix() {
		return contraAccountPrefix;
	}

	public String getContraAccountNumber() {
		return contraAccountNumber;
	}

	public String getContraBankCode() {
		return contraBankCode;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}
}
