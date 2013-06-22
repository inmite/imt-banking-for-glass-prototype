package eu.inmite.glass.banking.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.inmite.glass.banking.rest.FioRESTClient;

public class FioRESTClientTestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String str = FioRESTClient.getInstance().fetchAccountInformation("jJ9itOlTXKiLXHEBRpMeXZCNe4RHSMwnf6YOY2hKRPrdYj3icikoftcWEvbbMAYM") + "";
		System.out.println(str);
	}
}
