package com.example.demo1;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

/**
 * Contains the data of an observation.
 */
public class Observation extends Group {

    /**
     * corresponds to one quad with data
     * needs to be public to be read in the tests but should be private otherwise
     */
    public class Feature {
        private final float t, l, r, b;
        private final int n;

        /** Getter for the number of observations. */
        public int getN() { return n; }
        /** Getter for the top coordinate. */
        public float getT() { return t; }
        /** Getter for the bottom coordinate. */
        public float getB() { return b; }
        /** Getter for the left coordinate. */
        public float getL() { return l; }
        /** Getter for the right coordinate. */
        public float getR() { return r; }

        /** Constructor for a feature. */
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
    private final ArrayList<Feature> features;
    private Group groupFlat;
    private Group groupHisto;

    /**
     * Adds a feature to the observation.
     * @param t the top coordinate
     * @param l the left coordinate
     * @param r the right coordinate
     * @param b the bottom coordinate
     * @param n the number of observations
     */
    public void addFeature(float t, float l, float r, float b, int n) {
        features.add(new Feature(t, l, r, b, n));
    }

    /**
     * Get the features of the observation. (needed for the tests)
     * @return the features of the observation
     */
    public ArrayList<Feature> getFeatures() { return features; }

    /**
     * Getter for the scale of the observation.
     * @return the scale of the observation
     */
    public Scale getScale() {
        return scale;
    }

    /**
     * Getter for the request of the observation.
     * @return the request of the observation
     */
    public Requete getRequete() {
        return requete;
    }

    /**
     * sets the request of the observation.
     * @param requete the request of the observation
     */
    public void setRequete(Requete requete) {
        this.requete = requete;
    }

    /**
     * changes the display of the observation.
     * will generate the meshes if needed
     * @param histo true if the histogram should be displayed, false otherwise
     */
    public void setHisto(boolean histo) {
        if (!histo && groupFlat==null) groupFlat = genertateMesh(false);
        if (histo && groupHisto==null) groupHisto = genertateMesh(true);
        this.getChildren().clear();
        this.getChildren().add(histo ? groupHisto : groupFlat);

    }

    /**
     * sets the scale of the observation.
     * @param scale the scale of the observation
     */
    public void setScale(Scale scale) {
        this.scale = scale;
    }

    /**
     * generates the meshes of the observation.
     * @param histogram true if the histogram should be created, false otherwise
     * @return the meshes of the observation
     */
    public Group genertateMesh(boolean histogram)
    {
        Group g = new Group();
        this.getChildren().clear();
        this.getManagedChildren().clear();
        System.gc();
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
                Util3D.addQuadrilateral(g, utr, utl, ubl, ubr, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(g, utl, utr, tr, tl, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(g, ubr, ubl, bl, br, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(g, utr, ubr, br, tr, scale.getMaterialFromScale(f.n));
                Util3D.addQuadrilateral(g, ubl, utl, tl, bl, scale.getMaterialFromScale(f.n));
            }
            else
                Util3D.addQuadrilateral(g, tr, tl, bl, br, scale.getMaterialFromScale(f.n));
        }
        return g;
    }

    /**
     * Constructor for an observation.
     * @param scale the scale of the observation
     */
    public Observation(Scale scale) {
        this.scale = scale;
        features = new ArrayList<>();
    }

}
