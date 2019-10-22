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
		final String error = "La contraseña es demasiado débil.";
		final String message = "Introduzca la contraseña para el cifrado y compresión ZIP:";
		final String title = "Establecer contraseña";
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
		String messageError = "Ocurrió un error mientras se comprimía ";
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
				JOptionPane.showMessageDialog(null, "Ocurrió un error en la creación del archivo.",
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
