package edu.up.isgc.videoeditor;

import java.io.*;
import java.net.URL;

import static edu.up.isgc.videoeditor.OpenAi.TextAi.openAiText;
import static edu.up.isgc.videoeditor.Weather.gpsLatitudes;
import static edu.up.isgc.videoeditor.Weather.gpsLongitudes;
import static edu.up.isgc.videoeditor.FileDate.sortedList;

/* Receives the first and last image name of the ordered dictionary to make the pinpointing image between those locations
 * Saves the image, to add the Aitext and convert it to video */
public class MapQuest {
   static Integer index;
    static int counter;
    public static String MapQuestImageName;
    public static String MapQuestVideoName;
    static void mapQuestApiCall(File[] folder) throws IOException {
        Integer firstIndex, lastIndex;
        firstIndex = sortedList.get(1).getValue();
        lastIndex = sortedList.get(sortedList.size()-1).getValue();
        String firstLatitudeLocation = String.valueOf(gpsLatitudes.get(firstIndex));
        String firstLongitudeLocation = String.valueOf(gpsLongitudes.get(firstIndex));

        String lastLatitudeLocation = String.valueOf(gpsLatitudes.get(lastIndex));
        String lastLongitudeLocation = String.valueOf(gpsLongitudes.get(lastIndex));

        String mapQuestApiKey = "YOUR API KEY";
        URL url = new URL("https://www.mapquestapi.com/staticmap/v5/map?key="+ mapQuestApiKey +"&locations="+firstLatitudeLocation+","+firstLongitudeLocation +"|marker-lg-A20000-D51A1A||"+lastLatitudeLocation+","+lastLongitudeLocation+"&size=@2x&defaultMarker=marker-sm-22407F-3B5998&zoom=1&size=1000,2000@2x");
        saveMapQuestImage(url, folder);
    }

    static void saveMapQuestImage(URL url, File[] folder) throws IOException {
        index = folder.length + 1;
        int size;
        byte[] image = new byte[2048];
        MapQuestImageName = "image" + index +".jpg";
        MapQuestVideoName = "video" + index +".mp4";
        InputStream input = url.openStream();
        OutputStream response = new FileOutputStream("src/images/"+MapQuestImageName);
        while ((size = input.read(image)) != -1) {
            response.write(image, 0, size);
        }
        input.close();
        response.close();
    }
    static void addText(File[] folder) throws IOException, InterruptedException {
        mapQuestApiCall(folder);
        int counter=folder.length + 1;
        String fontFile = "src/font/OpenSans-Bold.ttf";
        String textDisplay = openAiText;

        String[] command = {"ffmpeg", "-y", "-i", "image" + counter + ".jpg",
                "-hide_banner", "-vf", "transpose=1:passthrough=portrait,drawtext=text='" + textDisplay + "':fontfile=" + fontFile + ":fontcolor=black:fontsize=14:x=20:y=100",
                "image" + counter + ".jpg"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        File path = new File("src/images");
        processBuilder.directory(path);


        Process process = processBuilder.start();
        process.waitFor();
    }

    static void mapQuestVideo(File[] folder) throws IOException, InterruptedException {
        counter=folder.length + 1;
        String[] command = {"ffmpeg", "-loop", "1", "-i", "image" + counter + ".jpg", "-c:v", "libx264", "-t", "7", "-pix_fmt", "yuv420p", "video" + counter + ".mp4"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        File path = new File("src/images");
        processBuilder.directory(path);

        Process process = processBuilder.start();
        process.waitFor();
    }

    //Adjust the video to the rest of the videos format
    static void adjustVideo(File[] folder) throws IOException, InterruptedException {
        ReadFiles.reloadFolder();
         counter=folder.length + 1;
        String[] command = {"ffmpeg", "-y", "-i", "video" + counter + ".mp4", "-vf", "transpose=1:passthrough=portrait,scale=w='min(iw,(ih*9/16))':h='min(ih,(iw*16/9))',pad=w=ih*16/9:h=ih:x=(ow-iw)/2:y=(oh-ih)/2", "video" + counter + ".mp4"};
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                File path = new File("src/images");
                processBuilder.directory(path);

                Process process = processBuilder.start();
                process.waitFor();
    }

    static void generateImage(File[] folder) throws IOException, InterruptedException {
        addText(folder);
        mapQuestVideo(folder);
        adjustVideo(folder);
    }
}

