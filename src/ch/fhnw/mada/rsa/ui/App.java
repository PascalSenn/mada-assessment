package ch.fhnw.mada.rsa.ui;
 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		var pm = new PresentationModel();
		var ui = new Ui(pm, stage);
		var scene = new Scene(ui);
		stage.setScene(scene);
		stage.setTitle("RSA");
		stage.setHeight(600);
		stage.setWidth(600);
		stage.show();
		
	}
}
