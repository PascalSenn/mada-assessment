package ch.fhnw.mada.rsa.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import ch.fhnw.mada.rsa.fea.FastExponentiationAlgorithm;
import ch.fhnw.madam.rsa.eea.ExtendedEuclideanAlgorithm;

public class FastExponentiationAlgorithmTests {
	
	@Test
	public void FastExponentiationAlgorithm1(){
		var random = new Random();
		
		for(var i = 0; i < 1000; i++) {
			System.out.println("Fast Exp. Alg Test: " + i);
			var base = getPositiveInt(random) ;
			var exponent = getPositiveInt(random) ;
			var mod =getPositiveInt(random) ;
			TestFastExponentiationAlgorithm(exponent, base, mod);
		}
	}

	private int getPositiveInt(Random rand) {
		return getPositiveInt(rand, 100);
		
	}
	private int getPositiveInt(Random rand, int bounds) {
		var num = rand.nextInt(bounds);
		if(num < 1 ) {
			return getPositiveInt(rand, bounds);
		}
		return num;
	}
	
	private void TestFastExponentiationAlgorithm(int exponentInt, int baseInt, int modInt) {

		// Arrange 
		var base = BigInteger.valueOf(baseInt);
		var mod = BigInteger.valueOf(modInt);
		var exponent = BigInteger.valueOf(exponentInt);
		var expextedResult = base.pow(exponentInt).mod(mod);
		
		// Act

		var result = FastExponentiationAlgorithm.calculate(base, exponent, mod);
		
		 
		// Assert  
	
		assertTrue(result.getResult().compareTo(expextedResult) == 0);
	}

}
