package ch.fhnw.mada.rsa.ui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage; 

public class Ui extends VBox {
	private DirectoryChooser folderSelector;
	private Text title;
	private StackPane titleLayout;
	private TextField path;
	private Button selector;
	private GridPane folderChooser;
	private PresentationModel pm;
	private Stage stage;
	public Ui(PresentationModel pm, Stage stage) {
		this.pm = pm;
		this.stage = stage;
		initializeControls();
		layoutControls();
	}
	
	private void initializeControls() {
		folderSelector = new DirectoryChooser();
		title = new Text("RSA Assesment");
		folderChooser = new GridPane();
		selector = new Button("Select Folder");
		selector.setOnAction(x -> pm.path.set(folderSelector.showDialog(this.stage).getAbsolutePath()));
		path = new TextField();
		path.textProperty().bind(pm.path);
		titleLayout= new StackPane();
	}
	
	private void layoutControls() {
		folderChooser.setMaxWidth(USE_PREF_SIZE);
		folderChooser.add(path, 0, 0);
		folderChooser.add(selector, 1, 0); 
		titleLayout.getChildren().add(title);
		getChildren().addAll(titleLayout, folderChooser);
		
		
	}
	
	
}
