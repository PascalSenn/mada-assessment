package ch.fhnw.mada.rsa;

import java.math.BigInteger;
import java.util.Random;

import ch.fhnw.mada.rsa.utils.Tuple;
import ch.fhnw.madam.rsa.eea.ExtendedEuclideanAlgorithm;

public class TokenPair {
	private BigInteger p;
	private BigInteger q;
	private BigInteger e;
	private BigInteger d;
	private BigInteger n;
	private BigInteger phiN;
	private Random random = new Random();

	/***
	 * Generates a pair of primes that are not equal to each other
	 * @param bitSize
	 * @return
	 */
	public static Tuple<BigInteger> generatePrimePair(int bitSize, Random random){

		var p = BigInteger.probablePrime(bitSize, random);
		var q = BigInteger.probablePrime(bitSize, random);
		while(p.equals(q)) {
			q = BigInteger.probablePrime(bitSize, random);
			
		}
		return new Tuple<BigInteger>(p, q);
	}

	/***
	 * Generates pair of numbers that are coprime to phiN and also  e*d % phiN = 1;
	 * @param bitSize
	 * @return
	 */
	public static Tuple<BigInteger> generateNumberPairCoprimeToPhiN(int bitSize, BigInteger phiN, Random random) {
		var e = new BigInteger(bitSize, random);
		var eea = ExtendedEuclideanAlgorithm.calculate(phiN, e);
		while(e.compareTo(phiN) <= 0 && !eea.getGCD().equals(BigInteger.ONE)) {
			e = new BigInteger(bitSize, random);
			eea = ExtendedEuclideanAlgorithm.calculate(phiN, e);
		}
		return new Tuple<BigInteger>(e, eea.getSecondFactor());
	}
	
	public static TokenPair createToken(int bitsize) {
		return new TokenPair(bitsize); 
	}
	
	private TokenPair(int bitSize) {
		var pqTuple = generatePrimePair(bitSize, random); 
		p = pqTuple.v1;
		q = pqTuple.v2;
		n = p.multiply(q);
		phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		var edTuple = generateNumberPairCoprimeToPhiN(bitSize, phiN, random);
		e = edTuple.v1;
		d = edTuple.v2;
	}

	public Tuple<BigInteger>  publicKey() {
		return new Tuple<BigInteger>(n,e);
	}

	public Tuple<BigInteger>  privateKey() {
		return new Tuple<BigInteger>(n,d);
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getE() {
		return e;
	}
	
	public BigInteger getD() {
		return d;
	}
	
	
}
