package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {

    public static Observation readJson(String json, PhongMaterial[] materials) {
        Observation obs = new Observation(null);
        JSONObject obj = new JSONObject(json);
        JSONArray features = obj.getJSONArray("features");
        int min = 10000000;
        int max = 0;
        int avg = 0;
        int nb = 0;
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            String type = ((JSONObject) feature).getString("type");
            if (type.equals("Feature")) {
                JSONObject geometry = ((JSONObject) feature).getJSONObject("geometry");
                JSONObject properties = ((JSONObject) feature).getJSONObject("properties");
                int n = properties.getInt("n");
                String type2 = geometry.getString("type");
                if (type2.equals("Polygon")) {
                    JSONArray coordinates = geometry.getJSONArray("coordinates").optJSONArray(0);
                    float l = (float)coordinates.optJSONArray(0).getDouble(0);
                    float t = (float)coordinates.optJSONArray(0).getDouble(1);
                    float r = (float)coordinates.optJSONArray(2).getDouble(0);
                    float b = (float)coordinates.optJSONArray(2).getDouble(1);
                    obs.addFeature(t, l, r, b, n);
                    if (n < min) min = n;
                    if (n > max) max = n;
                    avg += n;
                    nb++;
                }
            }
        }
        avg /= nb;
        obs.setScale(new Scale(materials, min, max, avg));
        return obs;
    }

    public static String[] getTenFirstNames(String json) {
        JSONArray arr = new JSONArray(json);
        int n = 10;
        if (arr.length() < n) n = arr.length();
        String[] names = new String[10];
        for (int i = 0; i < n; i++) names[i] = arr.getJSONObject(i).getString("scientificName");
        return names;
    }

}
