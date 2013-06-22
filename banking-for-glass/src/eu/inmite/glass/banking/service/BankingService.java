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

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Tomas Vondracek
 */
public class BankingService {

	private static final Logger LOG = Logger.getLogger(BankingService.class.getSimpleName());

	private static String TEMPLATE_BALANCE = null;

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

		LOG.info("pushing account balance " + balance);
		try {
			MirrorClient.insertTimelineItem(credential, item);
		} catch (IOException e) {
			LOG.severe("failed to push timeline item for user " + userId + "\n" + e.getMessage());
		}
	}

	private static String getTEMPLATE_BALANCE() {
		return TEMPLATE_BALANCE;
	}

	private static void setTEMPLATE_BALANCE(String tEMPLATE_BALANCE) {
		TEMPLATE_BALANCE = tEMPLATE_BALANCE;
	}
}
