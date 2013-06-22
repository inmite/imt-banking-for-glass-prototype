package eu.inmite.glass.banking.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.glassware.AuthUtil;
import com.google.glassware.MirrorClient;
import eu.inmite.glass.banking.view.model.AccountBalanceVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * @author Tomas Vondracek
 */
public class BankingService {

	private static final Logger LOG = Logger.getLogger(BankingService.class.getSimpleName());

	private static BankingService instance;

	public static BankingService getInstance() {
		if (instance == null) {
			instance = new BankingService();
		}
		return instance;
	}

	private BankingService() {
	}

	public void pushAccountForUser(String userId) throws IOException {
		// mock object
		final AccountBalanceVO info = new AccountBalanceVO();
		info.setAccountNumber("123");
		info.setAccountPrefix("19");
		info.setBankCode("0100");
		info.setAmount(new BigDecimal(10000));
		info.setCurrencyCode("CZK");

		final Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);

		final TimelineItem item = new TimelineItem();
		item.setText(info.toString());

		LOG.info("pushing account info " + info);
		try {
			MirrorClient.insertTimelineItem(credential, item);
		} catch (IOException e) {
			LOG.severe("failed to push timeline item for user " + userId + "\n" + e.getMessage());
		}
	}
}
