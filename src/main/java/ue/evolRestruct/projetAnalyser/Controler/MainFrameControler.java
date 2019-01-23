package ue.evolRestruct.projetAnalyser.Controler;


import javafx.event.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainFrameControler {

    @FXML // fx:id="ap"
    private AnchorPane ap; // Value injected by FXMLLoader
    
    @FXML // fx:id="projectDirectoryPath"
    private Label projectDirectoryPath; // Value injected by FXMLLoader

    @FXML // fx:id="browserPath"
    private Button browserPath; // Value injected by FXMLLoader

    @FXML // fx:id="q1"
    private Label q1; // Value injected by FXMLLoader

    @FXML // fx:id="q2"
    private Label q2; // Value injected by FXMLLoader

    @FXML // fx:id="q3"
    private Label q3; // Value injected by FXMLLoader

    @FXML // fx:id="q4"
    private Label q4; // Value injected by FXMLLoader

    @FXML // fx:id="q5"
    private Label q5; // Value injected by FXMLLoader

    @FXML // fx:id="q6"
    private Label q6; // Value injected by FXMLLoader

    @FXML // fx:id="q7"
    private Label q7; // Value injected by FXMLLoader

    @FXML // fx:id="q8"
    private Label q8; // Value injected by FXMLLoader

    @FXML // fx:id="q9"
    private Label q9; // Value injected by FXMLLoader

    @FXML // fx:id="q10"
    private Label q10; // Value injected by FXMLLoader

    @FXML // fx:id="q11"
    private Label q11; // Value injected by FXMLLoader

    @FXML // fx:id="q12"
    private Label q12; // Value injected by FXMLLoader

    @FXML // fx:id="q13"
    private Label q13; // Value injected by FXMLLoader

    @FXML // fx:id="buttonBar"
    private ButtonBar buttonBar; // Value injected by FXMLLoader

    @FXML // fx:id="callGraph"
    private Button callGraph; // Value injected by FXMLLoader

    @FXML // fx:id="CouplingGraph"
    private Button CouplingGraph; // Value injected by FXMLLoader

    @FXML // fx:id="Dendrogram"
    private Button Dendrogram; // Value injected by FXMLLoader

    @FXML // fx:id="SpoonCouplingGraph"
    private Button SpoonCouplingGraph; // Value injected by FXMLLoader

    @FXML
    void triggerCallGraph(ActionEvent event) {
    	System.out.println("triggerCallGraph");
    }

    @FXML
    void triggerCoupling(ActionEvent event) {
    	System.out.println("triggerCoupling");
    }

    @FXML
    void triggerDendro(ActionEvent event) {
    	System.out.println("triggerDendro");
    }

    @FXML
    void triggerDirectoryExplorer(ActionEvent event) {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	Stage stage = (Stage) ap.getScene().getWindow();
    	File selectedDirectory = directoryChooser.showDialog(stage);

    	if(selectedDirectory == null){
    	     //No Directory selected
    	}else{
    	     System.out.println(selectedDirectory.getAbsolutePath());
    	     projectDirectoryPath.setText(selectedDirectory.getAbsolutePath());
    	}
    }

    @FXML
    void triggerSpoonCoupling(ActionEvent event) {
    	System.out.println("triggerSpoonCoupling");
    }

}

