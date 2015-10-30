package org.benchmark.cmis;

import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

/**
 * Hello world!
 */
public class App
{

    public static void main(String[] args)
    {

        CmisRepository cmis = new CmisRepository("alf01:8080", "admin", "admin", BindingType.BROWSER);
        Session session = cmis.openSession();

        System.out.println(session.getRootFolder().getName());

        Folder root = session.getRootFolder();

        CmisAction cmisAction = new CmisAction(session);
        System.out.println("Found the following objects in the ROOT folder:-");

        ItemIterable<CmisObject> children = cmisAction.getChildren(root);
        for (CmisObject o : children)
        {
            System.out.println(o.getName() + " which is of type " + o.getType().getDisplayName() + " and ID:" + o.getId());
        }

        // specific ID
        Iterator<CmisObject> pageItems = cmisAction.getChildren("74094c7c-de62-4977-838f-08475b1e910d").iterator();
        while (pageItems.hasNext())
        {
            CmisObject item = pageItems.next();
            System.out.println("Children: (" + item.getName() + " ID: " + item.getId());
        }

        System.out.println("DONE");
    }

}
