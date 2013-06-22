package eu.inmite.glass.banking.rest;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.common.collect.ImmutableMap;
import eu.inmite.glass.banking.model.fio.AccountStatement;
import eu.inmite.glass.banking.model.fio.AccountStatement.TransactionList.Transaction;
import eu.inmite.glass.banking.rest.model.AccountResponse;
import eu.inmite.glass.banking.utils.TemplateReplace;
import eu.inmite.glass.banking.view.model.AccountBalanceVO;
import eu.inmite.glass.banking.view.model.AccountInformationVO;
import eu.inmite.glass.banking.view.model.TransactionInfoVO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FioRESTClient {
	
	private static FioRESTClient inst = null;
	private URLFetchService httpClient;
	
	private static final String url = "https://www.fio.cz/ib_api/rest/periods/${TOKEN}/${DATE_FROM}/${DATE_TO}/transactions.json";
	
	private FioRESTClient() {
		super();
		httpClient = URLFetchServiceFactory.getURLFetchService();
	}
	
	public static FioRESTClient getInstance() {
		if (inst == null) {
			inst = new FioRESTClient();
		}
		return inst;
	}
	
	public AccountInformationVO fetchAccountInformation(String token) {
		return this.fetchAccountInformation(token, new Date(System.currentTimeMillis() - 30*24*60*60*1000), new Date());
	}
	
	public AccountInformationVO fetchAccountInformation(String token, Date fromDate, Date toDate) {
		AccountInformationVO accountInformation = new AccountInformationVO();
		Map<String, String> map = ImmutableMap.<String, String>of("TOKEN", token, "DATE_FROM", "2013-01-25", "DATE_TO", "2013-06-22");
		
		try {
			String fixedURL = TemplateReplace.templateReplace(url, map);
			HTTPResponse response = httpClient.fetch(new URL(fixedURL));
			
			AccountResponse responseObject = new ObjectMapper().readValue(response.getContent(), AccountResponse.class);
			AccountInformationVO returnObject = new AccountInformationVO();
			// Fill balance info from Statement
			AccountBalanceVO accountBalance = returnObject.getAccountBalance();
			accountBalance.setAccountNumber(responseObject.getAccountStatement().getInfo().getAccountId());
			accountBalance.setBankCode(responseObject.getAccountStatement().getInfo().getBankId());
			accountBalance.setAmount(responseObject.getAccountStatement().getInfo().getClosingBalance());
			accountBalance.setCurrencyCode(responseObject.getAccountStatement().getInfo().getCurrency());
			
			// Fill transactions from Statement
			List<TransactionInfoVO> transactions = returnObject.getTransactions();
			
			for (Transaction transactionDO : responseObject.getAccountStatement().getTransactionList().getTransaction()) {
				TransactionInfoVO transactionVO = new TransactionInfoVO();
				transactionVO.setAmount(transactionDO.getColumn1().getValue());
				transactionVO.setCurrencyCode(transactionDO.getColumn14().getValue());
				if (transactionDO.getColumn7() != null) {
					transactionVO.setMessage(transactionDO.getColumn7().getValue());
				} else if (transactionDO.getColumn8() != null){
					transactionVO.setMessage(transactionDO.getColumn8().getValue());
				} else {
					transactionVO.setMessage("ID: " + transactionDO.getColumn8().getValue());
				}
				if (transactionDO.getColumn0() != null) {
					transactionVO.setDate(transactionDO.getColumn0().getValue());
				}
				transactions.add(transactionVO);
			}
			
			return returnObject;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return accountInformation;
	}

}
