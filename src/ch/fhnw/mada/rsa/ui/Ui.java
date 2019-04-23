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

import ch.fhnw.mada.rsa.RSAOutOfRangeException;
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
    private Scanner msg;

	private GridPane publicKeyPane;
	private GridPane privateKeyPane;
	private GridPane enanddecryption;
    private FileChooser publicKeyReadIn;
    private FileChooser privateKeyReadIn;
	private Button selectorPublicKey;
	private Button selectorPrivateKey;
	private Button generateKeyPair;
    private Scanner sPub;
    private Scanner sPri;
    

	private GridPane folderChooser2;
    private FileChooser fileChooser2;
	private Button selector2;
    private Scanner msg2;
	private TextField path2;

    
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
		selector = new Button("Select File to Encrypt");
		selector.setOnAction(x -> {
			try {
        		//File erstellen
        		File f = new File("");
        		f = fileChooser.showOpenDialog(stage);
        		
        		//File lesen
        		msg = new Scanner(f);
    			//Pfad binden*/
    			pm.path.set(f.getPath());       			
            	
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		});
		
		//Show Textfile Path
		path = new TextField();
		path.textProperty().bind(pm.path);
		

		//Choose the TextFile
		fileChooser2 = new FileChooser();
		folderChooser2 = new GridPane();
		selector2 = new Button("Select File to Decrypt");
		selector2.setOnAction(x -> {
			try {
        		//File erstellen
        		File f = new File("");
        		f = fileChooser.showOpenDialog(stage);
        		
        		//File lesen
        		msg2 = new Scanner(f);
    			//Pfad binden*/
    			pm.path2.set(f.getPath());       			
            	
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		});
		
		//Show Textfile Path
		path2 = new TextField();
		path2.textProperty().bind(pm.path2);
		

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
    			System.out.println(pm.rsaN.get());
    			System.out.println(pm.rsaE.get());
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
	        			System.out.println(pm.rsaN.get());
	        			System.out.println(pm.rsaD.get());
	                	
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
    			System.out.println("N:" + pm.rsaN.get());
    			System.out.println("D:" + pm.rsaD.get());
    			System.out.println("E:" + pm.rsaE.get());
	        	
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
		
		enanddecryption = new GridPane();
		encrypt = new Button("Encrypt");
		encrypt.setOnAction(x -> {
			String str= pm.rsaN.get();
			BigInteger n = new BigInteger(str);
			str= pm.rsaD.get();
			BigInteger d = new BigInteger(str);
			str = pm.rsaE.get();
			BigInteger e = new BigInteger(str);

			var translator = new Translator( 
					new Tuple<BigInteger>(n, d),
					new Tuple<BigInteger>(n, e)
			);
			
			   PrintWriter encryptMsg;
			try {
				encryptMsg = new PrintWriter(new BufferedWriter(new FileWriter("encryptedMsg.txt")));
				while(msg.hasNextLine()) { 
						String message = new String(msg.nextLine());
						for (int i = 0; i < message.length(); i++){
							char c = message.charAt(i);
							int ascii = (int) c;
						    encryptMsg.println(translator.encryptMessage(BigInteger.valueOf(ascii)));
						}
						encryptMsg.println("");
						
				   }
			    encryptMsg.flush();  
			    encryptMsg.close();  
				   
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RSAOutOfRangeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			
		});
		
		decrypt = new Button("Decrypt");
		decrypt.setOnAction(x -> {
			String str= pm.rsaN.get();
			BigInteger n = new BigInteger(str);
			str= pm.rsaD.get();
			BigInteger d = new BigInteger(str);
			str = pm.rsaE.get();
			BigInteger e = new BigInteger(str);

			var translator = new Translator( 
					new Tuple<BigInteger>(n, d),
					new Tuple<BigInteger>(n, e)
			);
			
			   PrintWriter decryptMsg;
			try {
				decryptMsg = new PrintWriter(new BufferedWriter(new FileWriter("decryptedMsg.txt")));
				   while(msg2.hasNextLine()) 
				   {          
					   String line = msg2.nextLine();
						if(line.length() == 0) {
							System.out.println("");
						    decryptMsg.println("");
						}else {
							BigInteger encryptAscii = new BigInteger(line);
							var decryptedAscii  = translator.decryptMessage(encryptAscii);
							int ascii = decryptedAscii.intValue();
							char c = (char) ascii;
							

						    decryptMsg.print(c);
							System.out.print(c);
						}
					
				   }
				    decryptMsg.flush();  
				    decryptMsg.close(); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RSAOutOfRangeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		});
	}
	
	private void layoutControls() {
		folderChooser.setMaxWidth(USE_PREF_SIZE);
		folderChooser.add(path, 0, 0);
		folderChooser.add(selector, 1, 0);
		
		folderChooser2.setMaxWidth(USE_PREF_SIZE);
		folderChooser2.add(path2, 0, 0);
		folderChooser2.add(selector2, 1, 0);
		

		publicKeyPane.setMaxWidth(USE_PREF_SIZE);
		publicKeyPane.add(pathPub, 0, 0);
		publicKeyPane.add(selectorPublicKey, 1, 0);
		
		privateKeyPane.setMaxWidth(USE_PREF_SIZE);
		privateKeyPane.add(pathPri, 0, 0);
		privateKeyPane.add(selectorPrivateKey, 1, 0);
		
		generate.setMaxWidth(USE_PREF_SIZE);
		generate.add(generateKeyPair, 0, 0);
		enanddecryption.setMaxWidth(USE_PREF_SIZE);
		enanddecryption.add(encrypt, 0, 0);
		enanddecryption.add(decrypt, 1, 0);
		
		titleLayout.getChildren().add(title);
		getChildren().addAll(titleLayout, folderChooser, folderChooser2, publicKeyPane, privateKeyPane, generate, enanddecryption);
		
		
	}
	
	
}
