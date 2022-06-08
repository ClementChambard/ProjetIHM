package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {

    public static Observation readJson(Group root, Scale scale, String json) {
        Observation obs = new Observation(scale);
        JSONObject obj = new JSONObject(json);
        JSONArray features = obj.getJSONArray("features");
        features.forEach(feature -> {
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
                }
            }
        });
        obs.genertateMesh();
        root.getChildren().add(obs);
        return obs;
    }

}
