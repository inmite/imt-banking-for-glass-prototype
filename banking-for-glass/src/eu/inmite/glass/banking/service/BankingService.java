package eu.inmite.glass.banking.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.collect.ImmutableMap;
import com.google.glassware.AuthUtil;
import com.google.glassware.MirrorClient;
import eu.inmite.glass.banking.rest.FioRESTClient;
import eu.inmite.glass.banking.utils.TemplateReplace;
import eu.inmite.glass.banking.view.model.AccountBalanceVO;
import eu.inmite.glass.banking.view.model.AccountInformationVO;
import eu.inmite.glass.banking.view.model.TransactionInfoVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Tomas Vondracek
 */
public class BankingService {

	private static final Logger LOG = Logger.getLogger(BankingService.class.getSimpleName());

	private static String TEMPLATE_TRANSACTION = null;

	private static String TEMPLATE_BALANCE = null;

	private static BankingService instance;

	public static BankingService getInstance() {
		if (instance == null) {
			instance = new BankingService();
			try {
				BankingService.setTEMPLATE_BALANCE(TemplateReplace.getHTMLTemplateText("balance"));
				BankingService.setTEMPLATE_TRANSACTION(TemplateReplace.getHTMLTemplateText("transaction"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	private BankingService() {
	}

	public void pushAccountForUser(String userId) throws IOException {
		// mock object
		final AccountInformationVO account = FioRESTClient.getInstance().fetchAccountInformation("jJ9itOlTXKiLXHEBRpMeXZCNe4RHSMwnf6YOY2hKRPrdYj3icikoftcWEvbbMAYM");
		final AccountBalanceVO balance = account.getAccountBalance();

		final Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);

		final String bundleId = balance.getFullBankAccountNumber();
		final TimelineItem coverItem = createCoverItem(balance, bundleId);
		final List<TimelineItem> transactionItems = new ArrayList<TimelineItem>();
		if (account.getTransactions() != null) {
			for (TransactionInfoVO transaction : account.getTransactions()) {
				TimelineItem tranItem = createTransactionItem(bundleId, transaction);
				transactionItems.add(tranItem);
			}
		}

		LOG.info("pushing account balance " + balance);
		try {
			MirrorClient.insertTimelineItem(credential, coverItem);
			for (TimelineItem item : transactionItems) {
				MirrorClient.insertTimelineItem(credential, item);
			}
		} catch (IOException e) {
			LOG.severe("failed to push timeline coverItem for user " + userId + "\n" + e.getMessage());
		}
	}

	private static String getTEMPLATE_BALANCE() {
		return TEMPLATE_BALANCE;
	}
	private static String getTEMPLATE_TRANSACTION() {
		return TEMPLATE_TRANSACTION;
	}

	private static void setTEMPLATE_BALANCE(String tEMPLATE_BALANCE) {
		TEMPLATE_BALANCE = tEMPLATE_BALANCE;
	}

	public static void setTEMPLATE_TRANSACTION(String templateTransaction) {
		TEMPLATE_TRANSACTION = templateTransaction;
	}

	private TimelineItem createTransactionItem(String bundleId, TransactionInfoVO transaction) {
		TimelineItem tranItem = new TimelineItem();
		final String color = transaction.getAmount().compareTo(BigDecimal.ZERO) > 0 ? "#00FF00" : "#FF0000";
		String html = TemplateReplace.templateReplace(getTEMPLATE_TRANSACTION(), ImmutableMap.<String, String>of(
				"AMOUNT", String.valueOf(transaction.getAmount().longValue()),
				"CURRENCY", transaction.getCurrencyCode(),
				"MESSAGE", transaction.getMessage(),
				"COLOR", color
		));
		tranItem.setHtml(html);
		tranItem.setBundleId(bundleId);
		tranItem.setIsBundleCover(false);
		return tranItem;
	}

	private TimelineItem createCoverItem(AccountBalanceVO balance, String bundleId) {

		final String html = TemplateReplace.templateReplace(getTEMPLATE_BALANCE(), ImmutableMap.<String, String>of(
				"BANK_ACCOUNT", balance.getFullBankAccountNumber(),
				"BALANCE", String.valueOf(balance.getAmount()),
				"CURRENCY", balance.getCurrencyCode()
		));

		final TimelineItem coverItem = new TimelineItem();
		coverItem.setBundleId(bundleId);
		coverItem.setIsBundleCover(true);
		coverItem.setHtml(html);
		return coverItem;
	}
}
