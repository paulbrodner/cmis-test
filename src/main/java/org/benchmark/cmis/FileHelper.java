package org.benchmark.cmis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.benchmark.cmis.test.Ace198;

import com.google.common.io.Files;

public class FileHelper
{
    private Session session;
    private String[] dataFiles;
    Random random = new Random();

    private long totalFolderCount = 0;
    private long totalFilesCount = 0;

    public FileHelper(Session session)
    {
        this.session = session;
        File dataLocation = new File(Ace198.class.getClassLoader().getResource("files").getFile());
        dataFiles = dataLocation.list();
    }

    public Folder createRandomFolder(Folder destinationFolder)
    {
        String folderName = UUID.randomUUID().toString();
        Map<String, String> folderProps = new HashMap<String, String>();
        folderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        folderProps.put(PropertyIds.NAME, folderName);
        totalFolderCount++;
        return destinationFolder.createFolder(folderProps);
    }

    public Document createRandomFile(Folder destinationFolder)
    {
        int choosenOne = random.nextInt(dataFiles.length - 1);
        File file = new File(Ace198.class.getClassLoader().getResource("files/" + dataFiles[choosenOne]).getFile());
        String randomFileName = UUID.randomUUID().toString() + "." + Files.getFileExtension(file.getName());
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());

        ContentStream contentStream;
        try
        {
            contentStream = session.getObjectFactory().createContentStream(randomFileName, file.length(), mimeType + "; charset=UTF-8",
                    new FileInputStream(file));
            // Create the Document Object
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            properties.put(PropertyIds.NAME, randomFileName);
            Document doc = destinationFolder.createDocument(properties, contentStream, VersioningState.MINOR);

            totalFilesCount++;
            // System.out.println("Creating File: " + doc.getName());
            return doc;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void dataPreparation(Folder source, int depth, int subfolderCount, int filesPerFolder)
    {
        if (depth == 1)
        {
            return;
        }

        Folder folderParent = createRandomFolder(source);

        // create files for first level
        for (int f = 1; f <= filesPerFolder; f++)
        {
            createRandomFile(folderParent);
        }

        for (int s = 1; s <=subfolderCount; s++)
        {
            Folder subfolder = createRandomFolder(folderParent);

            // create files for first level
            for (int f = 1; f <= filesPerFolder; f++)
            {
                createRandomFile(subfolder);
            }

            dataPreparation(subfolder, depth - 1, subfolderCount, filesPerFolder);
        }
    }

    public long getTotalFolderCount()
    {
        return totalFolderCount;
    }

    public long getTotalFilesCount()
    {
        return totalFilesCount;
    }
}
