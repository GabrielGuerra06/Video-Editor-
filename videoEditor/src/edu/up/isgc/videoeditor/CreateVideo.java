package edu.up.isgc.videoeditor;

import java.io.*;

/* The class recieves the mapQuest Video and the txt ordered file to make the convertion of images to videos and the concatenation */
public class CreateVideo {

    static File[] listOfFiles = ReadFiles.listOfFiles;

    static void createVideo() throws IOException, InterruptedException {
        MapQuest.generateImage(listOfFiles);

        ImagesToVideo(listOfFiles);

        VideoList.writeList(listOfFiles);
        concatVideos(VideoList.videoList);
    }

    static void ImagesToVideo(File[] folder) throws IOException, InterruptedException {
        int counter=0;

        for (File file : folder) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".heic")) {
                counter += 1;
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String newFileName = fileName.replace("image", "video").replace("."+extension, ".mp4");

                String[] command = {"ffmpeg", "-loop", "1", "-i", fileName, "-c:v", "libx264", "-t", "5", "-pix_fmt", "yuv420p", "-preset", "medium", "-b:v", "1000k", "-crf", "28", "-vf", "scale=960:-2", "-vsync", "1", newFileName};
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                File path = new File("src/images");
                processBuilder.directory(path);

                Process process = processBuilder.start();
                process.waitFor();
            }
            else{
                String fileName = file.getName();
                File f = new File("src/images/"+ fileName);
                String newFileName = fileName.replace("image", "video");
                f.renameTo(new File("src/images/"+newFileName));
            }
        }
    }



    static void concatVideos(File videoList) throws IOException, InterruptedException {

        String[] command = {"ffmpeg", "-f", "concat", "-i", videoList.getName(), "-c", "copy", "finalVideo.mp4"};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            File path = new File("src/images");
            processBuilder.directory(path);
            Process process = processBuilder.start();
            process.waitFor();

    }
}