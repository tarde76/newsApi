package com.company;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {
        System.out.println("Connecting to newsapi.org...");
        try {
            URL url = new URL("https://newsapi.org/v2/top-headlines?" +
                    "country=pl&" +
                    "category=business&" +
                    "apiKey=3e4424edf4af4550b4ac1bd12e1a74f8");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode !=200)
                throw new RuntimeException("HttpsResponseCode: " + responseCode);
            else{
                String jsonString = getJSONString(url);

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                String status = (String)jsonObject.get("status");
                System.out.printf("Successful: status %s",status);
                System.out.println();
                System.out.print("Enter file name for data export : ");
                Scanner scanner = new Scanner(System.in);
                String fileName = scanner.nextLine();
                JSONArray articles = (JSONArray)jsonObject.get("articles");

                writeToFile(articles, fileName);
                System.out.println("Data successfully written to file " + fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String getJSONString(URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream());
        StringBuilder jsonBuilder = new StringBuilder();
        while (scanner.hasNext()){
            jsonBuilder.append(scanner.nextLine());
        }
        return jsonBuilder.toString();
    }

    private static void writeToFile(JSONArray articles, String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (Object article : articles){
            JSONObject articleJsonObject = (JSONObject) article;
            String author = (String) articleJsonObject.get("author");
            author = author !=null ? author.trim() : "";
            String title = (String) articleJsonObject.get("title");
            title = title != null ? title.trim() : "";
            String description = (String) articleJsonObject.get("description");
            description = description != null ? description.trim() : "";
            printWriter.printf("%s:%s:%s", title, description, author);
            printWriter.println();
        }
        printWriter.close();
    }
}
