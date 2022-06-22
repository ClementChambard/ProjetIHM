package com.example.demo1;

import java.time.LocalDate;
import java.util.ArrayList;
import com.example.demo1.Observation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timelapse {
       private ArrayList<Observation> observations;
       private int currentFrame;
       private Observation motherObservation ;


    public Observation getMotherObservation() {
        return motherObservation;
    }

    public Observation getObservation(int index)  {
            return observations.get(index);
       }
        public Observation getNextObservation() { if (currentFrame >= observations.size()) currentFrame = 0; return observations.get(currentFrame++); }

       private Timeline timeline;
        private boolean invalid;

    public boolean isInvalid() {
        return invalid;
    }

    public void start() {
            if (!invalid)
            timeline.play();
        }

        public void pause() {
            if (!invalid)
            timeline.pause();
        }

    public Timelapse(Observation motherObservation, Controller c){
            this.observations = new ArrayList<>();
            this.motherObservation = motherObservation;
            if (motherObservation.getRequete().getDebut() == null || motherObservation.getRequete().getFin() == null)
            {
                invalid = true;
                return;
            }
            invalid = false;
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
            for (var o : observations) System.out.println(o);
            this.currentFrame = 0;

        timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
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
