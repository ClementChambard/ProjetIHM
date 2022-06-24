package com.example.demo1;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * class containing the data for visualizing an observation as a timelapse
 */
public class Timelapse {
    private final ArrayList<Observation> observations;
    private int currentFrame;
    private final Observation motherObservation ;
    private Timeline timeline;
    private final boolean invalid;

    /**
     * Getter for the mother observation
     * @return the mother observation
     */
    public Observation getMotherObservation() {
        return motherObservation;
    }

    /**
     * Getter for an observation at a given index
     * @param index the index of the frame
     * @return the observation at the given index
     */
    public Observation getObservation(int index)  {
        return observations.get(index);
    }

    /**
     * Getter for the next observation
     * @return the next observation
     */
    public Observation getNextObservation() {
        if (currentFrame >= observations.size()) currentFrame = 0;
        return observations.get(currentFrame++);
    }

    /**
     * Getter for the isInvalid flag
     * @return the isInvalid flag
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * Starts the timelapse if it is valid
     */
    public void start() {
        if (!invalid)
            timeline.play();
    }

    /**
     * Stops the timelapse if it is valid
     */
    public void pause() {
        if (!invalid)
            timeline.pause();
    }

    /**
     * Constructor for the timelapse
     * @param motherObservation the mother observation
     * @param c the controller (for some variables)
     */
    public Timelapse(Observation motherObservation, Controller c){
        this.observations = new ArrayList<>();
        this.motherObservation = motherObservation;
        this.currentFrame = 0;

        // check if the timelapse is valid
        if (motherObservation.getRequete().getDebut() == null || motherObservation.getRequete().getFin() == null)
        {
            invalid = true;
            return;
        }
        invalid = false;

        // generate the observations
        LocalDate s = motherObservation.getRequete().getDebut().plusYears(0);
        LocalDate e = motherObservation.getRequete().getDebut().plusYears(5);
        if (e.isAfter(motherObservation.getRequete().getFin())) e = motherObservation.getRequete().getFin().plusYears(0);
        boolean run = true;
        while (run){
            Requete requete = new Requete(motherObservation.getRequete().getScientific_name());
            requete.setDebut(s);
            requete.setFin(e);
            if (e.isEqual(motherObservation.getRequete().getFin())) run = false;
            observations.add(requete.sendRequest());
            s = e;
            e = e.plusYears(5);
            if (e.isAfter(motherObservation.getRequete().getFin())) e = motherObservation.getRequete().getFin().plusYears(0);


        }

        // create the timeline
        timeline = new Timeline(new KeyFrame(
                Duration.seconds(.5),
                ae -> {
                    Observation obs = getNextObservation();
                    c.replaceObs(obs);
                    c.getTimeLapsYear().setVisible(true);
                    c.replaceTimeLapsYear(obs.getRequete().getDebut(),obs.getRequete().getFin() );
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);

    }


}
