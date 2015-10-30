package org.benchmark.cmis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class CmisRepository {
	private String username = "admin";
	private String password = "admin";
	private String serverUrl = "alf01";
	private BindingType bindingValue;

	private static String ATOMPUB_URL_FORMAT = "http://%s/alfresco/api/-default-/public/cmis/versions/1.1/atom/";
	private static String BROWSER_URL_FORMAT = "http://%s/alfresco/api/-default-/public/cmis/versions/1.1/browser/";

	private Map<String, String> parameter = new HashMap<String, String>();

	public CmisRepository(String serverUrl, String username, String password, BindingType bindingValue) {
		setServerUrl(serverUrl);
		setUsername(username);
		setPassword(password);
		setBindingType(bindingValue);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}

	public void setBindingType(BindingType value) {
		if (value == BindingType.ATOMPUB) {
			// atompub binding
			parameter.put(SessionParameter.ATOMPUB_URL,
					String.format(ATOMPUB_URL_FORMAT, getServerUrl()));
			parameter.put(SessionParameter.BINDING_TYPE,
					BindingType.ATOMPUB.value());
			System.out.println("Accessing ATOMPUB_URL: "
					+ parameter.get(SessionParameter.ATOMPUB_URL));
		} else if (value == BindingType.BROWSER) {
			// browser binding
			parameter.put(SessionParameter.BROWSER_URL,
					String.format(BROWSER_URL_FORMAT, getServerUrl()));
			parameter.put(SessionParameter.BINDING_TYPE,
					BindingType.BROWSER.value());
			System.out.println("Accessing BROWSER_URL: "
					+ parameter.get(SessionParameter.BROWSER_URL));
		}
	}

	public Session openSession() {
		// Create a SessionFactory and set up the SessionParameter map
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

		// user credentials - using the standard admin/admin
		parameter.put(SessionParameter.USER, getUsername());
		parameter.put(SessionParameter.PASSWORD, getPassword());

		setBindingType(getBindingValue());

		// find all the repositories at this URL - there should only be one.
		List<Repository> repositories = new ArrayList<Repository>();
		repositories = sessionFactory.getRepositories(getParameter());
		for (Repository r : repositories) {
			System.out.println("Found repository: " + r.getId());
		}

		Repository repository = repositories.get(0);
		parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());

		System.out.println("Got a connection to Repository: "
				+ repository.getName() + ", with id: " + repository.getId());

		Session session = sessionFactory.createSession(getParameter());
		return session;
	}

	public BindingType getBindingValue() {
		return bindingValue;
	}

	@SuppressWarnings("unused")
	private void setBindingValue(BindingType bindingValue) {
		this.bindingValue = bindingValue;
	}

}
