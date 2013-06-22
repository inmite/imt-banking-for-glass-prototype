package eu.inmite.glass.banking.servlet;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import eu.inmite.glass.banking.model.BankingService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * handles task request - refreshes data for one user and push new timeline item for him
 * @author Tomas Vondracek
 */
public class RefreshAccountsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO verify that appengine is the one who called us

		final String userId = req.getParameter("userId");
		if (Strings.isNullOrEmpty(userId)) {
			throw new IllegalArgumentException("missing user id");
		}

		BankingService.getInstance().pushAccountForUser(userId);
	}
}
