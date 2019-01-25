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
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.Analyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.GraphAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.DendroAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.DendroAnalyzer.NodeCluster;
import ue.evolRestruct.projetAnalyser.spoon.spoonCode.SpoonAnalyze;
import ue.evolRestruct.projetAnalyser.Model.Stats.StatisticsAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

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
    private Button callGraphButton; // Value injected by FXMLLoader

    @FXML // fx:id="CouplingGraph"
    private Button couplingGraphButton; // Value injected by FXMLLoader

    @FXML // fx:id="Dendrogram"
    private Button dendrogramButton; // Value injected by FXMLLoader

    @FXML // fx:id="SpoonCouplingGraph"
    private Button spoonCouplingGraphButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="spoonDendro"
    private Button spoonDendroButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="partitions"
    private Button partitions; // Value injected by FXMLLoader

	private String pathFolder = null;
	private MultiGraph spoonGraph = null;
	private MultiGraph callGraph = null;
	private MultiGraph couplingGraph = null;
	private ArrayList<JFrame> arrayDendro = null;
	private ArrayList<NodeCluster> arrayPartition = null;
	private ArrayList<JFrame> arraySpoonDendro = null;

    @FXML
    void triggerCallGraph(ActionEvent event) {
    	//System.out.println("triggerCallGraph");
    	if(this.callGraph != null) {
    		Viewer viewer = this.callGraph.display();
    	    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    	}
    	else
    	{
    		System.out.println("Veuillez choisir un dossier ou attendre que le projet est été analyser pour voir ce graphe");
    	}
    }

    @FXML
    void triggerCoupling(ActionEvent event) {
    	//System.out.println("triggerCoupling");
    	if(this.couplingGraph != null) {
    		Viewer viewer = this.couplingGraph.display();
    	    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    	}
    	else
    	{
    		System.out.println("Veuillez choisir un dossier ou attendre que le projet est été analyser pour voir ce graphe");
    	}
    }

    @FXML
    void triggerDendro(ActionEvent event) {
    	//System.out.println("triggerDendro");
    	if(this.arrayDendro != null) {
    		for(JFrame windows : this.arrayDendro) {
    			windows.setVisible(true);
    		}
    	}
    	else {
    		System.out.println("Veuillez attendre la fin du chargement pour utiliser cette fonctionnalité");
    	}
    	
    }

    @SuppressWarnings("restriction")
	@FXML
    void triggerDirectoryExplorer(ActionEvent event) {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	Stage stage = (Stage) ap.getScene().getWindow();
    	File selectedDirectory = directoryChooser.showDialog(stage);

    	if(selectedDirectory == null){
    	     System.err.println("Erreur lors du chargement du dossier");
    	}else{
    		try {
    			System.out.println(selectedDirectory.getAbsolutePath());
				String path = selectedDirectory.getAbsolutePath();
				this.pathFolder = path;
				projectDirectoryPath.setText(this.pathFolder);
				
				// Spoon
				SpoonAnalyze.checkAllProject(this.pathFolder);   			
				this.spoonGraph = GraphAnalyzer.buildPonderalGraph(SpoonAnalyze.pairArray);
				
				// AST
				Analyzer analyzer = new Analyzer("./");
				PackageAnalyzer project = analyzer.parseCallVisitors();	
				
				q1.setText(String.valueOf(StatisticsAnalyzer.numberOfClasses(project)));
				q2.setText(String.valueOf(StatisticsAnalyzer.numberOfLinesOfCode(project)));
				q3.setText(String.valueOf(StatisticsAnalyzer.numberOfMethods(project)));
				q4.setText(String.valueOf(StatisticsAnalyzer.numberOfPackages(project)));
				q5.setText(String.valueOf(StatisticsAnalyzer.averageNumberOfMethodsByClass(project)));
				q6.setText(String.valueOf(StatisticsAnalyzer.averageNumberOfLinesOfCodeByMethod(project)));
				q7.setText(String.valueOf(StatisticsAnalyzer.averageNumberOfFieldsByClass(project)));
				q8.setText(String.valueOf(StatisticsAnalyzer.biggestClassesByNumberOfMethods(project, 10)));
				q9.setText(String.valueOf(StatisticsAnalyzer.biggestClassesByNumberOfFields(project, 10)));
				q10.setText(String.valueOf(StatisticsAnalyzer.biggestClassesByNumberOfMethodsAndByNumberOfFields(project, 10)));
				q11.setText(String.valueOf(StatisticsAnalyzer.classesWithMoreThenXMethods(project, 10)));
				q12.setText(String.valueOf(StatisticsAnalyzer.biggestMethodesByNumberOfLinesOfCode(project, 10)));
				q13.setText(String.valueOf(StatisticsAnalyzer.maximumNumberOfParameters(project)));
				
				this.callGraph = GraphAnalyzer.buildGeneralGraph(project);
				this.couplingGraph = GraphAnalyzer.buildPonderalGraph(project);
				DendroAnalyzer da = GraphAnalyzer.buildDendogramme(project);
				this.arrayDendro = da.buildDendro();
				this.arraySpoonDendro = GraphAnalyzer.buildDendogramme(SpoonAnalyze.pairArray).buildDendro();
				this.arrayPartition = da.getClusters();
    				
    				
    				
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	     
    	}
    }

    @FXML
    void triggerSpoonCoupling(ActionEvent event) {
    	//System.out.println("triggerSpoonCoupling");
    	if(this.spoonGraph != null) {
    		Viewer viewer = this.spoonGraph.display();
    	    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    	}
    	else
    	{
    		System.out.println("Veuillez choisir un dossier ou attendre que le projet est été analyser pour voir ce graphe");
    	}
    }
    
    @FXML
    void triggerSpoonDendro(ActionEvent event) {
    	//System.out.println("triggerSpoonDendro");
    	if(this.arraySpoonDendro != null) {
    		for(JFrame windows : this.arraySpoonDendro) {
    			windows.setVisible(true);
    		}
    	}
    	else {
    		System.out.println("Veuillez attendre la fin du chargement pour utiliser cette fonctionnalité");
    	}
    }
    
    @FXML
    void triggerParittions(ActionEvent event) {
    	//System.out.println("triggerParittions");
    	if(this.arrayPartition != null) {
    		for(NodeCluster nc : this.arrayPartition) {
    			DendroAnalyzer.openDendro(nc).setVisible(true);
    		}
    	}
    	else {
    		System.out.println("Veuillez attendre la fin du chargement pour utiliser cette fonctionnalité");
    	}
    }
    
}

