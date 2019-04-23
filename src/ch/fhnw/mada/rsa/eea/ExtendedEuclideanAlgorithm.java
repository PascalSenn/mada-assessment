package ch.fhnw.mada.rsa.eea;

import java.math.BigInteger;

public class ExtendedEuclideanAlgorithm {
	private BigInteger aRoot;

	private BigInteger bRoot;
	private BigInteger a;
	private BigInteger b;
	private BigInteger y0;
	private BigInteger y1;
	private BigInteger x0;
	private BigInteger x1;
	private BigInteger q;
	private BigInteger r;
	
	public static ExtendedEuclideanAlgorithm calculate(BigInteger a, BigInteger b) {
		return new ExtendedEuclideanAlgorithm(a, b);
	}
	private ExtendedEuclideanAlgorithm(BigInteger _a, BigInteger _b) {
		aRoot = _a;
		bRoot = _b;
		this.a = _a;
		this.b = _b;
		this.x0 =  BigInteger.valueOf(1);
		this.y0 =  BigInteger.valueOf(0);
		this.x1 =  BigInteger.valueOf(0);
		this.y1 =  BigInteger.valueOf(1); 
		while(!b.equals(BigInteger.valueOf(0))) {
			q = a.divide(b);
			r = a.mod(b);
			a = b;
			b = r;
			var tempX1 = x1;
			var tempY1 = y1;
			x1 = x0.subtract(q.multiply(x1));
			y1 = y0.subtract(q.multiply(y1));
			x0 = tempX1;
			y0 = tempY1;
		}
	}
	
	public BigInteger getGCD() {
		return a;
	}
	
	public BigInteger getSecondFactor() {
		if(y0.compareTo(BigInteger.ZERO) <= 0) {
			return aRoot.add(y0);
		}
		return y0;
		
	}

	public BigInteger getY0() {
		return y0;
	}
	public BigInteger getY1() {
		return y1;
	}
	public BigInteger getX0() {
		return x0;
	}
	public BigInteger getX1() {
		return x1;
	}
	
}
