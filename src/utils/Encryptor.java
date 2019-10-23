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

// Paquetes IO
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Paquetes Security
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

// Paquetes Crypto
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

public class Encryptor
{
	private static final String DEFAULT_ALGORITHM = "AES";
	private String algorithm, algorithmProvider;
	private int ivLength, tLen;
	private Key key;
	private KeySpec keySpec;
	private SecretKeyFactory secretKeyFactory;
	private ThreadLocal<byte[]> ivThreadLocal;
	private ThreadLocal<Cipher> cipherThreadLocal;
	private boolean prependIV, generateIV;
	
	public Encryptor(Key key)
	{
		this(key, DEFAULT_ALGORITHM);
	}
	
	public Encryptor(Key key, String algorithm)
	{
		this(key, algorithm, 0);
	}
	
	public Encryptor(Key key, String algorithm, int ivLength)
	{
		this(key, algorithm, ivLength, 0);
	}
	
	public Encryptor(Key key, String algorithm, int ivLength, int tLen)
	{
		this.key = key;
		this.algorithm = algorithm;
		this.ivLength = ivLength;
		this.ivThreadLocal = new ThreadLocal<>();
		this.cipherThreadLocal = new ThreadLocal<>();
		this.prependIV = this.generateIV = true;
	}
	
	public Encryptor(KeySpec keySpec, SecretKeyFactory secretKeyFactory)
	{
		this(keySpec, secretKeyFactory, DEFAULT_ALGORITHM, 0);
	}
	
	public Encryptor(KeySpec keySpec, SecretKeyFactory secretKeyFactory, String algorithm, int ivLength)
	{
		this(keySpec, secretKeyFactory, DEFAULT_ALGORITHM, ivLength, 0);
	}
	
	public Encryptor(KeySpec keySpec, SecretKeyFactory secretKeyFactory, String algorithm, int ivLength, int tLen)
	{
		this.keySpec = keySpec;
		this.secretKeyFactory = secretKeyFactory;
		this.algorithm = algorithm;
		this.ivLength = ivLength;
		this.tLen = tLen;
		this.ivThreadLocal = new ThreadLocal<>();
		this.cipherThreadLocal = new ThreadLocal<>();
		this.prependIV = this.generateIV = true;
	}
	
	public byte[] encrypt(byte[] message) throws GeneralSecurityException
	{
		return encrypt(message, null);
	}
	
	public byte[] encrypt(byte[] message, byte[] aad) throws GeneralSecurityException
	{
		return encrypt(message, aad, null);
	}
	
	public byte[] encrypt(byte[] message, byte[] aad, byte[] iv) throws GeneralSecurityException
	{
		Cipher cipher = getCipher(true);
		if (iv == null && generateIV && ivLength > 0) {
			iv = generateIV();
		}
		
		if (iv != null) {
			cipher.init(Cipher.ENCRYPT_MODE, getKey(), getAlgorithmParameterSpec(iv));
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, getKey());
		}
		
		ivThreadLocal.set(iv);
		
		if (aad != null) {
			cipher.updateAAD(aad);
		}

		byte[] encrypted;

