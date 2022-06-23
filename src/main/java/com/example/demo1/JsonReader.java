package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
        if (nb != 0) avg /= nb;
        obs.setScale(new Scale(materials, min, max, avg,nb));

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

    public static ArrayList<Releve> readReleve(String json,boolean allinfo) {
        JSONObject obj = new JSONObject(json);
        int total = obj.getInt("total");
        ArrayList<Releve> list = new ArrayList<>();
        if (total == 0) return list;
        JSONArray array = obj.getJSONArray("results");
        for (var releve : array)
        {
            String scientificName = "";
            String recordedBy = "";
            String order = "";
            String superclass = "";
            String species = "";
            if (((JSONObject) releve).keySet().contains("scientificName")) scientificName = ((JSONObject)releve).getString("scientificName");
            if (((JSONObject) releve).keySet().contains("order")) order = ((JSONObject)releve).getString("order");
            if (((JSONObject) releve).keySet().contains("superorder")) superclass = ((JSONObject)releve).getString("superorder");
            if (((JSONObject) releve).keySet().contains("recordedBy")) recordedBy = ((JSONObject)releve).getString("recordedBy");
            if (((JSONObject) releve).keySet().contains("species")) species = ((JSONObject)releve).getString("species");
            list.add(new Releve(scientificName, order, superclass, recordedBy, species,allinfo));
        }
        return list;
    }

}
