package ch.fhnw.mada.rsa.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.stream.Stream;

import ch.fhnw.mada.rsa.TokenPair;
import ch.fhnw.mada.rsa.Translator;
import ch.fhnw.mada.rsa.utils.Tuple;
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
	private Integer rsaN;
	private Integer rsaD;
	private Integer rsaE;
	
	private TokenPair tokenPair;
	private Text title;
	private StackPane titleLayout;
	private TextField path;
	private Button selector;
	private GridPane folderChooser;
	private PresentationModel pm;
	private Stage stage;
    private FileChooser fileChooser;
    private GridPane generate;
    private String message;
    private Scanner msg;

	private GridPane publicKeyPane;
	private GridPane privateKeyPane;
    private FileChooser publicKeyReadIn;
    private FileChooser privateKeyReadIn;
	private Button selectorPublicKey;
	private Button selectorPrivateKey;
	private Button generateKeyPair;
    private Scanner sPub;
    private Scanner sPri;
    
    private Button encrypt;
    private Button decrypt;
    
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
		selector = new Button("Select File");
		selector.setOnAction(x -> {
			try {
        		//File erstellen
        		File f = new File("");
        		f = fileChooser.showOpenDialog(stage);
        		//File lesen
        		msg = new Scanner(f);
        		//message lesen
    			message = new String(sPub.next());
    			
    			//Pfad binden
    			pm.path.set(f.getPath());       			
            	
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		});
		
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
		
		pathPri = new TextField();
		pathPri.textProperty().bind(pm.pathPri);
		pathPub = new TextField();
		pathPub.textProperty().bind(pm.pathPub);
		
		
		//Title
		title = new Text("RSA Assesment"); 
		titleLayout= new StackPane();
		
		//Generiert Public und Private Key und speichert diese in sk.txt(private key) und pk.txt(public key) mit , getrennt
		generate = new GridPane();
		generateKeyPair = new Button("Generate Key Pair");
		generateKeyPair.setOnAction(x -> {
			tokenPair = TokenPair.createToken(2048);
			
			PrintWriter pkWriter = null; 
			PrintWriter skWriter = null; 
	        try { 
	        	pm.rsaN.set(tokenPair.privateKey().v1.toString());
	        	pm.rsaD.set(tokenPair.privateKey().v2.toString());
	        	pm.rsaE.set(tokenPair.publicKey().v2.toString());
	        	skWriter = new PrintWriter(new BufferedWriter(new FileWriter("sk.txt"))); 
	        	skWriter.println("(" + tokenPair.privateKey().v1 + "," + tokenPair.privateKey().v2 + ")");
	        	pkWriter = new PrintWriter(new BufferedWriter(new FileWriter("pk.txt"))); 
	        	pkWriter.println("(" + tokenPair.publicKey().v1 + "," + tokenPair.publicKey().v2 + ")"); 
	        } catch (IOException ioe) { 
	            ioe.printStackTrace(); 
	        } finally { 
	            if (skWriter != null){ 
	            	skWriter.flush(); 
	            	skWriter.close(); 
	            } 
	            if (pkWriter != null){ 
	            	pkWriter.flush(); 
	            	pkWriter.close(); 
	            } 
	        } 
			
		});
		
		encrypt = new Button("Encrypt");
		encrypt.setOnAction(x -> {
			
			var translator = new Translator( 
					new Tuple<BigInteger>(pm.rsaN, pm.rsaD),
					new Tuple<BigInteger>(pm.rsaN, pm.rsaE)
			);
			
			   PrintWriter encryptMsg = new PrintWriter(new BufferedWriter(new FileWriter("encryptedMsg.txt"))); 
			   for (int i = 0; i < message.length(); i++){
				    char c = message.charAt(i);     
				    int ascii = (int) c;
				    encryptMsg.print(translator.encryptMessage(BigInteger.valueOf(ascii)));
				}
			
			
		});
		
		encrypt = new Button("Decrypt");
		encrypt.setOnAction(x -> {

			var translator = new Translator( 
					new Tuple<BigInteger>(pm.rsaN, pm.rsaD),
					new Tuple<BigInteger>(pm.rsaN, pm.rsaE)
			);
			
			   PrintWriter decryptMsg = new PrintWriter(new BufferedWriter(new FileWriter("decryptedMsg.txt"))); 
			   for (int i = 0; i < message.length(); i++){
				    char c = message.charAt(i);     
				    int ascii = (int) c;
				    decryptMsg.print(translator.decryptMessage(BigInteger.valueOf(ascii)));
				}
			
		});
		
		
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
