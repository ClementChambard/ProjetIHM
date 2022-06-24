package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Class for running junit tests.
 */
public class Tests {

    @Test
    public void loadLocalJson()
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
        assertEquals(8147, obs.getFeatures().get(0).getN());
        // on peut tester plus de valeurs
    }

    @Test
    public void loadRemoteJson()
    {
        try {
            Requete requete = new Requete("Delphinidae");
            Observation obs = requete.sendRequest();
        } catch (Exception e) {
            assert(false);
        }
        // comment tester que le fichier reçu est le bon ???
        // la réponse de l'API pourrait changer avec le temps
    }

    @Test
    public void loadRemoteJsonTime()
    {
        try {
            Requete requete = new Requete("Delphinidae");
            requete.setDebut(LocalDate.of(2000, 1, 1));
            requete.setFin(LocalDate.of(2022, 1, 1));
            Observation obs = requete.sendRequest();
        } catch (Exception e) {
            assert(false);
        }
        // comment tester que le fichier reçu est le bon ???
    }

    @Test
    public void inArea()
    {
        try {
            Requete.getGeohashData(GeoHashHelper.getGeohash(new Location("", 0, 0), 3));
        } catch (Exception e) {
            assert(false);
        }
        // comment tester que les infos reçues sont bonnes ??
    }

    @Test
    public void testGeoHash()
    {
        assertEquals("s00", GeoHashHelper.getGeohash(new Location("", 0, 0), 3));
        assertEquals("s0", GeoHashHelper.getGeohash(new Location("", 0, 0), 2));
        assertEquals("s", GeoHashHelper.getGeohash(new Location("", 0, 0), 1));
    }

    @Test
    public void infoArea()
    {
        try {
            Requete requete = new Requete("Delphinidae");
            requete.setDebut(LocalDate.of(2000, 1, 1));
            requete.setFin(LocalDate.of(2022, 1, 1));
            requete.getAtGeohash(GeoHashHelper.getGeohash(new Location("", 0, 0), 3));
        } catch (Exception e) {
            assert(false);
        }
        // comment tester que les infos reçues sont bonnes ?
    }

    @Test
    public void similarNames()
    {
        try {
            Requete requete = new Requete("Del");
            String[] similar = requete.sendRequestSimilar();
            assert(similar[0].startsWith("Del"));
            if (similar[1] != null) assert(similar[1].startsWith("Del"));
            if (similar[2] != null) assert(similar[2].startsWith("Del"));
            // on peut tester plus de valeurs
            assertEquals(10, similar.length);
        } catch (Exception e) {
            assert(false);
        }
    }
}
