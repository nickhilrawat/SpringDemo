package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostmanCollectionUtility {

    public static void main(String[] args) {
        String directoryPath = "/Users/nickhilrawat/Desktop/Projects/jsons";
//        String outputPath = "/Users/nickhilrawat/Desktop/Projects/postman_collection.json";
        List<JSONObject> jsonObjects = readJsonFiles(directoryPath);

        JSONArray requests = createPostmanRequests(jsonObjects);
        JSONObject collection = createPostmanCollection(requests);

        try (FileWriter file = new FileWriter("postman_collection.json")) {
            file.write(collection.toString(4));
            System.out.println("Postman collection created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<JSONObject> readJsonFiles(String directoryPath) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    JSONTokener tokener = new JSONTokener(reader);
                    JSONObject jsonObject = new JSONObject(tokener);
                    jsonObjects.add(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObjects;
    }

    public static JSONArray createPostmanRequests(List<JSONObject> jsonObjects) {
        JSONArray requests = new JSONArray();

        for (JSONObject jsonObject : jsonObjects) {
            JSONObject request = new JSONObject();
            request.put("name", "Sample Request");
            request.put("request", new JSONObject()
                    .put("method", "POST")
                    .put("header", new JSONArray())
                    .put("body", new JSONObject()
                            .put("mode", "raw")
                            .put("raw", jsonObject.toString()))
                    .put("url", new JSONObject()
                            .put("raw", "http://example.com")
                            .put("host", new JSONArray().put("example").put("com"))
                            .put("protocol", "http")));

            requests.put(request);
        }
        return requests;
    }

    public static JSONObject createPostmanCollection(JSONArray requests) {
        JSONObject info = new JSONObject();
        info.put("name", "Sample Collection");
        info.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        JSONObject collection = new JSONObject();
        collection.put("info", info);
        collection.put("item", requests);

        return collection;
    }
}
