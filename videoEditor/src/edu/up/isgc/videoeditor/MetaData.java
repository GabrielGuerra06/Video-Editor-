package edu.up.isgc.videoeditor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/* The class makes the Exiftool command to extract the createDate, and the GPSLocation, stores it in lists to call weatherAPI */
public class MetaData {
    static List<String> gpsPositions = new ArrayList<>();
    static List<String> dateData = new ArrayList<>();
    static File[] listOfFiles = ReadFiles.listOfFiles;

    static void renameFiles(File[] images) {
        for (int i = 0; i < images.length; i++) {
            int counter = i+1;
            String extension="";
            if (images[i].isFile()) {
                File f = new File("src/images/"+images[i].getName());
                String fileName = f.getName();
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index+1);
                }
                f.renameTo(new File("src/images/"+"image"+counter+"."+extension));
            }
        }
    }


    static void getData(File[] images) throws IOException {
        String filePath = "src/images";
        File folder = new File(filePath);
        listOfFiles = folder.listFiles();

        for(File file : listOfFiles){
                String name = file.getName();
            System.out.println("Extracting data...");
                ProcessBuilder processBuilder = new ProcessBuilder();
                File path = new File("src/images");
                processBuilder.directory(path);
                var command = "exiftool -GPSLatitude -GPSLongitude " + name;
                processBuilder.command("sh", "-c", command);

                Process process = processBuilder.start();
                OutputStream outputStream = process.getOutputStream();
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();

                appendGpsData(inputStream);
                appendGpsData(errorStream);

                outputStream.flush();
                outputStream.close();

            }

    }

    private static void appendGpsData(InputStream inputStream) throws IOException{
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] separate = line.split(":");
                String response = separate[1];
                gpsPositions.add(response);
            }
        }
    }

    static void getDate(File[] images) throws IOException {
        String filePath = "src/images";
        File folder = new File(filePath);
        listOfFiles = folder.listFiles();
        for(File file : listOfFiles){
                String name = file.getName();
                String[] command = {"exiftool", "-CreateDate", name};
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                File path = new File("src/images");
                processBuilder.directory(path);

                Process process = processBuilder.start();
                OutputStream outputStream = process.getOutputStream();
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();

                appendDateData(inputStream);
                appendDateData(errorStream);

                outputStream.flush();
                outputStream.close();

            }
    }

    private static void appendDateData(InputStream inputStream) throws IOException{
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                String response = line.replaceAll("Create Date|\\s{2,}", "");
                dateData.add(response);
            }
        }
    }

}
