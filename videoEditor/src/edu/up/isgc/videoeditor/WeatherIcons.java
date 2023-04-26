package edu.up.isgc.videoeditor;

import java.io.*;
import java.net.URL;
import java.util.List;

public class WeatherIcons {
    static List<String> weatherIconUrl =  Weather.weatherIconUrl;
    public static String listOfIconFiles;
    static void IconHttpCall() {
        String filePath = "src/icons";
        File folder = new File(filePath);
        listOfIconFiles = folder.getAbsolutePath();

        try {
            for (int i = 0; i < weatherIconUrl.size(); i++) {
                int counter = i+1;
                byte[] image = new byte[2048];
                int size;
                URL url = new URL(weatherIconUrl.get(i));
                InputStream input = url.openStream();
                OutputStream response = new FileOutputStream("src/icons/"+"weatherIcon" + counter + ".png");
                while ((size = input.read(image)) != -1) {
                    response.write(image, 0, size);
                }
                input.close();
                response.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

