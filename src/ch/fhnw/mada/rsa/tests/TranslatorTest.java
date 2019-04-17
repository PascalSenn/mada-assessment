package ch.fhnw.mada.rsa.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;
 

import ch.fhnw.mada.rsa.RSAOutOfRangeException;
import ch.fhnw.mada.rsa.TokenPair;
import ch.fhnw.mada.rsa.Translator;
import ch.fhnw.mada.rsa.utils.Tuple;

public class TranslatorTest {

	@Test
	public void TranslatorTest1() throws RSAOutOfRangeException{
		TestTranslator(391, 59, 179, 23); 
	}
	
	@Test
	public void TranslatorTest2() throws RSAOutOfRangeException{
		TestTranslator(55, 3, 7, 23); 
	}
	
	public void TestTranslator(int n, int e, int d, int message) throws RSAOutOfRangeException { 
		var translator = new Translator( 
				new Tuple<BigInteger>(BigInteger.valueOf(n), BigInteger.valueOf(d)),
				new Tuple<BigInteger>(BigInteger.valueOf(n), BigInteger.valueOf(e))
		);
		var encrypted = translator.encryptMessage(BigInteger.valueOf(message));
		var decrypted  = translator.decryptMessage(encrypted);
		assertFalse(encrypted.equals(decrypted));
		assertTrue(BigInteger.valueOf(message).equals(decrypted));
		
		
	}
}
