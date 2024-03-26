package com.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class NestedJsonComparator {
    public static void main(String[] args) {
        String oldJsonArray = "[{\"id\":1,\"name\":\"John\",\"addresses\":[{\"city\":\"New York\",\"subAddresses\":[{\"city\":\"New York1\"}]}]},{\"id\":2,\"name\":\"Alice\"}]";
        String newJsonArray = "[{\"id\":1,\"name\":\"John\",\"addresses\":[{\"city\":\"New York\",\"subAddresses\":[{\"city\":\"Los Angeles1\"}]}]},{\"id\":2,\"name\":\"Alice\"}]";

        try {
            JSONArray oldArray = new JSONArray(oldJsonArray);
            JSONArray newArray = new JSONArray(newJsonArray);

            JSONArray auditJson = compareJsonArrays(oldArray, newArray);

            System.out.println(auditJson.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray compareJsonArrays(JSONArray oldArray, JSONArray newArray) throws JSONException {
        JSONArray auditJson = new JSONArray();

        for (int i = 0; i < oldArray.length(); i++) {
            JSONObject oldObj = oldArray.getJSONObject(i);
            JSONObject newObj = newArray.getJSONObject(i);

            if (newObj != null) {
                JSONObject diff = compareJsonObjects(oldObj, newObj);
                if (diff != null && diff.length() > 0) {
                    JSONObject auditEntry = new JSONObject();
                    auditEntry.put("old", oldObj);
                    auditEntry.put("new", newObj);
                    auditJson.put(auditEntry);
                }
            }
        }

        return auditJson;
    }

    private static JSONObject findEquivalentObject(JSONArray array, JSONObject obj) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject newObj = array.getJSONObject(i);
            if (newObj.similar(obj)) {
                return newObj;
            }
        }
        return null;
    }

    private static JSONObject compareJsonObjects(JSONObject oldObj, JSONObject newObj) throws JSONException {
        JSONObject diff = new JSONObject();

        Iterator<String> keys = oldObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!newObj.has(key)) {
                diff.put(key, oldObj.get(key));
            } else {
                Object oldValue = oldObj.get(key);
                Object newValue = newObj.get(key);
                if (!oldValue.equals(newValue)) {
                    if (oldValue instanceof JSONObject && newValue instanceof JSONObject) {
                        JSONObject nestedDiff = compareJsonObjects((JSONObject) oldValue, (JSONObject) newValue);
                        if (nestedDiff.length() > 0) {
                            diff.put(key, nestedDiff);
                        }
                    } else if (oldValue instanceof JSONArray && newValue instanceof JSONArray) {
                        JSONArray nestedDiff = compareJsonArrays((JSONArray) oldValue, (JSONArray) newValue);
                        if (nestedDiff.length() > 0) {
                            diff.put(key, nestedDiff);
                        }
                    } else {
                        diff.put(key, new JSONArray().put(oldValue).put(newValue));
                    }
                }
            }
        }

        // Check for new keys in newObj
        keys = newObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!oldObj.has(key)) {
                diff.put(key, new JSONArray().put(JSONObject.NULL).put(newObj.get(key)));
            }
        }

        return diff;
    }
}