		if (prependIV && iv != null) {
			int outputSize = cipher.getOutputSize(message.length);
			encrypted = new byte[iv.length + outputSize];
			System.arraycopy(iv, 0, encrypted, 0, iv.length);
			try {
				int nBytes = cipher.doFinal(message, 0, message.length, encrypted, iv.length);
				if (nBytes < outputSize) {
					int excessBytes = outputSize - nBytes;
					byte[] resized = new byte[encrypted.length - excessBytes];
					System.arraycopy(encrypted, 0, resized, 0, resized.length);
					encrypted = resized;
				}
			} catch (ShortBufferException e) {
				throw new RuntimeException(e);
			}
		} else {
			encrypted = cipher.doFinal(message);
		}
		return encrypted;
	}
	
	public byte[] decrypt(byte[] message, byte[] aad) throws GeneralSecurityException
	{
		return decrypt(message, aad, null);
	}

	public byte[] decrypt(byte[] message, byte[] aad, byte[] iv) throws GeneralSecurityException
	{
		Cipher cipher = getCipher(true);
		if (ivLength > 0) {
			if (prependIV) {
				cipher.init(Cipher.DECRYPT_MODE, getKey(), getAlgorithmParameterSpec(message));
				if (aad != null) {
					cipher.updateAAD(aad);
				}
				return cipher.doFinal(message, ivLength, message.length - ivLength);
			} else {
				throw new IllegalStateException("No se pudo obtener IV.");
			}
		} else {
			if (iv != null) {
				cipher.init(Cipher.DECRYPT_MODE, getKey(), getAlgorithmParameterSpec(iv));
			} else {
				cipher.init(Cipher.DECRYPT_MODE, getKey());
			}
			
			if (aad != null) {
				cipher.updateAAD(aad);
			}
			return cipher.doFinal(message);
		}
	}
	
	public byte[] getIV()
	{
		return ivThreadLocal.get();
	}
	
	public void setGeneraeIV(boolean generateIV)
	{
		this.generateIV = generateIV;
	}
	
	public String getAlgorithm()
	{
		return algorithm;
	}
	
	public void setAlgorithmProvider(String algorithmProvider)
	{
		this.algorithmProvider = algorithmProvider;
	}
	
	public Key getKey()
	{
		if (key != null) {
			return key;
		} else if (keySpec != null && secretKeyFactory != null) {
			try {
				return key = secretKeyFactory.generateSecret(keySpec);
			} catch (InvalidKeySpecException e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalStateException("No se pudo producir la llave.");
	}
	
	public CipherInputStream wrapInputStream(InputStream is) throws GeneralSecurityException, IOException
	{
		return wrapInputStream(is, null);
	}

	/*
	 * Wraps an InputStream with a CipherInputStream using this encryptor's cipher.
	 * If an ivLength has been specified and prependIV is set to true this method
	 * will try to read and consume an IV from the InputStream before wrapping it.
	*/
	public CipherInputStream wrapInputStream(InputStream is, byte[] iv) throws GeneralSecurityException, IOException
	{
		Cipher cipher = getCipher(true);
		if(iv == null && ivLength > 0) {
			if(prependIV) {
				iv = new byte[ivLength];
				is.read(iv);
			} else {
				throw new IllegalStateException("No se pudo obtener IV");
			}
		}

		if(iv != null) {
			cipher.init(Cipher.DECRYPT_MODE, getKey(), getAlgorithmParameterSpec(iv));
		} else {
			cipher.init(Cipher.DECRYPT_MODE, getKey());
		}
		return new CipherInputStream(is, cipher);
	}

	/*
	 * Wraps an OutputStream with a CipherOutputStream using this encryptor's cipher.
	 * If an ivLength has been specified or an explicit IV has been set during construction
	 * and prependIV is set to true this method will write an IV to the OutputStream before wrapping it.
	*/
	public CipherOutputStream wrapOutputStream(OutputStream os) throws GeneralSecurityException, IOException
	{
		return wrapOutputStream(os, null);
	}

	/*
	 * Wraps an OutputStream with a CipherOutputStream using this encryptor's cipher.
	 * If an ivLength has been specified or an explicit IV has been set during construction
	 * and prependIV is set to true this method will write an IV to the OutputStream before wrapping it.
	*/
	public CipherOutputStream wrapOutputStream(OutputStream os, byte[] iv) throws GeneralSecurityException, IOException
	{
		Cipher cipher = getCipher(true);
		if(iv == null && generateIV && ivLength > 0) {
			iv = generateIV();
		}

		if(iv != null) {
			cipher.init(Cipher.ENCRYPT_MODE, getKey(), getAlgorithmParameterSpec(iv));
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, getKey());
			iv = cipher.getIV();
		}

		ivThreadLocal.set(iv);
		if(prependIV && iv != null) {
			os.write(iv);
		}
		return new CipherOutputStream(os, cipher);
	}

	// Returns the thread local cipher.
	public Cipher getCipher() throws GeneralSecurityException
	{
		return getCipher(false);
	}

	private Cipher getCipher(boolean create) throws GeneralSecurityException
	{
		Cipher cipher = cipherThreadLocal.get();
		if(cipher == null || create) {
			cipher = createCipher();
			cipherThreadLocal.set(cipher);
		}
		return cipher;
	}

	
	// Creates the cipherThreadLocal
	private Cipher createCipher() throws GeneralSecurityException
	{
		if(algorithmProvider != null) {
			return Cipher.getInstance(algorithm, algorithmProvider);
		} else {
			return Cipher.getInstance(algorithm);
		}
	}

	// Returns the algorithm parameter specification.
	private AlgorithmParameterSpec getAlgorithmParameterSpec(byte[] ivBuffer)
	{
		int length = ivLength == 0  && ivBuffer != null ? ivBuffer.length : ivLength;
		String[] parts = algorithm.split("/");
		if(parts.length > 1 && parts[1].equalsIgnoreCase("GCM")) {
			return new GCMParameterSpec(tLen > 0 ? tLen: 128, ivBuffer, 0, length);
		}
		return new IvParameterSpec(ivBuffer, 0, length);
	}

	// Generates an initialization vector.
	private byte[] generateIV()
	{
		byte[] iv = new byte[ivLength];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return iv;
	}
}
