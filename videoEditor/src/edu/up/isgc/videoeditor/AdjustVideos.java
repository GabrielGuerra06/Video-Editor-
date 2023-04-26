package edu.up.isgc.videoeditor;

import java.io.File;
import java.io.IOException;

/* Receives the uploaded folder with the videos to give them format including the portrait mode, aspect ratio and full image. */
public class AdjustVideos {

    static void formatVideos(File[] folder) throws IOException, InterruptedException {
       ReadFiles.reloadFolder();
        int counter=0;
        for (File file : folder) {
//            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".heic")) {
                counter += 1;
            String[] command = {"ffmpeg", "-y", "-i", file.getName(), "-vf", "transpose=1:passthrough=portrait,scale=w='min(iw,(ih*9/16))':h='min(ih,(iw*16/9))',pad=w=ih*16/9:h=ih:x=(ow-iw)/2:y=(oh-ih)/2", file.getName()};

            ProcessBuilder processBuilder = new ProcessBuilder(command);
                File path = new File("src/images");
                processBuilder.directory(path);

                Process process = processBuilder.start();
                process.waitFor();
//            }
        }
    }

    //The functions sets the same framerate to all the videos to avoid repeated frames in the videos
    static void setFrameRate(File[] folder) throws IOException, InterruptedException {
        ReadFiles.reloadFolder();
        int counter=0;
        for (File file : folder) {
            if (file.getName().endsWith(".mp4") || file.getName().endsWith(".MP4") || file.getName().endsWith(".mov") || file.getName().endsWith(".MOV")) {
                counter += 1;
                String command = "ffmpeg -y -i " + file.getName() + " -r 25 -c:v libx264 -crf 23 -c:a copy temp_output.mp4 && mv temp_output.mp4 " + file.getName();
                ProcessBuilder processBuilder = new ProcessBuilder();
                File path = new File("src/images");
                processBuilder.directory(path);

                processBuilder.command("sh", "-c", command);
                Process process = processBuilder.start();
                process.waitFor();
            }
        }
    }

    //The mov extensions needs to be written as mp4 to concatenate the videos
    static void convertMovToMp4(File[] folder) throws IOException, InterruptedException {
        ReadFiles.reloadFolder();
        int counter=0;
        for (File file : folder) {
            if (file.getName().endsWith(".mov") || file.getName().endsWith(".MOV")) {
                counter += 1;
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String convertedFile = "output.mp4";
                String newFileName = fileName.replace("image", "video").replace("."+extension, ".mp4");

                String command = "ffmpeg -y -i " + file.getName() + " -codec:v libx264 -profile:v high -level:v 4.2 -preset slow -crf 18 -pix_fmt yuv420p -movflags +faststart -codec:a aac -b:a 192k -ac 2 -ar 48000 -strict -2 -y " + convertedFile + " && mv " + convertedFile + " " + newFileName;

                ProcessBuilder processBuilder = new ProcessBuilder();
                File path = new File("src/images");
                processBuilder.directory(path);

                processBuilder.command("sh", "-c", command);
                Process process = processBuilder.start();
                process.waitFor();
            }
        }
    }
}
