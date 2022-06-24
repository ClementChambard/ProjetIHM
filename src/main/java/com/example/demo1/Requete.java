package com.example.demo1;

import com.example.demo1.GeoHash.GeoHash;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

/**
 * Contains the data of a request for an observation.
 * Can send requests to the API to get data for the observation
 */
public class Requete {

    private String scientific_name;
    private LocalDate debut;
    private LocalDate fin;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * sets the start date of the observation
     * @param debut the start date of the observation
     */
    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    /**
     * sets the end date of the observation
     * @param fin the end date of the observation
     */
    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    /**
     * sets the scientific name of the observation
     * @param scientific_name the scientific name of the observed specie
     * @return the similar names as what was given in the parameter
     */
    public String[] setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
        return sendRequestSimilar();
    }

    /**
     * Getter for the start date of the observation
     * @return the start date of the observation
     */
    public LocalDate getDebut() {
        return debut;
    }

    /**
     * Getter for the end date of the observation
     * @return the end date of the observation
     */
    public LocalDate getFin() {
        return fin;
    }

    /**
     * Getter for the scientific name of the observation
     * @return the scientific name of the observation
     */
    public String getScientific_name() {
        return scientific_name;
    }

    /**
     * Constructor for the class
     * @param scientific_name the scientific name of the observation
     */
    public Requete(String scientific_name) {
        this.scientific_name = scientific_name;
        this.debut = null;
        this.fin = null;
    }

    /**
     * Generate an url for the request to the API
     * @return the url for the request
     */
    public String getURL() {

        StringBuilder sb = new StringBuilder();
        sb.append("http://api.obis.org/v3/occurrence/grid/3?scientificname=");
        sb.append(scientific_name.replace(" ", "%20"));
        if (debut != null) {
            sb.append("&startdate=");
            sb.append(debut);
        }
        if (fin != null) {
            sb.append("&enddate=");
            sb.append(fin);
        }


        //"https://api.obis.org/v3/occurrence?scientificname=Carcharhinus&startdate=2000-01-01&enddate=2020-01-01" -H "accept: */*"
        return sb.toString();
    }

    /**
     * Send the observation request to the API and get the response
     * @return the observation
     */
    public Observation sendRequest() {
        String url = getURL();
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observation obs = JsonReader.readJson(json, Scale.baseColors);
        obs.setRequete(this);
        return obs;
    }

    /**
     * Send request to the API to get the similar names to the scientific name
     * @return the similar names
     */
    public String[] sendRequestSimilar() {
        String url = "https://api.obis.org/v3/taxon/complete/verbose/" + scientific_name.replace(" ", "%20");
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] n = JsonReader.getTenFirstNames(json);
        //for (var s : n) System.out.println(s);
        return n;
    }

    /**
     * Get the json response from the API for the observations at a given location
     * @param geoHash the location
     * @return the json response
     */
    public String getAtGeohash(String geoHash)
    {
        String url = "https://api.obis.org/v3/occurrence?scientificname="+ scientific_name.replace(" ", "%20") +"&geometry=" + geoHash;
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(json);
        return json;
    }

    /**
     * Get the json response from the API for the species at a given location
     * @param geoHash the location
     * @return the json response
     */
    public static String getGeohashData(String geoHash)
    {
        String url = "https://api.obis.org/v3/occurrence?geometry=" + geoHash;
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String[] names = JsonReader.getSpeciesList(json);
        //for (var s : names)
        //System.out.println(s);
        return json;
    }
}
