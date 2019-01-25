package ue.evolRestruct.projetAnalyser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{

	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader =  new FXMLLoader(this.getClass().getResource("/mainFrame.fxml"));
		Parent root = loader.load();
		
		this.primaryStage = stage;
		
		this.primaryStage.setScene(new Scene(root));	
		this.primaryStage.show();
	}
	
	
	
	public static void main(String[] args) {
		launch(App.class);
	}

}
