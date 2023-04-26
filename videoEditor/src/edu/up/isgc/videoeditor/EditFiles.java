package edu.up.isgc.videoeditor;

import java.io.File;
import java.io.IOException;
import java.util.List;

/* The class receives the weather description, temperature and the icons to overlay and show them inside each images asociated with the respective image*/
public class EditFiles {
    static void addWeatherText(List<String> weatherDescription,List<String> weatherTemp, File[] images, String listOfIconFiles) throws IOException, InterruptedException {
        ReadFiles.reloadFolder();
        String fontFile = "src/font/OpenSans-Bold.ttf";
        int counter =0;
        int index = 1;
        System.out.println("size: "+ weatherDescription.size());
        for (File file : images ) {
            if (counter<weatherDescription.size() && weatherDescription.get(counter) != null){
                String fileName = file.getName();
                System.out.println("Creating videos...");
                String iconImagePath = listOfIconFiles + "/weatherIcon" + index + ".png";
                String textDisplay = weatherTemp.get(counter) + "°C \n" + weatherDescription.get(counter);

                String[] command = {"ffmpeg", "-y", "-i", file.getAbsolutePath(), "-i", iconImagePath, "-hide_banner", "-filter_complex", "[1:v]scale=400:-1[ov];[0:v]transpose=1:passthrough=portrait,drawtext=text='" + textDisplay + "':fontfile=" + fontFile + ":fontcolor=blue:fontsize=100:x=20:y=200[txt];[txt][ov]overlay=x=800:y=50", file.getAbsolutePath()};

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                File path = new File("src/images");
                processBuilder.directory(path);
                Process process = processBuilder.start();
                process.waitFor();
                index+=1;
            }
            counter += 1;
        }
    }

}
