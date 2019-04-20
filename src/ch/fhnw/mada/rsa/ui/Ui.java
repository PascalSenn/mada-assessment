package ch.fhnw.mada.rsa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage; 

public class Ui extends VBox {
	private Text title;
	private StackPane titleLayout;
	private TextField path;
	private Button selector;
	private GridPane folderChooser;
	private PresentationModel pm;
	private Stage stage;
    private FileChooser fileChooser;
    private GridPane generate;

	private GridPane publicKeyPane;
	private GridPane privateKeyPane;
    private FileChooser publicKeyReadIn;
    private FileChooser privateKeyReadIn;
	private Button selectorPublicKey;
	private Button selectorPrivateKey;
	private Button generateKeyPair;
    private Scanner sPub;
    private Scanner sPri;
    /*private HBox showN;
    private HBox showD;
    private HBox showE;
    private Label n;
    private Label d;
    private Label e;
    private Label LabelN;
    private Label LabelE;
    private Label LabelD;
    private VBox showrsaKeys;*/
    
	private TextField pathPri;
	private TextField pathPub;
	public Ui(PresentationModel pm, Stage stage) {
		this.pm = pm;
		this.stage = stage;
		initializeControls();
		layoutControls();
	}
	
	private void initializeControls() {
		//Choose the TextFile
		fileChooser = new FileChooser();
		folderChooser = new GridPane();
		selector = new Button("Select Folder");
		selector.setOnAction(x -> pm.path.set(fileChooser.showOpenDialog(stage).getPath()));
		
		//Show Textfile Path
		path = new TextField();
		path.textProperty().bind(pm.path);
		

		//Liest den Public Key aus einer Datei und füllt N und E ab
		publicKeyReadIn = new FileChooser();
		publicKeyPane = new GridPane();
		selectorPublicKey = new Button("Select Public Key");
		selectorPublicKey.setOnAction(x -> {
        	try {
        		//File erstellen
        		File f = new File("");
        		f = publicKeyReadIn.showOpenDialog(stage);
        		//File lesen
        		sPub = new Scanner(f);
        		//Key lesen
    			String key = new String(sPub.next());
    			String[] keyparts = key.substring(1, key.length()-1).split(",");
    			
    			//N, E und en PrivateKey Pfad binden
    			pm.rsaN.set(keyparts[0]);
    			pm.rsaE.set(keyparts[1]);
    			pm.pathPub.set(f.getPath());       			
            	
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		});
		
		//Liest den Private Key aus einer Datei und füllt N und D ab
		privateKeyReadIn = new FileChooser();
		privateKeyPane = new GridPane();
		selectorPrivateKey = new Button("Select Private Key");
		selectorPrivateKey.setOnAction(x -> {
	            	try {
	            		//File erstellen
	            		File f = new File("");
	            		f = privateKeyReadIn.showOpenDialog(stage);
	            		//File lesen
	            		sPri = new Scanner(f);
	            		//Key lesen
	        			String key = new String(sPri.next());
	        			String[] keyparts = key.substring(1, key.length()-1).split(",");
	        			
	        			//N, D und en PrivateKey Pfad binden
	        			pm.rsaN.set(keyparts[0]);
	        			pm.rsaD.set(keyparts[1]);
	        			pm.pathPri.set(f.getPath());
	                	
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
		});
		
		//Show Keys
		/*LabelN = new Label();
		LabelE = new Label();
		LabelD = new Label();
		LabelN.textProperty().bind(pm.rsaN);
		LabelE.textProperty().bind(pm.rsaE);
		LabelD.textProperty().bind(pm.rsaD);*/
		pathPri = new TextField();
		pathPri.textProperty().bind(pm.pathPri);
		pathPub = new TextField();
		pathPub.textProperty().bind(pm.pathPub);
		
		
		//Title
		title = new Text("RSA Assesment");
		titleLayout= new StackPane();
		
		generate = new GridPane();
		generateKeyPair = new Button("Generate Key Pair");
		//Action to Generate a Pair selector.setOnAction();
		
		/*showN = new HBox();
		showE = new HBox();
		showD = new HBox();
		showrsaKeys = new VBox();
		
		 n = new Label("N:");
		 e = new Label("E:");
		 d = new Label("D:");*/
		
		
		
		
		
	}
	
	private void layoutControls() {
		folderChooser.setMaxWidth(USE_PREF_SIZE);
		folderChooser.add(path, 0, 0);
		folderChooser.add(selector, 1, 0);
		

		publicKeyPane.setMaxWidth(USE_PREF_SIZE);
		publicKeyPane.add(pathPub, 0, 0);
		publicKeyPane.add(selectorPublicKey, 1, 0);
		
		privateKeyPane.setMaxWidth(USE_PREF_SIZE);
		privateKeyPane.add(pathPri, 0, 0);
		privateKeyPane.add(selectorPrivateKey, 1, 0);
		
		generate.setMaxWidth(USE_PREF_SIZE);
		generate.add(generateKeyPair, 0, 0);
		
		/*showN.getChildren().addAll(n, LabelN);
		showD.getChildren().addAll(d, LabelD);
		showE.getChildren().addAll(e, LabelE);*/
		
		/*showrsaKeys.getChildren().addAll(showN, showD, showE);*/
		
		titleLayout.getChildren().add(title);
		getChildren().addAll(titleLayout, folderChooser, publicKeyPane, privateKeyPane, generate);
		
		
	}
	
	
}
