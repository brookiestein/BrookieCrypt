/*
 * Copyright (C) 19aa Lord Brookie
 * Este programa es software libre. Puede redistribuirlo y/o
 * modificarlo bajo los términos de la Licencia Pública General
 * de GNU según es publicada por la Free Software Foundation,
 * bien de la versión 2 de dicha Licencia o bien --según su
 * elección-- de cualquier versión posterior.
 * Este programa se distribuye con la esperanza de que sea
 * útil, pero SIN NINGUNA GARANTÍA, incluso sin la garantía
 * MERCANTIL implícita o sin garantizar la CONVENIENCIA PARA UN
 * PROPÓSITO PARTICULAR. Para más detalles, véase la Licencia
 * Pública General de GNU.
 * Debería haber recibido una copia de la Licencia Pública
 * General junto con este programa. En caso contrario, escriba
 * a la Free Software Foundation, Inc., en 675 Mass Ave,
 * Cambridge, MA 02139, EEUU.
 * Créditos a Martín
*/
package utils;

// Paquetes IO
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Paquetes Security
import java.security.GeneralSecurityException;
import java.security.Key;

public class FEncryptor
{
	private Encryptor encryptor;
	private int bufferSize;

	public FEncryptor()
	{
		this(KeyFactory.AES.randomKey());
	}
	
	public FEncryptor(String password)
	{
		this(KeyFactory.AES.keyFromPassword(password.toCharArray()));
	}
	
	public FEncryptor(Key key)
	{
		this(EncryptorFactory.AES.streamEncryptor(key));
	}
	
	public FEncryptor(Encryptor encryptor)
	{
		super();
		this.encryptor = encryptor;
		bufferSize = 65536;
	}
	
	// Reads and encrypts file src and writes the encrypted result to file.
	public void encrypt(File src, File dest) throws GeneralSecurityException, IOException
	{
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(src);
			os = encryptor.wrapOutputStream(new FileOutputStream(dest));
			copy(is, os);
		} finally {
			if (is != null) {
				is.close();
			}
			
			if (os != null) {
				os.close();
			}
		}
	}
	
	// Reads and decrypts file src and writes the decrypted result to file dest.
	public void decrypt(File src, File dest) throws GeneralSecurityException, IOException
	{
		InputStream is = null;
		OutputStream os = null;
		try {
			is = encryptor.wrapInputStream(new FileInputStream(src));
			os = new FileOutputStream(dest);
			copy(is, os);
		} finally {
			if (is != null) {
				is.close();
			}
			
			if (os != null) {
				os.close();
			}
		}
	}
	
	public void copy(InputStream is, OutputStream os) throws IOException
	{
		byte[] buffer = new byte[bufferSize];
		int read;
		while (0 < (read = is.read(buffer))) {
			os.write(buffer, 0, read);
		}
		os.flush();
	}
	
	public Encryptor getEncryptor()
	{
		return encryptor;
	}
	
	public int getBufferSize()
	{
		return bufferSize;
	}
	
	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}
}
