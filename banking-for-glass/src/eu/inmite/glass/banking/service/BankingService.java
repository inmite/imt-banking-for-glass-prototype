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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Tomas Vondracek
 */
public class BankingService {

	private static final Logger LOG = Logger.getLogger(BankingService.class.getSimpleName());

	private static final String TEMPLATE_BALANCE = "<article>\n" +
			"  <div style=\"padding: 25px 40px 23px 30px; background-color: #333; text-align: right; border: 1px solid #555; color: #2A94FE\">\n" +
			"    <img src=\"http://data.inmite.eu/tmp/logo.png\" height=\"48\" width=\"88\"style=\"float: left;\"/>\n" +
			"   \t<p style=\"float: right; width: 480px;\">${BANK_ACCOUNT}</p>\n" +
			"\t<div style=\"clear: both; height: 0;\"></div>\n" +
			"  </div>\n" +
			"  <div style=\"padding: 60px 40px 60px 20px; text-align: right; font-size: 88px\">${BALANCE} ${CURRENCY}</div>\n" +
			"</article>";

	private static BankingService instance;

	public static BankingService getInstance() {
		if (instance == null) {
			instance = new BankingService();
			try {
				BankingService.setTEMPLATE_BALANCE(TemplateReplace.getHTMLTemplateText("balance"));
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

		final String html = TemplateReplace.templateReplace(getTEMPLATE_BALANCE(), ImmutableMap.<String, String>of(
				"BANK_ACCOUNT", balance.getFullBankAccountNumber(),
				"BALANCE", String.valueOf(balance.getAmount()),
				"CURRENCY", balance.getCurrencyCode()
		));
		final TimelineItem item = new TimelineItem();
		item.setHtml(html);
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

	private static void setTEMPLATE_BALANCE(String tEMPLATE_BALANCE) {
		TEMPLATE_BALANCE = tEMPLATE_BALANCE;
	}

	private TimelineItem createTransactionItem(String bundleId, TransactionInfoVO transaction) {
		TimelineItem tranItem = new TimelineItem();
		String html = TemplateReplace.templateReplace(TEMPLATE_TRANSACTION, ImmutableMap.<String, String>of(
				"AMOUNT", String.valueOf(transaction.getAmount().longValue()),
				"CURRENCY", transaction.getCurrencyCode(),
				"MESSAGE", transaction.getMessage()
		));
		tranItem.setHtml(html);
		tranItem.setBundleId(bundleId);
		tranItem.setIsBundleCover(false);
		return tranItem;
	}

	private TimelineItem createCoverItem(AccountBalanceVO balance, String bundleId) {
		final String html = TemplateReplace.templateReplace(TEMPLATE_BALANCE, ImmutableMap.<String, String>of(
				"BANK_ACCOUNT", balance.getFullBankAccountNumber(),
				"BALANCE", String.valueOf(balance.getAmount().longValue()),
				"CURRENCY", balance.getCurrencyCode()
		));

		final TimelineItem coverItem = new TimelineItem();
		coverItem.setBundleId(bundleId);
		coverItem.setIsBundleCover(true);
		coverItem.setHtml(html);
		return coverItem;
	}
}
