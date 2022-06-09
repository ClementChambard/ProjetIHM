package com.example.demo1;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class Scale {

    public static final PhongMaterial[] baseColors = {
            new PhongMaterial(new Color(0.25, 1, 0, 0.01)),
            new PhongMaterial(new Color(0.5, 1, 0, 0.01)),
            new PhongMaterial(new Color(0.75, 1, 0, 0.01)),
            new PhongMaterial(new Color(1, 1, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.75, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.5, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.25, 0, 0.01)),
            new PhongMaterial(new Color(1, 0, 0, 0.01))
    };

    public Scale() {}
    public Scale(PhongMaterial[] materials, int min, int max, int avg) {
        // add half the materials between min and avg and half between avg and max
        int nb = materials.length/2;
        int incSmall = (avg - min)/nb;
        int incBig = (max - avg)/nb;
        int n = min;
        int j = 0;
        for (int i = 0; i < nb; i++) {
            add(materials[j++], n);
            n += incSmall;
        }
        for (int i = 0; i < nb; i++) {
            add(materials[j++], n);
            n += incBig;
        }
    }

    private final ArrayList<PhongMaterial> materials = new ArrayList<>();
    private final ArrayList<Integer> scales = new ArrayList<>();

    public void add(PhongMaterial material, int scale) {
        materials.add(material);
        scales.add(scale);
    }

    public PhongMaterial getMaterial(int index) {
        return materials.get(index);
    }

    public int getScale(int index) {
        return scales.get(index);
    }

    public void setScale(int index, int scale) {
        scales.set(index, scale);
    }

    public PhongMaterial getMaterialFromScale(int scale) {
        int bestMatch = 10000000;
        int i = -1;
        for (int s : scales) if (scale > s)
        {
            if (scale - s < bestMatch)
            {
                bestMatch = scale - s;
                i = scales.indexOf(s);
            }
        }
        if (i == -1) return null;
        return materials.get(i);
    }

}
