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
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

// Paquetes SECURITY
import java.security.GeneralSecurityException;
import java.security.DigestException;

// Paquetes UTIL
import java.util.ArrayList;
import utils.FEncryptor;
import java.util.concurrent.CancellationException;

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
	private PrincipalSheet s;
	private static final String SHA256 = "SHA-256";
	private static final String SHA512 = "SHA-512";
	
	public ManageCrypt(PrincipalSheet s)
	{
		this.s = s;
		ext = null;
		file = null;
		zipFile = null;
		zipParameters = null;
	}

	private void compress() throws ZipException
	{
		try {
			openFile("Abrir archivo o directorio para comprimir en zip", null);
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
			password = GetPassword("Introduzca la contraseña para el cifrado y compresión ZIP:");
			if (password != null && !password.equals("")) {
				zipParameters.setPassword(password);
				if (file.isFile()) {
					zipFile.addFile(file, zipParameters);
				} else {
					zipFile.addFolder(path, zipParameters);
				}
			} else {
				JOptionPane.showMessageDialog(s, "Compresión cancelada.", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (CancellationException e) {}
	}

	private void descompress()
	{
		final String messageSuccess = "Descompresión finalizada.";
		final String messageError = "El archivo indicado no parece ser un archivo zip válido.\n" +
		"¿Estará corrupto?";
		try {
			String path = file.getAbsolutePath();
			ext = path.substring(path.lastIndexOf('.'), path.length());
			String password = GetPassword("Introduzca la contraseña para la descompresión ZIP:");
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
				JOptionPane.showMessageDialog(s, "Descompresión cancelada.", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (ZipException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String GetPassword(@NonNull String message)
	{
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("La contraseña es demasiado débil.");
		messages.add(message);
		messages.add("Establecer contraseña");
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
	
	private void generateSum(String algorithm)
	{
		try {
			String path = file.getAbsolutePath();
			String ext = path.substring(path.lastIndexOf('.'), path.length());
			ManageHash hash = new ManageHash(algorithm, new File(path.replace(ext, ".enc")), s);
			hash.createHash();
			hash.saveHash();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void encrypt()
	{
		String messageError = "Ocurrió un error mientras se comprimía ";
		final String messageSuccess = "El archivo ha sido cifrado.";
		try {
			compress();
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
			String password = GetPassword("Introduzca la contraseña para el cifrado:");
			FEncryptor fe = new FEncryptor(password);
			fe.encrypt(file, destFile);
			generateSum(SHA256);
			generateSum(SHA512);
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

	private void verifySums() throws IOException, DigestException
	{
		String path = file.getAbsolutePath();
		ext = path.substring(path.lastIndexOf('.'), path.length());

		File filesha256 = new File(path.replace(ext, ".sha256sum"));
		File filesha512 = new File(path.replace(ext, ".sha512sum"));

		if (!filesha256.exists() || !filesha512.exists()) {
			throw new IOException("Archivo(s) de firma hash no encontrado(s).");
		}

		FileReader fr256 = new FileReader(filesha256);
		BufferedReader br256 = new BufferedReader(fr256);
		String tmp, read256 = null;
		
		FileReader fr512 = new FileReader(filesha512);
		BufferedReader br512 = new BufferedReader(fr512);
		String read512 = null;

		while ((tmp = br256.readLine()) != null) {
			if (tmp != null) {
				read256 = tmp;
			}
		}
		while ((tmp = br512.readLine()) != null) {
			if (tmp != null) {
				read512 = tmp;
			}
		}

		ManageHash hash = new ManageHash();
		hash.setFile(file);
		hash.setAlgorithm(SHA256);
		hash.setParent(s);
		hash.createHash();
		String originalHash = hash.getHash();
		if (originalHash.equals(read256)) {
			hash.setAlgorithm(SHA512);
			hash.createHash();
			originalHash = hash.getHash();
			if (originalHash.equals(read512)) {
				br256.close();
				br512.close();
			} else {
				br256.close();
				br512.close();
				throw new DigestException("Firma hash sha512 no coincide.");
			}
		} else {
			br256.close();
			br512.close();
			throw new DigestException("Firma hash sha256 no coincide.");
		}
	}
	
	public void decrypt()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo cifrado de BrookieCrypt", "enc");
		try {
			openFile("Abrir archivo para descifrar (.enc)", filter);
			verifySums();
			String path = file.getAbsolutePath();
			ext = path.substring(path.lastIndexOf('.'), path.length());
			File destFile = new File(path.replace(ext, ".zip"));
			String password = GetPassword("Introduzca la contraseña de cifrado:");
			FEncryptor fe = new FEncryptor(password);
			fe.decrypt(file, destFile);
			file = new File(destFile.getAbsolutePath());
			descompress();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (DigestException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);			
		} catch (CancellationException e) {}
	}

	public void destroyFiles()
	{
		
	}

	private void openFile(@NonNull String titleWindow, @Nullable FileNameExtensionFilter filter)
	throws CancellationException, IOException
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
		int status = chooser.showOpenDialog(s);
		if (status != JFileChooser.APPROVE_OPTION) {
			throw new CancellationException("Elección de archivo cancelada.");
		}
		
		file = chooser.getSelectedFile();
		if (!file.exists()) {
			file.createNewFile();
		}
	}
}
