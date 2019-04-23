package ch.fhnw.mada.rsa.fea;

import java.math.BigInteger;

public class FastExponentiationAlgorithm { 
	public static FastExponentiationAlgorithm calculate(BigInteger base, BigInteger exponent, BigInteger mod) {
		return  new FastExponentiationAlgorithm(base ,exponent ,mod );
	}
 
	private BigInteger h = BigInteger.ONE;
	private BigInteger k;
	
	private FastExponentiationAlgorithm(BigInteger base, BigInteger exponent, BigInteger mod) { 
		k = base;
		for(var i = 0; i <= exponent.bitLength(); i++) { 
			if(exponent.testBit(i)) {
				h = h.multiply(k).mod(mod);
			}
			k= k.multiply(k).mod(mod);
			
		}
		
	}
	
	public BigInteger getResult() {
		return h;
	}
}
