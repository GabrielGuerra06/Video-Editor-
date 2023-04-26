package edu.up.isgc.videoeditor;

import java.io.File;
import java.io.IOException;

import edu.up.isgc.videoeditor.OpenAi.ImageAi;
import edu.up.isgc.videoeditor.OpenAi.TextAi;


import static edu.up.isgc.videoeditor.ReadFiles.openDirectory;
import static edu.up.isgc.videoeditor.Weather.getWeatherData;

/* Execute all the functions with a folder reload between each file upload*/
public class Main {
    static File[] listOfFiles = ReadFiles.listOfFiles;

    public static void main(String[] args) throws IOException, InterruptedException {

        openDirectory();
        getWeatherData();


        String filePath = "src/images";
        File folder = new File(filePath);
        listOfFiles = folder.listFiles();


        FileDate.sortBydate(listOfFiles);
        VideoList.getOrder(listOfFiles);

        EditFiles.addWeatherText(Weather.weatherDescription, Weather.weatherTemp, listOfFiles, WeatherIcons.listOfIconFiles);

        TextAi.openAiApiCall(Weather.gpsLatitudes, Weather.gpsLongitudes);
        ImageAi.openAiApiCall(Weather.gpsLatitudes, Weather.gpsLongitudes,listOfFiles);



         filePath = "src/images";
         folder = new File(filePath);
        listOfFiles = folder.listFiles();

        AdjustVideos.formatVideos(ReadFiles.listOfFiles);

        filePath = "src/images";
        folder = new File(filePath);
        listOfFiles = folder.listFiles();

        AdjustVideos.setFrameRate(ReadFiles.listOfFiles);

        filePath = "src/images";
        folder = new File(filePath);
        listOfFiles = folder.listFiles();

        AdjustVideos.convertMovToMp4(ReadFiles.listOfFiles);

        CreateVideo.createVideo();

    }
}