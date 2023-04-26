package edu.up.isgc.videoeditor;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import static edu.up.isgc.videoeditor.EditFiles.addWeatherText;

/* FileDate calls the list of image createDate, convert it to miliseconds and compares which one should go first  */
public class FileDate {
    static List<String> fileNames = new ArrayList<>();
    public static  List<Entry<Long, Integer>> sortedList ;
    static void sortBydate(File[] images) {
        Map<Long, Integer> dateConverted = new HashMap<>();
        List<String> dates = MetaData.dateData;
        String[] dateInfo;
        int counter=0;
        for(File file : images){
                dateInfo = dates.get(counter).split(":|\\s");
                var date = new Date(Integer.parseInt(dateInfo[2]), Integer.parseInt(dateInfo[3]),Integer.parseInt(dateInfo[4]), Integer.parseInt(dateInfo[5]),Integer.parseInt(dateInfo[6]),Integer.parseInt(dateInfo[7]));
                Long convertedDate = date.getTime();
                dateConverted.put(convertedDate,counter);
                counter+=1;
        }

        sortedList = new ArrayList<>(dateConverted.entrySet());
        sortedList.sort(Entry.comparingByKey());
    }






}