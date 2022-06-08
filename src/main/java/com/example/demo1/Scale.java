package com.example.demo1;

import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class Scale {

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
