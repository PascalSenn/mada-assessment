package ch.fhnw.mada.rsa.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import ch.fhnw.mada.rsa.eea.ExtendedEuclideanAlgorithm;
 

public class ExtendedEuclideanAlgorithmTests { 

	 

	@Test
	public void ExtendedEuclideanAlgTest1(){
		// Arrange
		var arg0 = BigInteger.valueOf(17);
		var arg1 = BigInteger.valueOf(5);
		var expectedResult = BigInteger.ONE;
		
		// Act

		var result = ExtendedEuclideanAlgorithm.calculate(arg0, arg1);
		
		
		// Assert 
		assertTrue(result.getGCD().compareTo(expectedResult) == 0);
	}

	   @Test
		public void ExtendedEuclideanAlgTest2(){
			// Arrange
		   	var arg0 = BigInteger.valueOf(13);
		   	var arg1 = BigInteger.valueOf(13);
		   	var expectedResult =  BigInteger.valueOf(13);
		   	// Act

			var result = ExtendedEuclideanAlgorithm.calculate(arg0, arg1);
			
			
			// Assert 
			assertTrue(result.getGCD().compareTo(expectedResult) == 0);
		}

	    @Test
		public void ExtendedEuclideanAlgTest3(){
			// Arrange
	    	var arg0 = BigInteger.valueOf(37);
	    	var arg1 = BigInteger.valueOf(600);
	    	var expectedResult =  BigInteger.valueOf(1   );
	    	// Act

			var result = ExtendedEuclideanAlgorithm.calculate(arg0, arg1);
			
			
			// Assert 
			assertTrue(result.getGCD().compareTo(expectedResult) == 0);
		}

	    @Test
		public void ExtendedEuclideanAlgTest4(){
			// Arrange
	    	var arg0 = BigInteger.valueOf(20);
	    	var arg1 = BigInteger.valueOf(100);
	    	var expectedResult =  BigInteger.valueOf(20);         

	    	// Act

			var result = ExtendedEuclideanAlgorithm.calculate(arg0, arg1);
			
			
			// Assert 
			assertTrue(result.getGCD().compareTo(expectedResult) == 0);
		}

	    @Test
		public void ExtendedEuclideanAlgTest5(){
			// Arrange
	    	var arg0 = BigInteger.valueOf(624129);
	    	var arg1 = BigInteger.valueOf(2061517);
	    	var expectedResult =  BigInteger.valueOf(18913);
	    	// Act

			var result = ExtendedEuclideanAlgorithm.calculate(arg0, arg1);
			
			
			// Assert 
			assertTrue(result.getGCD().compareTo(expectedResult) == 0);
		}
	    
}
