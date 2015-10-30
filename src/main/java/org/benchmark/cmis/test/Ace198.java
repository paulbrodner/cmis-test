package org.benchmark.cmis.test;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.benchmark.cmis.CmisRepository;
import org.benchmark.cmis.FileHelper;

import com.google.common.base.Stopwatch;

public class Ace198
{

    static CmisRepository cmis = new CmisRepository("alf01:8080", "admin", "admin", BindingType.BROWSER);
    static Session session;

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException
    {
        session = cmis.openSession();

        //Folder folder = (Folder) session.getObject("b40fe91d-3688-4b16-8775-cdc7b362a701");
        Folder folder = (Folder) session.getObject("111e4ee0-0738-435f-a56d-baea98510c7a");
        System.out.println("Playing on Folder: " + folder.getName());

        /*
         * create some DATALOAD prior of testing
         */
        FileHelper fileHelper = new FileHelper(session);
        Stopwatch timer = Stopwatch.createStarted();
        fileHelper.dataPreparation(folder, 2, 1, 5000); // 10000 files
        timer.stop();
        System.out.println("Creating Total of #Folders:" + fileHelper.getTotalFolderCount() + " #Files: " + fileHelper.getTotalFilesCount() + " in " + timer);

        /*
         * Start Benchmarking getChildren()
         */
        //bechmarkGetChildrens(folder);

        /*
         * Start Benchmarking getDescendants()
         */
        //bechmarkGetDescendants(folder, 10);

        System.out.println("END");
    }

    @SuppressWarnings("unused")
    private static void bechmarkGetDescendants(Folder folder, int depth)
    {
        // start timer
        Stopwatch timer = Stopwatch.createStarted();

        Iterator<Tree<FileableCmisObject>> pageItems = folder.getDescendants(depth).iterator();

        int count = 0;
        while (pageItems.hasNext())
        {
            count++;
            Tree<FileableCmisObject> item = pageItems.next();
            // System.out.println("Children: (" + item.getName() + " ID: " + item.getId());
        }
        // stop timer
        timer.stop();
        System.out.println("It took: " + timer + " for " + String.valueOf(count) + " Descendants on: " + folder.getName());
    }

    @SuppressWarnings("unused")
    private static void bechmarkGetChildrens(Folder folder)
    {
        // start timer
        Stopwatch timer = Stopwatch.createStarted();

        Iterator<CmisObject> pageItems = folder.getChildren().iterator();
        int count = 0;
        while (pageItems.hasNext())
        {
            count++;
            CmisObject item = pageItems.next();
            // System.out.println("Children: (" + item.getName() + " ID: " + item.getId());
        }
        // stop timer
        timer.stop();
        System.out.println("It took: " + timer + " for " + String.valueOf(count) + " Childrens on: " + folder.getName());
    }
}
