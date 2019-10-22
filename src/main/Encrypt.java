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
*/
package main;

// Paquetes IO
import java.io.File;
import java.io.IOException;

// Paquetes UTIL
import java.util.ArrayList;

// Paquetes SWING
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

// Paquetes NET
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Encrypt
{
	private File file;
	private String ext;
	private ZipFile zipFile;
	private ZipParameters zipParameters;
	
	public Encrypt()
	{
		ext = null;
		file = null;
		zipFile = null;
		zipParameters = null;
	}
	
	private void compress() throws ZipException
	{
		boolean status = openFile();
		if (status) {
			ArrayList<File> files = null;
			String message;
			if (file.isFile()) {
				zipFile = new ZipFile(file.getAbsolutePath().replace(ext, ".zip"));
			} else {
				files = new ArrayList<File>();
				File[] listFiles = file.listFiles();
				zipFile = new ZipFile(file.getAbsolutePath() + ".zip");
				for (int i=0; i<listFiles.length; i++) {
					files.add(listFiles[i]);
				}
			}
			zipParameters = new ZipParameters();
			zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			zipParameters.setPassword(GetPassword());
			if (file.isFile()) {
				zipFile.addFile(file, zipParameters);
				message = "El archivo ha sido comprimido.";
			} else {
				zipFile.addFolder(file.getAbsoluteFile(), zipParameters);
				message = "La carpeta ha sido comprimida.";
			}
			JOptionPane.showMessageDialog(new PrincipalSheet(), message, "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private String GetPassword()
	{
		PrincipalSheet s = new PrincipalSheet();
		String password;
		final String error = "La contrase�a es demasiado d�bil.";
		final String message = "Introduzca la contrase�a para el cifrado y compresi�n ZIP:";
		final String title = "Establecer contrase�a";
		while (true) {
			password = JOptionPane.showInputDialog(s, message, title, JOptionPane.PLAIN_MESSAGE);
			if (password.length() <= 8) {
				JOptionPane.showMessageDialog(s, error, "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				return password;
			}
		}
	}

	public void encrypt()
	{
		String messageError = "Ocurri� un error mientras se comprim�a ";
		try {
			compress();
		} catch (ZipException e) {
			if (file.isFile()) {
				messageError += "el archivo.";
			} else {
				messageError += "la carpeta.";
			}
			JOptionPane.showMessageDialog(new PrincipalSheet(), messageError, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void decrypt()
	{
		
	}
	
	private boolean openFile()
	{
		JFileChooser chooser;
		String path = "";
		if (System.getProperty("os.name").substring(0, 5).equalsIgnoreCase("windo")) {
			path = "C:/Users/" + System.getProperty("user.name") + "/Desktop";
		} else if (System.getProperty("os.name").substring(0, 5).equalsIgnoreCase("linux")) {
			path = "/home/" + System.getProperty("user.name");
		}
		chooser = new JFileChooser(path);
		chooser.setDialogTitle("Abrir archivo");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int status = chooser.showOpenDialog(new PrincipalSheet());
		if (status != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		
		file = chooser.getSelectedFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Ocurri� un error en la creaci�n del archivo.",
				"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if (file.isFile()) {
			ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'), file.getAbsolutePath().length());
		}
		return true;
	}
}
