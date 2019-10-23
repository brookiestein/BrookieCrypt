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
*/
package main;

// Paquetes Awt
import java.awt.Component;

// Paquetes IO
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;

// Paquetes Math
import java.math.BigInteger;

// Paquetes Security
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

// Paquetes Annotation
import org.eclipse.jdt.annotation.NonNull;

public class ManageHash
{
	private MessageDigest md;
	private File file;
	private InputStream is;
	private byte[] buffer, shasum;
	private int byteRead;
	private String algorithm, finalOutput;
	private Component parent;

	public ManageHash()
	{
		algorithm = null;
		file = null;
		parent = null;
	}
	
	public ManageHash(@NonNull String algorithm)
	{
		this.algorithm = algorithm;
		file = null;
		parent = null;
	}
	
	public ManageHash(@NonNull File file)
	{
		this.file = file;
		algorithm = null;
		parent = null;
	}
	
	public ManageHash(@NonNull Component parent)
	{
		this.parent = parent;
		file = null;
		algorithm = null;
	}
	
	public ManageHash(@NonNull String algorithm, @NonNull File file, @NonNull Component parent)
	{
		this.algorithm = algorithm;
		this.parent = parent;
		this.file = file;
	}
	
	public void setAlgorithm(@NonNull String algorithm)
	{
		this.algorithm = algorithm;
	}
	
	public String getAlgorithm()
	{
		return algorithm;
	}
	
	public void setFile(@NonNull File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void setParent(@NonNull Component parent)
	{
		this.parent = parent;
	}
	
	public Component getParent()
	{
		return parent;
	}

	public void createHash() throws IOException
	{
		is = new FileInputStream(file);
		buffer = new byte[(int) file.length()];
		try {
			md = MessageDigest.getInstance(algorithm);
			while (0 < (byteRead = is.read(buffer))) {
				md.update(buffer, 0, byteRead);
			}
			shasum = md.digest();
			BigInteger bi = new BigInteger(1, shasum);
			finalOutput = bi.toString(16);
			is.close();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(parent, "Algoritmo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getHash()
	{
		return finalOutput;
	}
	
	public void saveHash() throws IOException
	{
		String path = file.getAbsolutePath();
		String ext = path.substring(path.lastIndexOf('.'), path.length());

		if (algorithm.equals("SHA-256")) {
			file = new File(path.replace(ext, ".sha256sum"));
		} else {
			file = new File(path.replace(ext, ".sha512sum"));
		}
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(finalOutput);
		bw.flush();
		bw.close();
	}
}
