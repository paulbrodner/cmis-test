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

        Folder folder = (Folder) session.getObject("56b90363-fd33-469d-928a-a97610970efc");
        // Folder folder = (Folder) session.getObject("76eb2043-f799-4792-981f-32b4a6feb1b4");
        System.out.println("Playing on Folder: " + folder.getName());

        /*
         * create some DATALOAD prior of testing
         */
        createDataLoad(folder, 2, 2, 1000);

        /*
         * Start Benchmarking getChildren()
         */
        // bechmarkGetChildrens(folder);

        /*
         * Start Benchmarking getDescendants()
         */
        // bechmarkGetDescendants(folder, 10);

        System.out.println("END");
    }

    private static void createDataLoad(Folder folder, int depth, int subfolderCount, int filesPerFolder)
    {
        FileHelper fileHelper = new FileHelper(session);
        Stopwatch timer = Stopwatch.createStarted();

        fileHelper.dataPreparation(folder, depth, subfolderCount, filesPerFolder);

        timer.stop();
        System.out.println("Creating Total of #Folders:" + fileHelper.getTotalFolderCount() + " #Files: " + fileHelper.getTotalFilesCount() + " in " + timer);
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
