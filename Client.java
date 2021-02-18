//Amrit Pandher
//2-2-21
//Assignment 1 CS320
//Main Method for testing

import java.util.*;

public class Client  {

    public static void main(String[] args) {
        Boolean x = true;
        IRouteFinder routeFinder = new RouteFinder();
        //Sara's Code
        while(x) {
            System.out.println("Please enter a letter that you destinations start with: ");
            Scanner scanner = new Scanner(System.in);
            String destInitial = scanner.nextLine();

            final Map<String, Map<String, String>> busRoutes =
                    routeFinder.getBusRoutesUrls(destInitial.charAt(0));

            //System.out.println("Sara: \n " + busRoutes);
            for (Map.Entry<String, Map<String, String>> busRoute : busRoutes.entrySet()) {
                System.out.println("Destination: " + busRoute.getKey());
                for(Map.Entry<String, String> busNumber : busRoute.getValue().entrySet()){
                    System.out.println("Bus Number: " + busNumber.getKey());
                }
                System.out.println("+++++++++++++++++++++++++++++++++++");
            }

            System.out.println("Please enter your destination: ");
            String destination = scanner.nextLine();
            System.out.println("Please enter a route ID: ");
            String routeId = scanner.nextLine();
            final Map<String, LinkedHashMap<String, String>> routeStops =
                    routeFinder.getRouteStops(busRoutes.get(destination).get(routeId));
            //System.out.println("Sara: /n " + routeStops);

            for(Map.Entry<String, LinkedHashMap<String, String>> routeStop : routeStops.entrySet()) {
                System.out.println("Destination: " + routeStop.getKey());
                for(Map.Entry<String, String> destinationStop : routeStop.getValue().entrySet()){
                    System.out.println(
                            "Stop number: " + destinationStop.getKey() + " is " + destinationStop.getValue()
                    );
                }
            }

            System.out.println("Do you want to check different destination? Please type Y to continue or press any other key to exit");
            Scanner s = new Scanner(System.in);
            String co = s.nextLine();

            if(!co.equalsIgnoreCase("y")){
                x = false; //break
            }
            //end of Sara's code
        }
    }
}
