package org.benchmark.cmis;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;

/**
 * @author Paul Brodner
 */
public class CmisAction
{
    private Session session;

    public CmisAction(Session session)
    {
        this.session = session;
    }

    /**
     * @param ID
     * @return the Folder object based on the String ID passed
     */
    public Folder getFolderFromID(String ID)
    {
        return (Folder) session.getObject(ID);
    }

    /**
     * @param ID
     * @return Iterable child cmis objects
     */
    public ItemIterable<CmisObject> getChildren(String folderID)
    {
        Folder folder = getFolderFromID(folderID);
        return getChildren(folder);
    }
    
    /**
     * @param ID
     * @return Iterable child cmis objects
     */
    public ItemIterable<CmisObject> getChildren(Folder folder)
    {
        return folder.getChildren();
    }

}
