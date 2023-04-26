package edu.up.isgc.videoeditor;
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Weather {
    static List<String> latitudesNumbers = new ArrayList<>();
    static List<String> longitudesNumbers = new ArrayList<>();
    static Map<Integer, Float> gpsLatitudes = new HashMap<Integer,Float>();
    static Map<Integer, Float> gpsLongitudes = new HashMap<Integer,Float>();

    static List<String> weatherList = new ArrayList<>();

    //Weather Data
    static List<String> weatherDescription = new ArrayList<>();
    static List<String> weatherIcon = new ArrayList<>();
    static List<String> weatherIconUrl = new ArrayList<>();
    static List<String> weatherTemp = new ArrayList<>();


    static void parseData(){
        List<String> locations = MetaData.gpsPositions;
        List<String> latitudes = new ArrayList<>();
        List<String> longitudes = new ArrayList<>();
        String latitudNumber;
        String longitudNumber;

        for (int i = 0; i < locations.size(); i++){
            if(i % 2 == 0){
                latitudes.add(locations.get(i));
            } else
                longitudes.add(locations.get(i));
        }

        for(int i=0; i<latitudes.size() && i<longitudes.size(); i++){
            latitudNumber = latitudes.get(i).replaceAll("[^0-9 |.]", " ");
            longitudNumber = longitudes.get(i).replaceAll("[^0-9 |.]", " ");
            latitudNumber = latitudNumber.replaceAll("\\s+", ",");
            longitudNumber = longitudNumber.replaceAll("\\s+", ",");

            latitudesNumbers.add(latitudNumber);
            longitudesNumbers.add(longitudNumber);

        }
    }
    static void latitudesToDecimal(){
        String element;
        float grades, minutes,seconds, result;

        for(int i=0; i<latitudesNumbers.size() ; i++){
            element = latitudesNumbers.get(i);
            ArrayList<String> numbers = new ArrayList<>(Arrays.asList(element.split(",")));
            grades = Float.parseFloat(numbers.get(1));
            minutes = Float.parseFloat(numbers.get(2));
            seconds = Float.parseFloat(numbers.get(3));
            result = (grades + (((float) 1 /60)*minutes) + (((float) 1 /3600)*seconds));
            gpsLatitudes.put(i,result);
        }
    }
    static void longitudesToDecimal(){
        String element;
        float grades, minutes,seconds, result;

        for(int i=0;  i<longitudesNumbers.size(); i++){
            element = longitudesNumbers.get(i);
            ArrayList<String> numbers = new ArrayList<>(Arrays.asList(element.split(",")));
            grades = Float.parseFloat(numbers.get(1));
            minutes = Float.parseFloat(numbers.get(2));
            seconds = Float.parseFloat(numbers.get(3));
            result = (grades + (((float) 1 /60)*minutes) + (((float) 1 /3600)*seconds));
            gpsLongitudes.put(i,result);
        }

    }

    static void weatherApiCall(){
        parseData();
        latitudesToDecimal();
        longitudesToDecimal();
        for (int i = 0; i < gpsLatitudes.size() && i < gpsLongitudes.size(); i++) {
            try {
                gpsLatitudes.get(i);
                ProcessBuilder processBuilder = new ProcessBuilder();
                String weatherKey = "YOUR API KEY";
                Process process = processBuilder.command("curl", "-X", "GET", "https://api.openweathermap.org/data/2.5/weather?lat=" +gpsLatitudes.get(i)+"&lon="+  gpsLongitudes.get(i) + "&appid=" + weatherKey).start();
                InputStream stream = process.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    weatherList.add(line);
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }

    }


    static void getWeatherData(){
        weatherApiCall();
        //weatherDescription
        getWeatherDescription();
        //weatherIcon
        getWeatherIcon();
        //weatherTemp
        getWeatherTemp();

        storeIconUrls();
        WeatherIcons.IconHttpCall();
    }

    static void getWeatherDescription(){
        for(int i=0; i< weatherList.size() ; i++ ) {
            List<String> weatherInformation = new ArrayList<>(Arrays.asList(weatherList.get(i).split(",")));
            String description;
            Pattern pattern = Pattern.compile("(?<=\"description\"\\:)[^,]+");
            Matcher matcher = pattern.matcher(weatherInformation.toString());
            if(matcher.find()) {
                description = matcher.group();
                description = description.replaceAll("\"", "");
                weatherDescription.add(description);
            }
        }
    }

    static void getWeatherIcon(){
        for(int i=0; i< weatherList.size() ; i++ ) {
            List<String> weatherInformation = new ArrayList<>(Arrays.asList(weatherList.get(i).split(",")));
            String icon;
            Pattern pattern = Pattern.compile("(?<=\"icon\"\\:)[^,]+");
            Matcher matcher = pattern.matcher(weatherInformation.toString());
            if(matcher.find()) {
                icon = matcher.group();
                icon = icon.replaceAll("\"", "");
                icon = icon.replaceAll("}|]|\s", "");
                weatherIcon.add(icon);
            }
        }
    }

    static void storeIconUrls(){
        for(int i=0; i<weatherIcon.size();i++ ){
            weatherIconUrl.add("https://openweathermap.org/img/wn/"+weatherIcon.get(i)+".png");
        }

    }
    static void getWeatherTemp(){
        for(int i=0; i< weatherList.size() ; i++ ) {
            List<String> weatherInformation = new ArrayList<>(Arrays.asList(weatherList.get(i).split(",")));
            String temp;
            Pattern pattern = Pattern.compile("(?<=\"temp\"\\:\\S{0,10})\\s*[\\d.]+");
            Matcher matcher = pattern.matcher(weatherInformation.toString());
            if(matcher.find()) {
                temp = matcher.group();
                double tempNumber = Double.parseDouble(temp);
                tempNumber = (tempNumber - 273.15);
                tempNumber = (int) tempNumber;
                weatherTemp.add(String.valueOf(tempNumber));
            }
        }
    }

}
