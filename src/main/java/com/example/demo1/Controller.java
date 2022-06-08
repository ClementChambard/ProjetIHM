package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import com.interactivemesh.jfx.importer.ImportException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Pane pane3D;

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

        // Draw a line

        // Draw a helix

        // Draw city on the earth
        Util3D.displayTown(root3D, "NYC", 40.639751f, -73.778925f);
        Util3D.displayTown(root3D, "Brest", 48.390394f, -4.486076f);
        Util3D.displayTown(root3D, "Marseille", 43.296482f, 5.369782f);
        Util3D.displayTown(root3D, "Capetown", -33.924966f, 18.423300f);
        Util3D.displayTown(root3D, "Istanbul", 41.008238f, 28.978359f);
        Util3D.displayTown(root3D, "Reykjavik", 64.135321f, -21.817439f);
        Util3D.displayTown(root3D, "Singapore", 1.352083f, 103.819839f);
        Util3D.displayTown(root3D, "Seoul", 37.566535f, 126.977969f);

        PhongMaterial redQuad = new PhongMaterial();
        redQuad.setDiffuseColor(new Color(1, 0, 0, 0.01));
        PhongMaterial greenQuad = new PhongMaterial();
        greenQuad.setDiffuseColor(new Color(0,1,0,0.01));
        PhongMaterial blueDot = new PhongMaterial(Color.BLUE);
        /*for (int i = -90; i < 90; i+= 5)
        {
            for (int j = -180; j < 180; j+= 5)
            {
                Point3D tl = Util3D.geoCoordTo3dCoord(i, j, 1.1f);
                Point3D tr = Util3D.geoCoordTo3dCoord(i, j+5, 1.1f);
                Point3D bl = Util3D.geoCoordTo3dCoord(i+5, j, 1.1f);
                Point3D br = Util3D.geoCoordTo3dCoord(i+5, j+5, 1.1f);
                Util3D.addQuadrilateral(root3D, tr, tl, bl, br,  ((((Math.abs(i)+Math.abs(j))/5))%2 == 1)?redQuad:greenQuad);
            }
        }*/
        Scale scale = new Scale();
        scale.add(new  PhongMaterial(new Color(0.25, 1, 0, 0.01)), 1);
        scale.add(new  PhongMaterial(new Color(0.5, 1, 0, 0.01)), 50);
        scale.add(new  PhongMaterial(new Color(0.75, 1, 0, 0.01)), 100);
        scale.add(new  PhongMaterial(new Color(1, 1, 0, 0.01)), 150);
        scale.add(new  PhongMaterial(new Color(1, 0.75, 0, 0.01)), 200);
        scale.add(new  PhongMaterial(new Color(1, 0.5, 0, 0.01)), 500);
        scale.add(new  PhongMaterial(new Color(1, 0.25, 0, 0.01)), 800);
        scale.add(redQuad, 1000);
        try (Reader reader = new FileReader("Delphinidae.json")) {
            BufferedReader rd = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            JsonReader.readJson(root3D, scale, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        subScene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subScene);

        // Create scene
        Sphere sss = new Sphere(0.03);
        sss.setMaterial(blueDot);
        root3D.getChildren().add(sss);
        subScene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {

                PickResult pickResult = event.getPickResult();
                Point3D spaceCoord = pickResult.getIntersectedPoint();
                sss.setTranslateX(spaceCoord.getX());
                sss.setTranslateY(spaceCoord.getY());
                sss.setTranslateZ(spaceCoord.getZ());

                Point2D loc = Util3D.SpaceCoordToGeoCoord(spaceCoord);
                // get geo-hash
                Location location = new Location("mouse", loc.getX(), loc.getY());
                System.out.println(GeoHashHelper.getGeohash(location));
            }
        });

    }
}