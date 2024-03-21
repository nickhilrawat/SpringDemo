package com.example;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class JSONComparator {

    public static void main(String[] args) {
        String json1Path = "/Users/nickhilrawat/Desktop/Nickhil_Projects/demo/src/main/java/com/example/path_to_json1.json";
        String json2Path = "/Users/nickhilrawat/Desktop/Nickhil_Projects/demo/src/main/java/com/example/path_to_json2.json";
        String outputAuditPath = "path_to_audit.json";

        try {
            JSONObject json1 = new JSONObject(readFile(json1Path));
            JSONObject json2 = new JSONObject(readFile(json2Path));

            JSONObject audit = compareJSONs(json1, json2);
            writeAudit(audit, outputAuditPath);

            System.out.println("Audit JSON file has been created successfully.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject compareJSONs(JSONObject json1, JSONObject json2) {
        JSONObject audit = new JSONObject();

        Iterator<String> keys1 = json1.keys();
        while (keys1.hasNext()) {
            String key = keys1.next();
            if (json2.has(key)) {
                Object value1 = json1.get(key);
                Object value2 = json2.get(key);
                if (!value1.equals(value2)) {
                    JSONObject diff = new JSONObject();
                    diff.put("old_value", value1);
                    diff.put("new_value", value2);
                    audit.put(key, diff);
                }
            } else {
                JSONObject diff = new JSONObject();
                diff.put("old_value", json1.get(key));
                diff.put("new_value", JSONObject.NULL);
                audit.put(key, diff);
            }
        }

        Iterator<String> keys2 = json2.keys();
        while (keys2.hasNext()) {
            String key = keys2.next();
            if (!json1.has(key)) {
                JSONObject diff = new JSONObject();
                diff.put("old_value", JSONObject.NULL);
                diff.put("new_value", json2.get(key));
                audit.put(key, diff);
            }
        }

        return audit;
    }

    public static void writeAudit(JSONObject audit, String filePath) throws IOException {
        Files.write(Paths.get(filePath), audit.toString(4).getBytes());
    }

    public static String readFile(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
}
