package com.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

public class JsonArrayAudit {
    public static void main(String[] args) {
        JSONArray array1 = new JSONArray("[{\"name\":\"John\", \"id\":1}, {\"name\":\"Mary\", \"id\":2}]");
        JSONArray array2 = new JSONArray("[{\"name\":\"John\", \"id\":1}]");

        JSONArray auditArray = createAuditJSON(array1, array2);
        System.out.println(auditArray.toString(2)); // including indentation for pretty print
    }

    private static JSONArray createAuditJSON(JSONArray arr1, JSONArray arr2) {
        JSONArray auditArray = new JSONArray();
        int arraySize = (arr1.length() >= arr2.length()) ? arr1.length() : arr2.length();
        for (int i = 0; i < arraySize; i++) {
            JSONObject auditObj = getChangesFromJSONObject(arr1.optJSONObject(i), arr2.optJSONObject(i));
            if(auditObj.length() > 0)
                auditArray.put(auditObj);
        }
        return auditArray;
    }
    private static JSONObject getChangesFromJSONObject(JSONObject oldObj, JSONObject newObj) {
        JSONObject auditObj = new JSONObject();
        TreeSet<String> keys = new TreeSet<>();
        if(newObj != null)
            keys.addAll(newObj.keySet());
        if(oldObj != null)
            keys.addAll(oldObj.keySet());

        for (String key : keys) {
            Object oldValue = (oldObj != null) ? oldObj.opt(key) : null;
            Object newValue = (newObj != null) ? newObj.opt(key) : null;

            if(!Objects.equals(oldValue, newValue)) {
                JSONObject changeObj = new JSONObject();
                changeObj.put("old", oldValue);
                changeObj.put("new", Optional.ofNullable(newValue).orElse("null"));
                auditObj.put(key, changeObj);
            }
        }

        return auditObj;
    }
}