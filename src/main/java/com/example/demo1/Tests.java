package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import javafx.util.converter.LocalDateStringConverter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    void loadLocalJson()
    {
        Observation obs = new Observation(null);
        try (Reader reader = new FileReader("Delphinidae.json")) {
            BufferedReader rd = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            obs = JsonReader.readJson(sb.toString(), Scale.baseColors);
        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
        assertEquals(obs.getFeatures().get(0).getN(), 8147);
    }

    @Test
    void loadRemoteJson()
    {
        Requete requete = new Requete("Delphinidae");
        Observation obs = requete.sendRequest();
        // comment tester que le fichier reçu est le bon ???
        // la réponse de l'API pourrait changer avec le temps
    }

    @Test
    void loadRemoteJsonTime()
    {
        Requete requete = new Requete("Delphinidae");
        requete.setDebut(LocalDate.of(2000, 1, 1));
        requete.setFin(LocalDate.of(2022, 1, 1));
        Observation obs = requete.sendRequest();
        // comment tester que le fichier reçu est le bon ???
    }

    @Test
    void infoArea()
    {
        Requete requete = new Requete("Delphinidae");
        requete.setDebut(LocalDate.of(2000, 1, 1));
        requete.setFin(LocalDate.of(2022, 1, 1));
        requete.getAtGeohash(GeoHashHelper.getGeohash(new Location("", 0, 0), 3));
        // comment tester que les infos reçues sont bonnes
    }

}
