package edu.up.isgc.videoeditor;

import java.io.*;


import static edu.up.isgc.videoeditor.MetaData.getData;
import static edu.up.isgc.videoeditor.MetaData.getDate;

/* Read Files receives and lists the files inside the images folder, to pass the File array to all the functions that require the image Folder */
public class ReadFiles {
    public static File[] listOfFiles;
    static void openDirectory() throws IOException {
        String filePath = "src/images";
        File folder = new File(filePath);
        listOfFiles = folder.listFiles();

        if(listOfFiles == null){
            return;
        }

        MetaData.renameFiles(listOfFiles); //Change the name of my files to have the same format

        getData(listOfFiles);
        getDate(listOfFiles);
    }

    static void reloadFolder(){
        String  filePath = "src/images";
        File folder = new File(filePath);
        listOfFiles = folder.listFiles();
    }

}