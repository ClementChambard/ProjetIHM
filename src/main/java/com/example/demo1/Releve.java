package com.example.demo1;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Releve extends VBox {

    private final String scientificName;
    private final String order;
    private final String superClass;
    private final String recordedBy;
    private final String species;


    private static Controller con;
    public static void setCon(Controller c) { con = c; }

    public Releve(String scientificName,String order,String superClass,String recordedBy,String species,boolean allInfo){
        super();

        this.scientificName= scientificName;

        this.order = order;
        this.superClass = superClass;
        this.recordedBy = recordedBy;
        this.species = species;
        Label labelNom = new Label();
        labelNom.setText(scientificName);
        getChildren().add(labelNom);
        if (allInfo) {
            if (!order.equals("")) {
                Label labelOrder = new Label();
                labelOrder.setText("order : " + order);
                getChildren().add(labelOrder);
            }
            if (!superClass.equals("")) {
                Label labelSuperclass = new Label();
                labelSuperclass.setText("superclass : " + superClass);
                getChildren().add(labelSuperclass);
            }
            if (!recordedBy.equals(""))
            {
                Label labelRecordBy = new Label();
                labelRecordBy.setText("recorded by : " + recordedBy);
                getChildren().add(labelRecordBy);
            }
            if (!species.equals("")) {
                Label labelSpecies = new Label();
                labelSpecies.setText("species : " + species);
                getChildren().add(labelSpecies);
                labelSpecies.setPadding(new Insets(0, 0, 3, 0));
            }
        }
        else
        {
            labelNom.setPadding(new Insets(0,0,6,0));
            setOnMouseClicked(e -> {
                con.replaceObs(new Requete(scientificName).sendRequest());
            });
            setOnMouseEntered(e -> {
                setBackground(Background.fill(new Color(.8f, .8f, .8f, 1.f)));
            });
            setOnMouseExited(e -> {
                setBackground(Background.EMPTY);
            });
            setCursor(Cursor.HAND);
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

    public String getSuperClass() {
        return superClass;
    }
}
