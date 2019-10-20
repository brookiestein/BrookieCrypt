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
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Paquetes ZIP
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Paquetes SWING
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Encrypt implements Closeable
{
	private static File file;
	private static ZipOutputStream zipStream;
	private static ZipEntry zipEntry;
	private static FileInputStream fileInput;
	private static FileOutputStream fileOutput;

	public Encrypt()
	{
		file = null;
		zipStream = null;
		zipEntry = null;
		fileInput = null;
		fileOutput = null;
	}
	
	private boolean compress()
	{
		boolean status = openFile();
		if (status) {
			String filename = file.getName();
			String ext = filename.substring(filename.lastIndexOf('.'), filename.length());
			try {
				fileOutput = new FileOutputStream(file.getAbsolutePath().replace(ext, ".zip"));
				zipStream = new ZipOutputStream(fileOutput);
				zipEntry = new ZipEntry(filename);
				try {
					zipStream.putNextEntry(zipEntry);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				fileInput = new FileInputStream(file.getAbsolutePath());
				int read;
				byte[] buffer = new byte[1024];
				try {
					while (0 < (read = fileInput.read(buffer))) {
						zipStream.write(buffer, 0, read);
					}
					close();
					JOptionPane.showMessageDialog(new PrincipalSheet(), "El archivo ha sido comprimido.",
					"Aviso", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(new PrincipalSheet(), "Ocurri� un error mientras se le�a " +
					"el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(new PrincipalSheet(), "Archivo no encontrado",
				"Error", JOptionPane.ERROR_MESSAGE);
			}
			return true;
		} else {
			return false;
		}
	}

	public void encrypt()
	{
		boolean status = compress();
		if (status) {
			
		} else {
			JOptionPane.showMessageDialog(new PrincipalSheet(), "Ocurri� un error en la compresi�n",
			"Error", JOptionPane.ERROR_MESSAGE);
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
	
	public void close()
	{
		try {
			fileInput.close();
			zipStream.closeEntry();
			zipStream.close();
			fileOutput.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new PrincipalSheet(), "Ocurri� un error mientras se intentaba " +
			"cerrar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
