package com.example.demo1;

import javafx.scene.paint.PhongMaterial;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Util class to read json data
 */
public class JsonReader {

    /**
     * Read json data and return an Observation object
     * @param json the json data
     * @param materials the materials to use for the mesh
     * @return an Observation object
     */
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
            String type = feature.getString("type");
            if (type.equals("Feature")) {
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONObject properties = feature.getJSONObject("properties");
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


    /**
     * Read json data to get the ten first species name
     * @param json the json data
     * @return an array of string with the ten first species name
     */
    public static String[] getTenFirstNames(String json) {
        JSONArray arr = new JSONArray(json);
        int n = 10;
        if (arr.length() < n) n = arr.length();
        String[] names = new String[10];
        for (int i = 0; i < n; i++) names[i] = arr.getJSONObject(i).getString("scientificName");
        return names;
    }

    /**
     * Read json data to get some information about the species in the location
     * @param json the json data
     * @param allInfo true if you want all information, false if you want only the name
     * @return an array of string with the information
     */
    public static ArrayList<Releve> readReleve(String json,boolean allInfo) {
        JSONObject obj = new JSONObject(json);
        int total = obj.getInt("total");
        ArrayList<Releve> list = new ArrayList<>();
        if (total == 0) return list;
        JSONArray array = obj.getJSONArray("results");
        for (var r : array)
        {
            JSONObject releve = (JSONObject) r;
            String scientificName = "";
            String recordedBy = "";
            String order = "";
            String superClass = "";
            String species = "";
            if (releve.keySet().contains("scientificName")) scientificName = releve.getString("scientificName");
            if (releve.keySet().contains("order")) order = releve.getString("order");
            if (releve.keySet().contains("superorder")) superClass = releve.getString("superorder");
            if (releve.keySet().contains("recordedBy")) recordedBy = releve.getString("recordedBy");
            if (releve.keySet().contains("species")) species = releve.getString("species");
            list.add(new Releve(scientificName, order, superClass, recordedBy, species,allInfo));
        }
        return list;
    }

}
