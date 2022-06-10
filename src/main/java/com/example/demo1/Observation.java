package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class Observation extends Group {

    private class Feature {
        private float t, l, r, b;
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

    public Requete getRequete() {
        return requete;
    }

    public void setRequete(Requete requete) {
        this.requete = requete;
    }



    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void genertateMesh(boolean histogram)
    {
        this.getChildren().clear();
        for (Feature f : features)
        {
            PhongMaterial mat = scale.getMaterialFromScale(f.n);
            if (mat == null) continue;
            float height = 1.01f;
            Point3D bl = Util3D.geoCoordTo3dCoord(f.b, f.l, height);
            Point3D br = Util3D.geoCoordTo3dCoord(f.b, f.r, height);
            Point3D tl = Util3D.geoCoordTo3dCoord(f.t, f.l, height);
            Point3D tr = Util3D.geoCoordTo3dCoord(f.t, f.r, height);
            if (histogram)
            {
                height += scale.getScaleFromScale(f.n)*0.03;
                Point3D ubl = Util3D.geoCoordTo3dCoord(f.b, f.l, height);
                Point3D ubr = Util3D.geoCoordTo3dCoord(f.b, f.r, height);
                Point3D utl = Util3D.geoCoordTo3dCoord(f.t, f.l, height);
                Point3D utr = Util3D.geoCoordTo3dCoord(f.t, f.r, height);
                Util3D.addQuadrilateral(this, utr, utl, ubl, ubr, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(this, utl, utr, tr, tl, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(this, ubr, ubl, bl, br, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(this, utr, ubr, br, tr, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(this, ubl, utl, tl, bl, scale.getMaterialFromScale(f.n));
            }
            else
                Util3D.addQuadrilateral(this, tr, tl, bl, br, scale.getMaterialFromScale(f.n));
        }
    }

    public Observation(Scale scale) {
        this.scale = scale;
        features = new ArrayList<>();
    }

}
