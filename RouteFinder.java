//Amrit Pandher
//2-2-21
//Assignment 1 CS320
//Class which implements IRouteFinder interface

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class RouteFinder implements IRouteFinder {

    private String getUrlText(String url){ //method to read webpage html into string
        String text = "";
        try {
            URLConnection transit = new URL(url).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(transit.getInputStream()));

            String inputLine = "";


            while ((inputLine = in.readLine()) != null) {
                text += inputLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private Matcher matcherCreate(String pattern, String text){ //method to reduce the amount of lines used to create a matcher using a pattern and text
        Pattern regPattern = Pattern.compile(pattern);
        Matcher regMatcher = regPattern.matcher(text);
        return regMatcher;
    }

    public Map<String, Map<String, String>> getBusRoutesUrls(char destInitial) throws RuntimeException {
        if(!Character.isLetter(destInitial)){ //check if input for initial is a letter
            throw new RuntimeException();
        }

        String text = getUrlText(TRANSIT_WEB_URL);

        char destInitialCap = Character.toUpperCase(destInitial); //allow for lowercase char entry

        String pattern = "(<h3>"+destInitialCap+"[\\s\\S]*?<hr)"; //grab needed section
        String pattern2 = "(<h3>[\\s\\S]*?<hr)"; //grab temporary subsection
        String pattern3 = "<h3>(.*?)</h3>"; //grab city names
        String pattern4 = "href=\"(.*)\".*>(.*)</a>"; //grab links and routes

        Matcher regMatcher = matcherCreate(pattern, text);

        String sectionMain = "";

        Map<String, Map<String, String>> Cities = new LinkedHashMap<>(); //store city names, routes
        Map<String,String> Routes = new LinkedHashMap<>(); //store route names, links

        while(regMatcher.find()){ //grabs all needed sections and concatenates Main text
            sectionMain += regMatcher.group(1); //this works for sure
        }

        regMatcher = matcherCreate(pattern2, sectionMain);

        Pattern regPattern2; Matcher regMatcher2; //will be needed in loop
        Pattern regPattern3; Matcher regMatcher3; //will be needed in loop

        while(regMatcher.find()){
            regMatcher2 = matcherCreate(pattern3, regMatcher.group(1)); //to find names
            regMatcher3 = matcherCreate(pattern4, regMatcher.group(1)); //to find hrefs

            Routes.clear(); //clean map to store routes
            while(regMatcher3.find()){
                Routes.put(regMatcher3.group(2),regMatcher3.group(1)); //add route and link to sub map
            }

            Map<String,String> tempRoutes = new LinkedHashMap<>(Routes); //deep copy
            while(regMatcher2.find()){
                Cities.putIfAbsent(regMatcher2.group(1),tempRoutes); //adds name, (route, link) to main map
            }
        }
        return Cities;
    }

    public Map<String, LinkedHashMap<String, String>> getRouteStops(String url) throws RuntimeException {
        //throws exception when route number does not exist

        String fullURL = "https://www.communitytransit.org/busservice/" + url; //concatenates to full url
        String text = getUrlText(fullURL);

        String pattern = "(<h2>Weekday[\\s\\S]*?<p class=)"; //should grab main section
        String pattern2 = "<h2>Weekday([\\s\\S]*?)</thead>"; //should grab sub section
        String pattern3 = "<small>(.*?)</small>"; //grabs destinations
        String pattern4 = "<p>(.*?)</p>"; //grabs stops

        Matcher regMatcher = matcherCreate(pattern, text);

        String sectionMain = "";

        Map<String, LinkedHashMap<String, String>> Destinations = new LinkedHashMap<>(); //store destination names, stops
        Map<String,String> Stops = new LinkedHashMap<>(); //store stop numbers, stops
        int count = 1; //counter used to keep track of stop number

        while(regMatcher.find()){ //grabs all needed sections and concatenates Main text
            sectionMain += regMatcher.group(1);
        }

        regMatcher = matcherCreate(pattern2, sectionMain);

        Pattern regPattern2; Matcher regMatcher2; //will be needed in loop
        Pattern regPattern3; Matcher regMatcher3; //will be needed in loop

        while(regMatcher.find()){
            regMatcher2 = matcherCreate(pattern3, regMatcher.group(1)); //to find Destinations
            regMatcher3 = matcherCreate(pattern4, regMatcher.group(1)); //to find Stops

            Stops.clear(); count=0; //clean map to store stops
            while(regMatcher3.find()){
                count++;
                Stops.put(String.valueOf(count),regMatcher3.group(1)); //add stop count and stops to sub map
            }
            LinkedHashMap<String,String> tempRoutes = new LinkedHashMap<>(Stops); //deep copy

            regMatcher2.find();
            Destinations.putIfAbsent(regMatcher2.group(1),tempRoutes); //adds names, (stop count, stops) to main map
        }
        return Destinations;
    }
}
