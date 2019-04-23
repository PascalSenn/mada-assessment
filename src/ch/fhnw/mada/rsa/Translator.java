package ch.fhnw.mada.rsa;

import java.math.BigInteger;

import ch.fhnw.mada.rsa.fea.FastExponentiationAlgorithm;
import ch.fhnw.mada.rsa.utils.Tuple;

public class Translator {
	private final Tuple<BigInteger> privateKey; 
	private final Tuple<BigInteger> publicKey;
	public Translator( Tuple<BigInteger> privateKey, Tuple<BigInteger> publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	/***
	 * Enrypts a message
	 * @param message
	 * @return
	 * @throws RSAOutOfRangeException
	 */
	public BigInteger encryptMessage(BigInteger message) throws RSAOutOfRangeException {
		if(message.compareTo(publicKey.v1) >= 0) {
			throw new RSAOutOfRangeException();
		}
		return FastExponentiationAlgorithm.calculate(message, publicKey.v2, publicKey.v1).getResult();
	}

	/***
	 * Decrypts a message
	 * @param message
	 * @return
	 * @throws RSAOutOfRangeException
	 */
	public BigInteger decryptMessage(BigInteger message) throws RSAOutOfRangeException {
		if(message.compareTo(privateKey.v1) >= 0) {
			throw new RSAOutOfRangeException();
		}
		return FastExponentiationAlgorithm.calculate(message, privateKey.v2, privateKey.v1).getResult();
	}
	
}
