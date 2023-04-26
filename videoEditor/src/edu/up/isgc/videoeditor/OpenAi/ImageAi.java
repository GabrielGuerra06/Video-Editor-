package edu.up.isgc.videoeditor.OpenAi;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Makes the call to the OpenAI api to generate the image, store it and convert it to video with the correct format */
public class ImageAi {

    static String imageURL;
    static List<String> openAiResponse = new ArrayList<>();
    public static String AiFileName;
    public static String AiVideoName;

    public static void openAiApiCall(Map<Integer, Float> gpsLatitudes, Map<Integer, Float> gpsLongitudes, File[] folder) throws IOException {
         List<String> locationsList = new ArrayList<>();
         String location;

        for(int i=0; i<gpsLatitudes.size() && i<gpsLongitudes.size(); i++){
            location = gpsLatitudes.get(i) + ":" + gpsLongitudes.get(i);
            locationsList.add(location);
        }

        String openAiKey = "YOUR API KEY";
        String prompt = "A inspirational landscape about these locations " + locationsList;

        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = processBuilder.command("curl", "-X", "POST", "https://api.openai.com/v1/images/generations" , "-H", "Content-Type: application/json", "-H", "Authorization: Bearer " + openAiKey,  "-d", "{\n  \"prompt\": \"" + prompt + "\",\n  \"n\": 1,\n  \"size\": \"1024x1024\"\n}").start() ;

        InputStream stream = process.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        while ((line = reader.readLine()) != null) {
            openAiResponse.add(line);
        }
        saveImageURL();
        getOpenAiImage(folder);
    }

    static void saveImageURL(){
            Pattern pattern = Pattern.compile("(?<=\"url\"\\:)[^}]+");
            Matcher matcher = pattern.matcher(openAiResponse.toString());
            if(matcher.find()) {
                imageURL = matcher.group();
                imageURL = imageURL.replaceAll("\"", "");
                imageURL = imageURL.replaceAll(",", "");
            }
    }

    static void getOpenAiImage(File[] folder){
        try {
            Integer index = folder.length + 1;
            AiFileName= "image" + index + ".jpg";
            AiVideoName="video" + index + ".mp4";
                byte[] image = new byte[2048];
                int size;
                URL url = new URL(imageURL);
                InputStream input = url.openStream();
            System.out.println(AiFileName);
                OutputStream response = new FileOutputStream("src/images/" + AiFileName);
                while ((size = input.read(image)) != -1) {
                    response.write(image, 0, size);
                }
                input.close();
                response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
