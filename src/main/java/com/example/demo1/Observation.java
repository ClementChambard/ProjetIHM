package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class Observation extends Group {

    private class Feature {
        float t, l, r, b;
        private int n;

        public Feature(float t, float l, float r, float b, int n) {
            this.t = t;
            this.l = l;
            this.r = r;
            this.b = b;
            this.n = n;
        }
    }

    private Scale scale;
    private Requete requete;
    private ArrayList<Feature> features;

    public void addFeature(float t, float l, float r, float b, int n) {
        features.add(new Feature(t, l, r, b, n));
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void genertateMesh()
    {
        this.getChildren().clear();
        for (Feature f : features)
        {
            Point3D bl = Util3D.geoCoordTo3dCoord(f.b, f.l, 1.01f);
            Point3D br = Util3D.geoCoordTo3dCoord(f.b, f.r, 1.01f);
            Point3D tl = Util3D.geoCoordTo3dCoord(f.t, f.l, 1.01f);
            Point3D tr = Util3D.geoCoordTo3dCoord(f.t, f.r, 1.01f);

            PhongMaterial mat = scale.getMaterialFromScale(f.n);
            if (mat == null) continue;
            Util3D.addQuadrilateral(this, tr, tl, bl, br, scale.getMaterialFromScale(f.n));
        }
    }

    public Observation(Scale scale) {
        this.scale = scale;
        features = new ArrayList<>();
    }

}
