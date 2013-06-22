package eu.inmite.glass.rest;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class FioRESTClient {
	
	private static FioRESTClient inst = null;
	private URLFetchService httpClient;
	
	private FioRESTClient() {
		super();
		httpClient = URLFetchServiceFactory.getURLFetchService();
	}
	
	public FioRESTClient getInstance() {
		if (inst == null) {
			inst = new FioRESTClient();
		}
		return inst;
	}

}
