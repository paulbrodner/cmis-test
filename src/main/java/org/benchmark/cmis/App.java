package org.benchmark.cmis;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		CmisRepository cmis = new CmisRepository("alf01:8080", "admin","admin", BindingType.BROWSER);
		Session session = cmis.openSession();

		System.out.println(session.getRootFolder().getName());
	}

}
