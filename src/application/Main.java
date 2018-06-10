package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("scene.fxml"));
			
			Scene scene = new Scene((Parent) loader.load());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Graphical editor");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
