package edu.up.isgc.videoeditor;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Receives the folder uploaded to write in the txt the correct order of the videos, the txt made its used to concat the videos */

public class VideoList {
    static File videoList = new File("src/images/videoList.txt");
    static List<String> videoOrder = new ArrayList<>();
    static List<Map.Entry<Long, Integer>> sortedList = FileDate.sortedList;
    static void writeList(File[] listOfFiles) {
        try {
            String filePath = "src/images";
            File folder = new File(filePath);
            listOfFiles = folder.listFiles();

            FileOutputStream txtFile = new FileOutputStream(videoList);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(txtFile));

            for (String name : videoOrder) {

                    bw.write("file " + "'" + name + "'");
                    bw.newLine();
            }
            bw.close();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    static void getOrder(File[] images){
        int aiIndex = images.length + 1;
        int mpIndex = images.length + 2;
        String aiImage = "video" + aiIndex + ".mp4";
        String mpImage = "video" + mpIndex + ".mp4";
        videoOrder.add(aiImage);
        int counter=0;
        for (File file : images) {
            int index = sortedList.get(counter).getValue();
            String fileName = images[index].getName();
            if (fileName.endsWith(".jpg")|| fileName.endsWith(".png") || fileName.endsWith(".heic")){
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String newFileName = fileName.replace("image", "video").replace("."+extension, ".mp4");
                videoOrder.add(newFileName);
            }
            else if (fileName.endsWith(".MOV") || fileName.endsWith(".mov") ) {
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String newFileName = fileName.replace("image", "video").replace("."+extension, ".mp4");
                videoOrder.add(newFileName);
            }

            else{
                String newFileName = fileName.replace("image", "video");
                videoOrder.add(newFileName);
            }
            counter+=1;
        }

        videoOrder.add(mpImage);
    }
}
