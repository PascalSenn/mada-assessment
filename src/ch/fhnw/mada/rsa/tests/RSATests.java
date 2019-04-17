package ch.fhnw.mada.rsa.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import ch.fhnw.mada.rsa.RSAOutOfRangeException;
import ch.fhnw.mada.rsa.TokenPair;
import ch.fhnw.mada.rsa.Translator;

public class RSATests {

	@Test
	public void RSASmallButManyTest() throws RSAOutOfRangeException{
		var random = new Random();
		
		for(var i = 0; i < 1000; i++) {
			 
			var tokenPair = TokenPair.createToken(50);
			var message = BigInteger.valueOf(random.nextInt(10)+1);
			var translator = new Translator(tokenPair.privateKey(), tokenPair.publicKey());
			var encrypted = translator.encryptMessage(message);
			var decrypted  = translator.decryptMessage(encrypted);
			assertTrue(message.equals(decrypted));
		}
	}

	@Test
	public void RSABigNumberTests() throws RSAOutOfRangeException{
		var random = new Random();
		
		for(var i = 0; i < 10; i++) {
			 
			var tokenPair = TokenPair.createToken(2048);
			var message = BigInteger.valueOf(random.nextInt(10000)+1);
			var translator = new Translator(tokenPair.privateKey(), tokenPair.publicKey());
			var encrypted = translator.encryptMessage(message);
			var decrypted  = translator.decryptMessage(encrypted);
			assertTrue(message.equals(decrypted));
		}
	}

	
}
