package edu.up.isgc.videoeditor.OpenAi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Calls the openAI api to store the inspirantional phrase in a String var, and then passes it to the mapQuestImage*/
public class TextAi {
    public static String openAiText;
    static List<String> openAiResponse = new ArrayList<>();

    public static void openAiApiCall(Map<Integer, Float> gpsLatitudes, Map<Integer, Float> gpsLongitudes) throws IOException {
        List<String> locationsList = new ArrayList<>();
        String location;

        for(int i=0; i<gpsLatitudes.size() && i<gpsLongitudes.size(); i++){
            location = gpsLatitudes.get(i) + ":" + gpsLongitudes.get(i);
            locationsList.add(location);
        }

        String openAiKey = "YOUR API KEY";
        String model = "text-davinci-003";
        String prompt = "A inspirational phrase about these locations " + locationsList;

        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = processBuilder.command("curl", "-X", "POST", "https://api.openai.com/v1/completions" , "-H", "Content-Type: application/json", "-H", "Authorization: Bearer " + openAiKey,  "-d", "{\n  \"model\": \"" + model + "\",\n  \"prompt\": \"" + prompt + "\",\n \"n\": 1,\n \"max_tokens\": 30,\n  \"temperature\": 0.2\n}").start() ;

        InputStream stream = process.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        while ((line = reader.readLine()) != null) {
            openAiResponse.add(line);
        }
        saveText();
    }

    static void saveText(){
        Pattern pattern = Pattern.compile("(?<=\"text\"\\:)[^}]+?(?=\"index\")");
        Matcher matcher = pattern.matcher(openAiResponse.toString());
        if(matcher.find()) {
            openAiText = matcher.group();
            openAiText = openAiText.replaceAll("\"", "");
            openAiText = openAiText.replaceAll("\n", "");
        }
    }

}
