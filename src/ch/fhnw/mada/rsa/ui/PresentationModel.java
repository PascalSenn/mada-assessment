package ch.fhnw.mada.rsa.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import ch.fhnw.mada.rsa.RSAOutOfRangeException;
import ch.fhnw.mada.rsa.TokenPair;
import ch.fhnw.mada.rsa.Translator;
import ch.fhnw.mada.rsa.utils.Tuple;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; 
public class PresentationModel {
	public final static String DEFAULT_FOLDER = "C:\\temp";
	public final static String SECRECT_KEY = "sk.txt";
	public final static String PUBLIC_KEY = "pk.txt";
	public final static String CIPHER_FILE = "cipher.txt";
	public final static String TEXT_FILE = "text.txt";
	public final static String TEXT_D_FILE = "text-d.txt"; 
	private final static DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public final StringProperty workingDirectoryProperty = new SimpleStringProperty(); 
	public final BooleanProperty processingProperty = new SimpleBooleanProperty(); 
	public final DoubleProperty processingProgressProperty = new SimpleDoubleProperty(0); 
	private final ObservableList<String> logList = FXCollections.observableArrayList();
	public final ListProperty<String> logProperty = new SimpleListProperty<String>(logList); 

	public String getSecretKeyPath() {
		return workingDirectoryProperty.get() + "\\" + SECRECT_KEY;
	}

	public String getPublicKeyPath() {
		return workingDirectoryProperty.get() + "\\" + PUBLIC_KEY;
	}
	public String getCipherPath() {
		return workingDirectoryProperty.get() + "\\" + CIPHER_FILE;
	}

	public String getTextPath() {
		return workingDirectoryProperty.get() + "\\" + TEXT_FILE;
	}
	
	public String getTextDPath() {
		return workingDirectoryProperty.get() + "\\" + TEXT_D_FILE;
	}
	
	public void resetDirectory() {
		workingDirectoryProperty.set(DEFAULT_FOLDER);
	}
	
	public void generateKeys() {
 
		log("Key generation started", 0);
		var tokenPair = TokenPair.createToken(2048); 
		log("Token created", 50);
		
        try(
        		var skWriter = new PrintWriter(new BufferedWriter(new FileWriter(getSecretKeyPath())));
        		var pkWriter = new PrintWriter(new BufferedWriter(new FileWriter(getPublicKeyPath())));
        	) {   
        	skWriter.print("(" + tokenPair.privateKey().v1 + "," + tokenPair.privateKey().v2 + ")"); 

    		log("Private key written to " + getSecretKeyPath(),65);
        	pkWriter.print("(" + tokenPair.publicKey().v1 + "," + tokenPair.publicKey().v2 + ")"); 
    		log("Public key written to " + getPublicKeyPath(), 75);
    		skWriter.flush(); 
        	pkWriter.flush(); 
        	
        } catch (IOException ioe) { 
			log("ERROR - Could not write output files ", 0);
            ioe.printStackTrace(); 
        }
		log("Key generation finished", 100); 
	}

	/***
	 * Encrypts cipher.txt and writes text-d.txt
	 */
	public void enrypt() {  

		log("Ecryption started", 0);
		File inputFile = new File(getTextPath());
		try(var outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(getCipherPath())));
			var inputStream = new BufferedReader(new FileReader(inputFile))){
			var translator = getTranslator();
			
			inputStream.lines()
				.flatMap(
						x -> x.chars()
							.mapToObj(
									y -> {
										try {
											return translator.encryptMessage(BigInteger.valueOf(y));
										} catch (RSAOutOfRangeException e) {
									        e.printStackTrace();
									        log("ERROR Output might be corrupted. Input was bigger than public key",0);
										}
										return BigInteger.ZERO;
									}
							)
				).forEach(x -> outputWriter.println(x.toString()));
			outputWriter.flush();
			   
		} catch (IOException e1) { 
			log("ERROR - Could not write output file or read input file", 0);
		}
		log("Ecryption finished", 100);
		
	}
	
	/***
	 * Decrpyts cipher.txt and writes text-d.txt
	 */
	public void decrypt() {  
		log("Decryption started", 0);
		File inputFile = new File(getCipherPath());
		try(var outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(getTextDPath())));
			var inputStream = new BufferedReader(new FileReader(inputFile))){
			var translator = getTranslator();
			
			inputStream.lines()
				.map(x-> {
					try {
						return translator.decryptMessage(new BigInteger(x));
					} catch (RSAOutOfRangeException e) { 
						e.printStackTrace();  
						log("ERROR Output might be corrupted. Input was bigger than public key",0);
						
					}
					return BigInteger.ZERO;
				}).forEach(x -> outputWriter.write((char)x.intValue()));
			   
		} catch (IOException e1) { 
			log("ERROR - Could not write output file or read input file", 0);
			e1.printStackTrace();
		}
		log("Decryption finished", 100);
		
	}
 

	private void log(String message, double progress) { 
		Platform.runLater(() -> {
			if(!processingProperty.get()) {
				processingProperty.set(true);
			}
		
			logList.add( String.format("[%s] %s",LocalDateTime.now().format(LOG_DATE_FORMAT), message));
			processingProgressProperty.setValue(progress); 
		});
		 
	}
	private Tuple<BigInteger> getPublicKey() throws FileNotFoundException {
		return getKeyPair(getPublicKeyPath());
	}
	private Tuple<BigInteger> getPrivateKey() throws FileNotFoundException{
		return getKeyPair(getSecretKeyPath());
	}

	private Tuple<BigInteger> getKeyPair(String path) throws FileNotFoundException {
		File file = new File(path);
		try(Scanner sc = new Scanner(file);) { 
		    sc.useDelimiter(","); 
		    var first = new BigInteger(sc.next().replaceAll("\\(", ""));
		    var second = new BigInteger(sc.next().replaceAll("\\)", ""));
		    return new Tuple<BigInteger>(first, second); 
		}
		catch(FileNotFoundException ex) {
			log("ERROR - File "+path+" was not found", 0);
			throw ex;
		}
		catch(Exception ex) {
			log("ERROR - Error while processing "+path, 0);
			throw ex;
		}
	}
	
	private Translator getTranslator() throws FileNotFoundException {
		return new Translator(
				getPrivateKey(),
				getPublicKey()
		); 
	}

}
