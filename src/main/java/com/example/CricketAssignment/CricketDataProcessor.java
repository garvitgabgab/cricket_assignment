package com.example.CricketAssignment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CricketDataProcessor {

    static String RESULT = "result";

    public static void main(String[] args) {
        try {
            // Call the API and retrieve JSON data
            String jsonData = fetchCricketData();

            // Parse the JSON data
            JSONArray matches = new JSONObject(jsonData).getJSONArray("data");

            // Task 1: Find highest score in one innings
            int highestScore = 0;
            String teamWithHighestScore = "";

            // Task 2: Count matches with total 300+ score
            int matchesWith300PlusScore = 0;

            for (int i = 0; i < matches.length(); i++) {
                JSONObject match = matches.getJSONObject(i);
                String status = match.getString("ms");

                //Looking for only completed matches - Assumption
                if(!RESULT.equalsIgnoreCase(status))
                {
                    continue;
                }


                int team1Score = Integer.parseInt(match.getString("t1s").split("/")[0].trim());
                int team2Score = Integer.parseInt(match.getString("t2s").split("/")[0].trim());
                if (team1Score > highestScore) {
                    highestScore = team1Score;
                    teamWithHighestScore = match.getString("t1");
                }
                if (team2Score > highestScore) {
                    highestScore = team2Score;
                    teamWithHighestScore = match.getString("t2");
                }

                if (team1Score + team2Score >= 300) {
                    matchesWith300PlusScore++;
                }
            }

            // Print the results
            System.out.println("Highest Score in one innings: " + highestScore + " by " + teamWithHighestScore);
            System.out.println("Number of matches with total 300+ score from both teams: " + matchesWith300PlusScore);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //1. Getting the correct data from server
    private static String fetchCricketData() throws Exception {
        URL url = new URL("https://api.cuvora.com/car/partner/cricket-data");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("apiKey", "test-creds@2320");

        int responseCode = connection.getResponseCode();
        //Successful Response
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed to fetch cricket data. HTTP error code: " + responseCode);
        }
    }
}













