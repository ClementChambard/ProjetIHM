package com.example.demo1;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Contains the color and scale values for an observation.
 */
public class Scale {

    private final ArrayList<PhongMaterial> materials = new ArrayList<>();
    private final ArrayList<Integer> scales = new ArrayList<>();

    /**
     * The base colors for the scale.
     */
    public static final PhongMaterial[] baseColors = {
            new PhongMaterial(new Color(0.25, 1, 0.35, 0.01)),
            new PhongMaterial(new Color(0.5, 1, 0, 0.01)),
            new PhongMaterial(new Color(0.75, 1, 0, 0.01)),
            new PhongMaterial(new Color(1, 1, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.75, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.5, 0, 0.01)),
            new PhongMaterial(new Color(1, 0.25, 0.35, 0.01)),
            new PhongMaterial(new Color(1, 0, 0, 0.01))
    };

    /**
     * Empty constructor.
     */
    public Scale() {}

    /**
     * Constructor.
     * @param min The minimum value of the observation.
     * @param max The maximum value of the observation.
     * @param avg The average value of the observation.
     * @param nb The number of values of the observation.
     */
    public Scale(PhongMaterial[] materials, int min, int max, int avg,int nb) {
        // add half the materials between min and avg and half between avg and max
        //int nb = materials.length/2;
        //int incSmall = (avg - min)/nb;
        //int incBig = (max - avg)/nb;
        //int n = min;
        //int j = 0;
        //for (int i = 0; i < nb*2; i++) {
        //    add(materials[j++], n);
        //    n += incSmall;
        //}
        //for (int i = 0; i < nb; i++) {
        //    add(materials[j++], n);
        //    n += incBig;
        //}*/


        //simple scale
        add(materials[0],  0);
        add(materials[1],  10);
        add(materials[2],20);
        add(materials[3],50);
        add(materials[4],100);
        add(materials[5],200);
        add(materials[6],500);
        add(materials[7],1000);
    }


    /**
     * Add a value to the scale.
     * @param material The material to use for the value.
     * @param scale The value.
     */
    public void add(PhongMaterial material, int scale) {
        materials.add(material);
        scales.add(scale);
    }

    /**
     * Sets the scale value for a given index.
     * @param index The index of the value to set.
     * @param scale The value to set.
     */
    public void setScale(int index, int scale) {
        scales.set(index, scale);
    }

    /**
     * Get the material for a given index.
     * @param index The index
     * @return The material.
     */
    public PhongMaterial getMaterial(int index) {
        return materials.get(index);
    }

    /**
     * Get the scale value for a given index.
     * @param index The index
     * @return The scale value.
     */
    public int getScale(int index) {
        return scales.get(index);
    }

    /**
     * Get the index of the scale value for a given value.
     * @param scale The value.
     * @return The index.
     */
    public  int getScaleFromScale(int scale) {
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
        if (i == -1) return 0;
        return i;
    }

    /**
     * Get the material for a given value.
     * @param scale The value.
     * @return The material.
     */
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

    /**
     * Fill the rectangles and labels with the scale values and colors.
     * @param legendView the rectangles to fill.
     * @param textLabels the labels to fill.
     */
    public void setLegendView(Rectangle[] legendView, Label[] textLabels) {
        for (int i = 0; i < legendView.length; i++) {
            legendView[i].setFill(Color.color(getMaterial(i).getDiffuseColor().getRed(), getMaterial(i).getDiffuseColor().getGreen(), getMaterial(i).getDiffuseColor().getBlue(), 1));

            if (i==textLabels.length-1) {
                textLabels[i].setText("> "+getScale(i));
            }else{
                textLabels[i].setText(getScale(i)+1 + " - " + getScale(i+1));
            }


        }
    }

}
