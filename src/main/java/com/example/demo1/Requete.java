package com.example.demo1;

import java.net.URI;
import java.time.Duration;
import java.util.Date;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

public class Requete {

    private String scientific_name;
    private Date debut;
    private Date fin;

    public Requete(String scientific_name, Date debut, Date fin) {
        this.scientific_name = scientific_name;
        this.debut = debut;
        this.fin = fin;
    }

    public String getURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://api.obis.org/v3/occurrence/grid/3?scientificname=");
        sb.append(scientific_name);
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

        return JsonReader.readJson(json, Scale.baseColors);
    }

}
