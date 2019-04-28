package ch.fhnw.mada.rsa.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import ch.fhnw.mada.rsa.RSAOutOfRangeException;
import ch.fhnw.mada.rsa.TokenPair;
import ch.fhnw.mada.rsa.Translator;
import ch.fhnw.mada.rsa.utils.Tuple;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter; 

public class Ui extends VBox { 
	private static final String INTRO_TEMPLATE = "This application uses the folder %s. Make sure this folder exists. All files are stored in this folder.";
	private static final String GENERATE_DESCRIPTION_TEMPLATE = "Creates public and private key as %s and %s";
	private static final String ENRYPT_FILE_TEMPLATE = "Entrypts the file %s with public key  %s and outputs into %s";
	private static final String DECRYPT_FILE_TEMPLATE = "Detrypts the file %s with private key  %s and outputs into %s";
	private static final int PADDING_SIDES = 25;
	private Text title;
	private Text intro;
	private StackPane titleLayout; 
	private PresentationModel pm;
	private Stage stage;
	
	private Text generateDescription;
	private Button generateButton;
	private GridPane generateLayout;
	private Text generateLabel;

	private Text encryptFileDescription;
	private Button encryptFileButton;
	private GridPane encryptFileLayout;
	private Text encryptFileLabel;
	

	private Text decryptFileDescription;
	private Button decryptFileButton;
	private GridPane decryptFileLayout;
	private Text decryptFileLabel;
	
	private DirectoryChooser mainDirectoryChooser; 
	private TextField mainDirectoryTextField; 
	private Button mainDirectoryButton;
	private HBox mainDirectoryLayout; 
	private Text mainDirectoryLabel;
	
	private Text progressLabel;
	private ProgressBar progressBar;
	private ListView<String> logView;
	
	public Ui(PresentationModel pm, Stage stage) {
		this.pm = pm;
		this.stage = stage;
		initializeControls();
		layoutControls();
	}
	private void initializeControls() {
		titleLayout= new StackPane();
		intro = new  Text("");
		title = new Text("RSA Assement");
		widthProperty().addListener((o, old, newValue) -> intro.setWrappingWidth(newValue.doubleValue() - PADDING_SIDES*2));
		pm.workingDirectoryProperty.addListener((o, old, newValue) -> intro.setText(String.format(INTRO_TEMPLATE, newValue)));
		
		generateDescription = new Text(); 
		widthProperty().addListener((o, old, newValue) -> generateDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100 ));
		generateButton = new Button("Generate");
		generateLayout = new GridPane();
		generateLabel = new Text("Generate Keys");
		generateButton.setOnAction(x ->  pm.generateKeys());
		pm.workingDirectoryProperty.addListener((o, old, newValue) -> generateDescription.setText(String.format(GENERATE_DESCRIPTION_TEMPLATE, pm.getPublicKeyPath(),  pm.getSecretKeyPath())));
		
		encryptFileDescription = new Text(); 
		widthProperty().addListener((o, old, newValue) -> encryptFileDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100 ));
		encryptFileButton = new Button("Encrypt");
		encryptFileLayout = new GridPane();
		encryptFileLabel = new Text("Enrypt File");
		encryptFileButton.setOnAction(x -> pm.enrypt());
		pm.workingDirectoryProperty.addListener((o, old, newValue) -> encryptFileDescription.setText(String.format(ENRYPT_FILE_TEMPLATE, pm.getTextPath(),  pm.getPublicKeyPath(),  pm.getCipherPath())));
		
		
		decryptFileDescription = new Text(); 
		widthProperty().addListener((o, old, newValue) -> decryptFileDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100 ));
		decryptFileButton = new Button("Decrypt");
		decryptFileLayout = new GridPane();
		decryptFileLabel = new Text("Decrypt File");
		decryptFileButton.setOnAction(x -> pm.decrypt());
		pm.workingDirectoryProperty.addListener((o, old, newValue) -> decryptFileDescription.setText(String.format(DECRYPT_FILE_TEMPLATE, pm.getCipherPath(), pm.getSecretKeyPath(), pm.getTextDPath())));
		
		
		mainDirectoryChooser = new DirectoryChooser();
		mainDirectoryLayout = new HBox();
		mainDirectoryButton = new Button("Select");
		mainDirectoryTextField = new TextField();
		mainDirectoryLabel = new Text("Directory");
		mainDirectoryButton.setOnAction(x -> { 
        		var directory = mainDirectoryChooser.showDialog(stage);
        		if(directory != null) {
        			pm.workingDirectoryProperty.set(directory.getPath()); 
        		}
		});
		mainDirectoryTextField.textProperty().bind(pm.workingDirectoryProperty);
	
		progressLabel = new Text("Progress");
		progressBar = new ProgressBar(0); 
		progressBar.progressProperty().bind(pm.processingProgressProperty);
		
		logView = new ListView<>();
		logView.itemsProperty().bind(pm.logProperty);
		
		pm.processingProperty.addListener((o, old, newValue) -> {
			progressBar.setDisable(newValue); 
		});
		
		pm.resetDirectory();
	}
	
	private void layoutControls() {
		
		setPadding(new Insets(5, PADDING_SIDES, 5, PADDING_SIDES));
		
		title.setFont(Font.font ("Segoe UI", 20));
		titleLayout.getChildren().add(title); 
		

		mainDirectoryLayout.getChildren().addAll(mainDirectoryTextField, mainDirectoryButton);
		HBox.setHgrow(mainDirectoryTextField, Priority.ALWAYS);
		layoutLabel(mainDirectoryLabel);
		
		setMargin(progressBar, new Insets(10,0,10,0)); 
		progressBar.setMinHeight(20);
		progressBar.prefWidthProperty().bind(widthProperty().subtract(2*PADDING_SIDES));
		progressBar.setMaxWidth(USE_PREF_SIZE);
		progressBar.setMinWidth(USE_PREF_SIZE);
		layoutLabel(progressLabel);
		
		layoutButton(generateLayout, generateLabel, generateButton, generateDescription);
		layoutButton(encryptFileLayout , encryptFileLabel, encryptFileButton, encryptFileDescription);
		layoutButton(decryptFileLayout , decryptFileLabel, decryptFileButton, decryptFileDescription);
		
		getChildren().addAll(titleLayout, mainDirectoryLabel, mainDirectoryLayout, intro, generateLayout, encryptFileLayout, decryptFileLayout, progressLabel, progressBar, logView);
	}
	
	private void layoutButton(GridPane layout, Text label, Button button, Text buttonDescirption) {

		layoutLabel(label);
		layout.add(label, 0, 0);
		layout.add(button, 0, 1);
		layout.add(buttonDescirption, 1, 1); 
		layout.getColumnConstraints().add(new ColumnConstraints(100));
		setMargin(layout, new Insets(10,0,0,0));  
		
	}
	
	private Text layoutLabel(Text label) { 
		label.setFont(Font.font ("Segoe UI", 15));
		return label;
	}
	
	
}
