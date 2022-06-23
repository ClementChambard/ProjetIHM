package com.example.demo1;

public class Releve{

    private String scientificName;
    private String order;
    private String superclass;
    private String recordedBy;
    private String species;

    public Releve(String scientificName,String order,String superclass,String recordedBy,String species){
        this.scientificName= scientificName;
        this.order = order;
        this.superclass = superclass;
        this.recordedBy = recordedBy;
        this.species = species;

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
