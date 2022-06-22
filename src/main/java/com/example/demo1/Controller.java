package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import com.interactivemesh.jfx.importer.ImportException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import com.example.demo1.AutoCompleteTextField;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    private Pane pane3D;

    private Observation obs = null;
    private Timelapse timelapse = null;


    private Point2D lastMousePosition = null;

    @FXML
    private Rectangle colorLegend1;

    @FXML
    private Rectangle colorLegend2;

    @FXML
    private Rectangle colorLegend3;

    @FXML
    private Rectangle colorLegend4;

    @FXML
    private Rectangle colorLegend5;

    @FXML
    private Rectangle colorLegend6;

    @FXML
    private Rectangle colorLegend7;

    @FXML
    private Rectangle colorLegend8;

    @FXML
    private Label textLabel1;

    @FXML
    private Label textLabel2;

    @FXML
    private Label textLabel3;

    @FXML
    private Label textLabel4;

    @FXML
    private Label textLabel5;

    @FXML
    private Label textLabel6;

    @FXML
    private Label textLabel7;

    @FXML
    private Label textLabel8;

    @FXML
    private CheckBox histoButton;

    @FXML
    private TextField scientificName;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;
    @FXML
    private ToggleButton start_stop;



    @FXML
    private ToggleButton stop;

    @FXML
    private Label timelapsLabel;

    @FXML
    private HBox commandTimelaps;

    @FXML
    private Label actualSpecie;

    @FXML
    private Label timeLapsYear;


    private boolean timeLapsOn = false;

    private AutoCompleteTextField autoCompleteTextField ;

    private Requete mainRequete = new Requete("");

    @FXML
    void togglehisto(){
        if ( obs != null) {
            obs.setHisto(histoButton.isSelected());

        }
    }

    @FXML private Button rechercheBtn;

    @FXML
    void stopTimelapse(){
        timeLapsYear.setVisible(false);
        start_stop.setText("|>");
        timeLapsOn = false;
        timelapse.pause();
        replaceObs(timelapse.getMotherObservation());
        stop.setDisable(true);
    }

    @FXML
    void rechercher() {
        if (obs != null) root3D.getChildren().remove(obs);
        obs = mainRequete.sendRequest();
        obs.setHisto(histoButton.isSelected());
        root3D.getChildren().add(obs);
        actualSpecie.setText(obs.getRequete().getScientific_name());
        timelapse = new Timelapse(obs, this);
        if ((mainRequete.getDebut().getYear()+5<mainRequete.getFin().getYear())||!timelapse.isInvalid()){
            commandTimelaps.setVisible(true);
            timelapsLabel.setVisible(true);
        }else{
            commandTimelaps.setVisible(false);
            timelapsLabel.setVisible(false);
            timeLapsYear.setVisible(false);
        }



    }

    private final Group root3D = new Group();

    @FXML
    void startChange()
    {
        mainRequete.setDebut(startDate.getValue());
    }

    @FXML
    void endChange()
    {
        mainRequete.setFin(endDate.getValue());
    }

    @FXML
    void start_stopTimelaps(){
       if (!timeLapsOn) {
           start_stop.setText("||");
            timelapse.start();
       }else {
           start_stop.setText("|>");
           timelapse.pause();
       }
        stop.setDisable(false);
        timeLapsOn = !timeLapsOn;
    }

    public void replaceObs(Observation obs1)
    {
        root3D.getChildren().remove(obs);
        obs = obs1;
        root3D.getChildren().add(obs);
        obs.setHisto(histoButton.isSelected());
    }

    public void replaceTimeLapsYear(LocalDate debut ,LocalDate fin){
        timeLapsYear.setText(debut.getYear()+" to "+fin.getYear());

    }

    public Label getTimeLapsYear() {
        return timeLapsYear;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Create a Pane et graph scene root for the 3D content

        Rectangle[] colorLegends = {colorLegend1, colorLegend2, colorLegend3, colorLegend4, colorLegend5, colorLegend6, colorLegend7, colorLegend8};
        Label[] textLabels = {textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, textLabel6, textLabel7, textLabel8};

        autoCompleteTextField = new AutoCompleteTextField(scientificName);
        autoCompleteTextField.setRequete(mainRequete);
        autoCompleteTextField.setBtn(rechercheBtn);





        //set color









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

       /* try (Reader reader = new FileReader("Delphinidae.json")) {
            BufferedReader rd = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            obs = JsonReader.readJson(sb.toString(), Scale.baseColors);
            obs.setHisto(false);
            root3D.getChildren().add(obs);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Requete rq = new Requete("selachii");
        root3D.getChildren().remove(obs);
        obs = rq.sendRequest();
        obs.setHisto(histoButton.isSelected());
        obs.getScale().setLegendView(colorLegends, textLabels);
        root3D.getChildren().add(obs);
        actualSpecie.setText(obs.getRequete().getScientific_name());
        timelapse = new Timelapse(obs, this);
        commandTimelaps.setVisible(false);
        timelapsLabel.setVisible(false);
        timeLapsYear.setVisible(false);

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(earth);
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
                //Requete.getGeohashData()
                //System.out.println(GeoHashHelper.getGeohash(location, 3));
            }
        });

    }
}