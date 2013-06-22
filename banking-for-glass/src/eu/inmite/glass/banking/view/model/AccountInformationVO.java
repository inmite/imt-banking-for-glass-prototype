package eu.inmite.glass.banking.view.model;

import java.util.LinkedList;
import java.util.List;

public class AccountInformationVO {
	
	private AccountBalanceVO accountBalance;
	private List<TransactionInfoVO> transactions;
	
	public AccountInformationVO() {
		super();
		accountBalance = new AccountBalanceVO();
		transactions = new LinkedList<TransactionInfoVO>();
	}
	
	public AccountBalanceVO getAccountBalance() {
		return accountBalance;
	}
	
	public List<TransactionInfoVO> getTransactions() {
		return transactions;
	}

}
