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

// Paquetes SECURITY
import java.security.GeneralSecurityException;

// Paquetes UTIL
import java.util.ArrayList;
import utils.FEncryptor;

// Paquetes SWING
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

// Paquetes NET
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

// Paquetes para anotaciones
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class ManageCrypt
{
	private File file;
	private String ext;
	private ZipFile zipFile;
	private ZipParameters zipParameters;
	
	public ManageCrypt()
	{
		ext = null;
		file = null;
		zipFile = null;
		zipParameters = null;
	}
	
	private boolean compress() throws ZipException
	{
		boolean status = openFile("Abrir archivo o directorio para comprimir en zip", null);
		if (status) {
			String path = file.getAbsolutePath();
			String password;
			if (file.isFile()) {
				ext = path.substring(path.lastIndexOf('.'), path.length());
				zipFile = new ZipFile(file.getAbsolutePath().replace(ext, ".zip"));
			} else {
				zipFile = new ZipFile(file.getAbsolutePath() + ".zip");
			}
			zipParameters = new ZipParameters();
			zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			password = GetPassword("Introduzca la contrase�a para el cifrado y compresi�n ZIP:");
			if (password != null && !password.equals("")) {
				zipParameters.setPassword(password);
				if (file.isFile()) {
					zipFile.addFile(file, zipParameters);
				} else {
					zipFile.addFolder(path, zipParameters);
				}
				return true;
			} else {
				JOptionPane.showMessageDialog(new PrincipalSheet(), "Compresi�n cancelada.",
				"Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		return false;
	}

	private void descompress()
	{
		PrincipalSheet s = new PrincipalSheet();
		final String messageSuccess = "Descompresi�n finalizada.";
		final String messageError = "El archivo indicado no parece ser un archivo zip v�lido.\n" +
		"�Estar� corrupto?";
		try {
			String path = file.getAbsolutePath();
			ext = path.substring(path.lastIndexOf('.'), path.length());
			String password = GetPassword("Introduzca la contrase�a para la descompresi�n ZIP:");
			if (password != null) {
				zipFile = new ZipFile(path);
				zipFile.setPassword(password);
				if (zipFile.isValidZipFile()) {
					zipFile.extractAll(path.replace(ext, ""));
					JOptionPane.showMessageDialog(s, messageSuccess, "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(s, messageError, "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(s, "Descompresi�n cancelada.", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (ZipException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String GetPassword(@NonNull String message)
	{
		PrincipalSheet s = new PrincipalSheet();
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("La contrase�a es demasiado d�bil.");
		messages.add(message);
		messages.add("Establecer contrase�a");
		messages.add("Error");
		while (true) {
			String input = JOptionPane.showInputDialog(s, messages.get(1), messages.get(2), JOptionPane.PLAIN_MESSAGE);
			if (input != null && input.length() < 8) {
				JOptionPane.showMessageDialog(s, messages.get(0), messages.get(3), JOptionPane.ERROR_MESSAGE);				
			} else {
				return input;
			}
		}
	}

	public void encrypt()
	{
		String messageError = "Ocurri� un error mientras se comprim�a ";
		final String messageSuccess = "El archivo ha sido cifrado.";
		PrincipalSheet s = new PrincipalSheet();
		try {
			boolean status = compress();
			if (!status) {
				return;
			}
			String path = file.getAbsolutePath();
			File destFile;
			if (file.isFile()) {
				ext = path.substring(path.lastIndexOf('.'), path.length());
				destFile = new File(file.getAbsolutePath().replace(ext, ".enc"));
				file = new File(path.replace(ext, ".zip"));
			} else {
				if (!path.endsWith(".zip")) {
					file = new File(path + ".zip");
				}
				destFile = new File(file.getAbsolutePath() + ".enc");
			}
			String password = GetPassword("Introduzca la contrase�a para el cifrado:");
			FEncryptor fe = new FEncryptor(password);
			fe.encrypt(file, destFile);
			JOptionPane.showMessageDialog(s, messageSuccess, "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
		} catch (ZipException e) {
			if (file.isFile()) {
				messageError += "el archivo.";
			} else {
				messageError += "la carpeta.";
			}
			JOptionPane.showMessageDialog(s, messageError, "Error", JOptionPane.ERROR_MESSAGE);
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void decrypt()
	{
		PrincipalSheet s = new PrincipalSheet();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo cifrado de BrookieCrypt", "enc");
		boolean status = openFile("Abrir archivo para descifrar (.enc)", filter);
		if (!status) {
			return;
		}

		try {
			String path = file.getAbsolutePath();
			ext = path.substring(path.lastIndexOf('.'), path.length());
			File destFile = new File(path.replace(ext, ".zip"));
			String password = GetPassword("Introduzca la contrase�a de cifrado:");
			FEncryptor fe = new FEncryptor(password);
			fe.decrypt(file, destFile);
			file = new File(destFile.getAbsolutePath());
			descompress();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void destroyFiles()
	{
		
	}

	private boolean openFile(@NonNull String titleWindow, @Nullable FileNameExtensionFilter filter)
	{
		JFileChooser chooser;
		String path = "";
		if (System.getProperty("os.name").substring(0, 5).equalsIgnoreCase("windo")) {
			path = "C:/Users/" + System.getProperty("user.name") + "/Desktop";
		} else if (System.getProperty("os.name").substring(0, 5).equalsIgnoreCase("linux")) {
			path = "/home/" + System.getProperty("user.name");
		}
		chooser = new JFileChooser(path);
		chooser.setDialogTitle(titleWindow);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (filter != null) {
			chooser.setFileFilter(filter);
		}
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
		return true;
	}
}
