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
import java.security.NoSuchAlgorithmException;
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
	private File file, originalFile, originalFileZip;
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
		originalFile = null;
		originalFileZip = null;
		zipFile = null;
		zipParameters = null;
	}
	
	public File getOriginalFile()
	{
		return originalFile;
	}
	
	public File getOriginalFileZip()
	{
		return originalFileZip;
	}

	public void compress(@NonNull File file) throws ZipException, CancellationException
	{
		this.file = file;
		String path = file.getAbsolutePath();
		String password;
		if (file.isFile()) {
			ext = path.substring(path.lastIndexOf('.'), path.length());
			originalFileZip = new File(file.getAbsolutePath().replace(ext, ".zip"));
			zipFile = new ZipFile(file.getAbsolutePath().replace(ext, ".zip"));
		} else {
			originalFileZip = new File(file.getAbsolutePath() + ".zip");
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
			throw new CancellationException("Compresión cancelada debido a la ausencia de contraseña.");
		}
	}

	public void descompress() throws ZipException, CancellationException
	{
		final String messageError = "El archivo indicado no parece ser un archivo zip válido.\n" +
		"¿Estará corrupto?";
		String path = file.getAbsolutePath();
		ext = path.substring(path.lastIndexOf('.'), path.length());
		String password = GetPassword("Introduzca la contraseña para la descompresión ZIP:");
		if (password != null) {
			originalFileZip = new File(path.replace(ext, ".zip"));
			zipFile = new ZipFile(path);
			zipFile.setPassword(password);
			if (zipFile.isValidZipFile()) {
				zipFile.extractAll(path.replace(ext, ""));
			} else {
				JOptionPane.showMessageDialog(s, messageError, "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			throw new CancellationException("Descompresión cancelada.");
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

	public void encrypt() throws CancellationException
	{
		final String messageSuccess = "El archivo ha sido cifrado.";
		try {
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
			if (password == null) {
				throw new CancellationException("Cifrado cancelado debido a la ausencia de contraseña.");
			}
			FEncryptor fe = new FEncryptor(password);
			fe.encrypt(file, destFile);
			generateSum(SHA256);
			generateSum(SHA512);
			JOptionPane.showMessageDialog(s, messageSuccess, "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
		} catch (GeneralSecurityException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void verifyHash(@NonNull String algorithm, boolean showMessage)
	throws IOException, DigestException, NoSuchAlgorithmException
	{
		String path = file.getAbsolutePath();
		ext = path.substring(path.lastIndexOf('.'), path.length());
		File fileHash;
		if (algorithm.equals(SHA256)) {
			fileHash = new File(path.replace(ext, ".sha256sum"));
		} else if (algorithm.equals(SHA512)) {
			fileHash = new File(path.replace(ext, ".sha512sum"));
		} else {
			throw new NoSuchAlgorithmException("Algoritmo: \"" + algorithm + "\" no válido.");
		}
		if (!fileHash.exists()) {
			throw new IOException("Archivo de firma hash " + algorithm + " no encontrado.");
		}
		FileReader fr = new FileReader(fileHash);
		BufferedReader br = new BufferedReader(fr);
		String read = br.readLine();
		br.close();
		ManageHash hash = new ManageHash();
		hash.setFile(file);
		hash.setAlgorithm(algorithm);
		hash.setParent(s);
		hash.createHash();
		String originalHash = hash.getHash();
		if (!originalHash.equals(read)) {
			throw new DigestException("Firma hash " + algorithm + " no coincide.");
		} else if (showMessage) {
			JOptionPane.showMessageDialog(s, "Las firmas hash " + algorithm + " coinciden.",
			"Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void decrypt() throws IOException, DigestException, GeneralSecurityException, CancellationException
	{
		verifyHash(SHA256, false);
		verifyHash(SHA512, false);
		String path = file.getAbsolutePath();
		ext = path.substring(path.lastIndexOf('.'), path.length());
		File destFile = new File(path.replace(ext, ".zip"));
		path = destFile.getAbsolutePath();
		String password = GetPassword("Introduzca la contraseña de cifrado:");
		if (password == null) {
			throw new CancellationException("Descifrado cancelado debido a la ausencia de contraseña.");
		}
		FEncryptor fe = new FEncryptor(password);
		fe.decrypt(file, destFile);
		file = new File(path);
		originalFileZip = new File(path);
	}

	public void destroyFiles(@NonNull File file)
	{
		try {
			DestroyFiles.destroy(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(s, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void openFile(@NonNull String titleWindow, @Nullable FileNameExtensionFilter filter)
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
		originalFile = new File(file.getAbsolutePath());
	}
}
