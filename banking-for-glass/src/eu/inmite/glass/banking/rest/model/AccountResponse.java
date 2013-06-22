package eu.inmite.glass.banking.rest.model;

import eu.inmite.glass.banking.model.fio.AccountStatement;

public class AccountResponse {

	private AccountStatement accountStatement;

	public AccountStatement getAccountStatement() {
		return accountStatement;
	}

	public void setAccountStatement(AccountStatement accountStatement) {
		this.accountStatement = accountStatement;
	}
	
}
