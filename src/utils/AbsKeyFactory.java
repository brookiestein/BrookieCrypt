/*
 * Copyright (C) 19aa Lord Brookie
 * Este programa es software libre. Puede redistribuirlo y/o
 * modificarlo bajo los t�rminos de la Licencia P�blica General
 * de GNU seg�n es publicada por la Free Software Foundation,
 * bien de la versi�n 2 de dicha Licencia o bien --seg�n su
 * elecci�n-- de cualquier versi�n posterior.
 * Este programa se distribuye con la esperanza de que sea
 * �til, pero SIN NINGUNA GARANT�A, incluso sin la garant�a
 * MERCANTIL impl�cita o sin garantizar la CONVENIENCIA PARA UN
 * PROP�SITO PARTICULAR. Para m�s detalles, v�ase la Licencia
 * P�blica General de GNU.
 * Deber�a haber recibido una copia de la Licencia P�blica
 * General junto con este programa. En caso contrario, escriba
 * a la Free Software Foundation, Inc., en 675 Mass Ave,
 * Cambridge, MA 02139, EEUU.
 * Cr�ditos a Mart�n
*/
package utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

// Abstract KeyFactory implementation for creating secure keys.
public abstract class AbsKeyFactory implements KeyFactory
{
	public static final byte[] DEFAULT_SALT = new byte[] { 73, 32, -12, -103, 88, 14, -44,
	9, -119, -42, 5, -63, 102, -11, -104, 66, -17, 112, 55, 44, 18, -46, 30, -6, -55, 28,
	-54, 12, 39, 110, 63, 125 };

	public static final int DEFAULT_ITERATION_COUNT = 65536;
	private String algorithm;
	private byte[] salt;
	private int iterationCount;
	private int maximumKeyLength;

	public AbsKeyFactory(String algorithm, int maximumKeyLength)
	{
		this(algorithm, maximumKeyLength, DEFAULT_SALT, DEFAULT_ITERATION_COUNT);
	}

	public AbsKeyFactory(String algorithm, int maximumKeyLength, byte[] salt, int iterationCount)
	{
		this.algorithm = algorithm;
		this.maximumKeyLength = maximumKeyLength;
		this.salt = salt;
		this.iterationCount = iterationCount;
	}

	// Sets the salt to be used by this factory.
	public void setSalt(byte[] salt)
	{
		this.salt = salt;
	}

	// Sets the amount of hashing iterations to be used by this factory.
	public void setIterationCount(int iterationCount)
	{
		this.iterationCount = iterationCount;
	}

	@Override
	public final Key keyFromPassword(char[] password)
	{
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			int keyLength = Math.min(Cipher.getMaxAllowedKeyLength(algorithm), maximumKeyLength);
			KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);
			SecretKey tmp = factory.generateSecret(spec);
			return new SecretKeySpec(tmp.getEncoded(), algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
    public final Key randomKey()
	{
    	try {
    		int keyLength = Math.min(Cipher.getMaxAllowedKeyLength(algorithm), maximumKeyLength);
    		return randomKey(keyLength);
    	} catch(NoSuchAlgorithmException e) {
    		throw new RuntimeException(e);
    	}
    }

	@Override
    public final Key randomKey(int size)
	{
    	try {
    		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
    		keyGenerator.init(size);
    		return keyGenerator.generateKey();
    	} catch(NoSuchAlgorithmException e) {
    		throw new RuntimeException(e);
    	}
    }
}
