package com.example.demo1;

import com.example.demo1.GeoHash.GeoHash;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

public class Requete {

    private String scientific_name;
    private LocalDate debut;
    private LocalDate fin;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LocalDate getDebut() {
        return debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public String[] setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
        return sendRequestSimilar();
    }

    public Requete(String scientific_name) {
        this.scientific_name = scientific_name;
        this.debut = null;
        this.fin = null;
    }

    public String getURL() {

        StringBuilder sb = new StringBuilder();
        sb.append("http://api.obis.org/v3/occurrence/grid/3?scientificname=");
        sb.append(scientific_name);
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

    public String[] sendRequestSimilar() {
        String url = "https://api.obis.org/v3/taxon/complete/verbose/" + scientific_name;
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

    public String getAtGeohash(String geoHash)
    {
        String url = "https://api.obis.org/v3/occurrence?scientificname="+ scientific_name +"&geometry=" + geoHash;
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
        System.out.println(json);
        return json;
    }

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
