package com.example.demo1;

import com.example.demo1.GeoHash.GeoHashHelper;
import com.example.demo1.GeoHash.Location;
import com.interactivemesh.jfx.importer.ImportException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * controller for the main window
 */
public class Controller implements Initializable {
    @FXML
    private Pane pane3D;

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

    @FXML
    private VBox releveScroll;

    @FXML
    private VBox especeScroll;

    @FXML private Button rechercheBtn;

    /**
     * Action function for the 'histo' checkbox
     * will toggle the histogram display
     */
    @FXML
    void togglehisto(){
        if ( obs != null) {
            obs.setHisto(histoButton.isSelected());

        }
    }

    /**
     * Action function for the 'recherche' button
     * will launch the request and display the result
     */
    @FXML
    void rechercher() {
        if (obs != null) root3D.getChildren().remove(obs);
        replaceObs(mainRequete.sendRequest());

        timelapse = new Timelapse(obs, this);
        if (!(mainRequete.getDebut()==null || mainRequete.getFin()==null) && mainRequete.getDebut().getYear()+5<mainRequete.getFin().getYear()||!timelapse.isInvalid()){
            commandTimelaps.setVisible(true);
            timelapsLabel.setVisible(true);
        }else{
            commandTimelaps.setVisible(false);
            timelapsLabel.setVisible(false);
            timeLapsYear.setVisible(false);
        }
    }

    /**
     * Action function for the start date picker
     * will update the start date of the request
     */
    @FXML
    void startChange()
    {
        mainRequete.setDebut(startDate.getValue());
    }

    /**
     * Action function for the end date picker
     * will update the main request with the new date
     */
    @FXML
    void endChange()
    {
        mainRequete.setFin(endDate.getValue());
    }

    /**
     * Action function for the 'stop' button
     * will stop the timelapse
     */
    @FXML
    void stopTimelapse(){
        timeLapsYear.setVisible(false);
        start_stop.setText("\u25B6");
        timeLapsOn = false;
        timelapse.pause();
        replaceObs(timelapse.getMotherObservation());
        stop.setDisable(true);
    }

    /**
     * Action function for the start/stop button.
     * will start or pause the timelapse.
     */
    @FXML
    void start_stopTimelaps(){
       if (!timeLapsOn) {
           start_stop.setText("||");
            timelapse.start();
       }else {
           start_stop.setText("\u25B6");
           timelapse.pause();
       }
        stop.setDisable(false);
        timeLapsOn = !timeLapsOn;
    }

    /**
     * replaces the currently shown observation with a new one
     * @param obs the new observation
     */
    public void replaceObs(Observation obs)
    {
        root3D.getChildren().remove(this.obs);
        this.obs = obs;
        root3D.getChildren().add(obs);
        obs.setHisto(histoButton.isSelected());
        actualSpecie.setText(obs.getRequete().getScientific_name());
    }

    /**
     * Changes the text of the label of the current timelapse interval
     * @param debut the start date of the timelapse interval
     * @param fin the end date of the timelapse interval
     */
    public void replaceTimeLapsYear(LocalDate debut ,LocalDate fin){
        timeLapsYear.setText(debut.getYear()+" to "+fin.getYear());
    }

    /**
     * Getter for the label of the current shown year of the timelapse
     * @return the label of the current year of the timelapse
     */
    public Label getTimeLapsYear() {
        return timeLapsYear;
    }

    private final Group root3D = new Group();
    private Observation obs = null;
    private Timelapse timelapse = null;
    private ArrayList<Releve> listReleve= null;
    private ArrayList<Releve> listeSpecies = null;
    private boolean timeLapsOn = false;
    private AutoCompleteTextField autoCompleteTextField ;
    private Requete mainRequete = new Requete("");

    /**
     * Initializes everything
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // initialize instance variables
        Rectangle[] colorLegends = {colorLegend1, colorLegend2, colorLegend3, colorLegend4, colorLegend5, colorLegend6, colorLegend7, colorLegend8};
        Label[] textLabels = {textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, textLabel6, textLabel7, textLabel8};

        autoCompleteTextField = new AutoCompleteTextField(scientificName);
        autoCompleteTextField.setRequete(mainRequete);
        autoCompleteTextField.setBtn(rechercheBtn);

        commandTimelaps.setVisible(false);
        timelapsLabel.setVisible(false);
        timeLapsYear.setVisible(false);

        Releve.setCon(this);

        // Easter Egg : copy the specie name when clicking on the text field
        actualSpecie.setOnMouseClicked(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(actualSpecie.getText());
            clipboard.setContent(content);

        });

        // Load observation from local file
        try (Reader reader = new FileReader("Delphinidae.json")) {
            BufferedReader rd = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            obs = JsonReader.readJson(sb.toString(), Scale.baseColors);
            obs.setHisto(false);
            obs.setRequete(mainRequete);
            obs.getScale().setLegendView(colorLegends, textLabels);
            root3D.getChildren().add(obs);
            actualSpecie.setText("Delphinidae");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the timelapse for the observation
        timelapse = new Timelapse(obs, this);

        // Load online data
        //Requete rq = new Requete("selachii");
        //root3D.getChildren().remove(obs);
        //obs = rq.sendRequest();
        //obs.setHisto(histoButton.isSelected());
        //obs.getScale().setLegendView(colorLegends, textLabels);
        //root3D.getChildren().add(obs);
        //actualSpecie.setText(obs.getRequete().getScientific_name());

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
        PhongMaterial dotMaterial = new PhongMaterial(Color.RED);
        Sphere selectionDot = new Sphere(0.02);
        selectionDot.setMaterial(dotMaterial);
        root3D.getChildren().add(selectionDot);

        // Control + click
        subScene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isControlDown()) {

                // Get the mouse position
                PickResult pickResult = event.getPickResult();
                Point3D spaceCoord = pickResult.getIntersectedPoint();
                selectionDot.setTranslateX(spaceCoord.getX());
                selectionDot.setTranslateY(spaceCoord.getY());
                selectionDot.setTranslateZ(spaceCoord.getZ());

                // get geo-hash for the selected point
                Point2D loc = Util3D.SpaceCoordToGeoCoord(spaceCoord);
                String geoHash = GeoHashHelper.getGeohash(new Location("mouse", loc.getX(), loc.getY()), 3);

                // fill the list with observations for the selected point
                listReleve = JsonReader.readReleve(obs.getRequete().getAtGeohash(geoHash), true);
                releveScroll.getChildren().clear();
                releveScroll.getChildren().addAll(listReleve);

                // fill the list with species for the selected point
                listeSpecies= JsonReader.readReleve(Requete.getGeohashData(geoHash), false);
                especeScroll.getChildren().clear();
                especeScroll.getChildren().addAll(listeSpecies);

            }
        });

    }
}