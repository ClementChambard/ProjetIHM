package com.example.demo1;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Releve extends VBox {

    private String scientificName;
    private String order;
    private String superclass;
    private String recordedBy;
    private String species;




    public Releve(String scientificName,String order,String superclass,String recordedBy,String species,boolean allinfo){
        super();

        this.scientificName= scientificName;

        this.order = order;
        this.superclass = superclass;
        this.recordedBy = recordedBy;
        this.species = species;
        Label labelNom = new Label();
        labelNom.setText(scientificName);
        getChildren().add(labelNom);
        if (allinfo) {
            if (!order.equals("")) {
                Label labelOrder = new Label();
                labelOrder.setText("order : " + order);
                getChildren().add(labelOrder);
            }
            if (!superclass.equals("")) {
                Label labelSuperclass = new Label();
                labelSuperclass.setText("superclass : " + superclass);
                getChildren().add(labelSuperclass);
            }
            if (!recordedBy.equals(""))
            {
                Label labelrecordBy = new Label();
                labelrecordBy.setText("recorded by : " + recordedBy);
                getChildren().add(labelrecordBy);
            }
            if (!species.equals("")) {
                Label labelspecies = new Label();
                labelspecies.setText("species : " + species);
                getChildren().add(labelspecies);
                labelspecies.setPadding(new Insets(0, 0, 3, 0));
            }


        }

        getChildren().add(new Separator());

    }

    public String getOrder() {
        return order;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getSpecies() {
        return species;
    }

    public String getSuperclass() {
        return superclass;
    }
}
