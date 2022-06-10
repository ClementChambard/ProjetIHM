package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import com.interactivemesh.jfx.importer.ImportException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Pane pane3D;

    private Observation obs = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modeUrl = this.getClass().getResource("earth.obj");
            objImporter.read(modeUrl);
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);

        root3D.getChildren().add(earth);

        PhongMaterial dot = new PhongMaterial(Color.RED);

        try (Reader reader = new FileReader("Delphinidae.json")) {
            BufferedReader rd = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            obs = JsonReader.readJson(sb.toString(), Scale.baseColors);
            obs.genertateMesh(false);
            root3D.getChildren().add(obs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Requete rq = new Requete("selachii", new Date(), new Date());
        root3D.getChildren().remove(obs);
        obs = rq.sendRequest();
        obs.genertateMesh(true);
        root3D.getChildren().add(obs);

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        SubScene subScene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(new Color(0.1, 0.1, 0.12, 1));
        pane3D.getChildren().addAll(subScene);

        // Create scene
        Sphere sss = new Sphere(0.02);
        sss.setMaterial(dot);
        root3D.getChildren().add(sss);
        subScene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isControlDown()) {

                PickResult pickResult = event.getPickResult();
                Point3D spaceCoord = pickResult.getIntersectedPoint();
                sss.setTranslateX(spaceCoord.getX());
                sss.setTranslateY(spaceCoord.getY());
                sss.setTranslateZ(spaceCoord.getZ());

                Point2D loc = Util3D.SpaceCoordToGeoCoord(spaceCoord);
                // get geo-hash
                Location location = new Location("mouse", loc.getX(), loc.getY());
                obs.getRequete().getAtGeohash(GeoHashHelper.getGeohash(location, 3));
                //System.out.println(GeoHashHelper.getGeohash(location, 3));
            }
        });

    }
}