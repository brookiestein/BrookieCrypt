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
import java.io.IOException;

// Paquetes Security
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

// Paquetes UTIL
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

// Paquetes AWT
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

// Paquetes SWING
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

// Paquetes Annotation
import org.eclipse.jdt.annotation.NonNull;

// Paquetes Zip
import net.lingala.zip4j.exception.ZipException;

class PrincipalSheet extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final String name = "BrookieCrypt";
	private static final String version = "0.0.4";
	private static final String license = "GNU GPL v2";
	private static final String author = "Lord Brookie";

	private EventManager listener;
	private JLabel text, message;
	private JMenuBar bar;
	private ArrayList<JMenu> menu;
	private ArrayList<JMenuItem> items;
	private JComboBox<String> combo;
	private JCheckBox checkDeleteFile;
	private JPanel s, s2;
	private ManageCrypt ec;
	private JButton election;
	private final String crypt = "Cifrar un archivo o directorio";
	private final String decrypt = "Descifrar un archivo";
	private String frag, messageCompress, messageDescompress, messageCipher, messageDecipher, messageDestroy;
	private String messageHash;
	
	public PrincipalSheet()
	{		
		listener = new EventManager();
		setLayout(new BorderLayout());
		setFocusable(true);
		addKeyListener(listener);

		text = new JLabel("¿Qué desea hacer?");
		text.setBounds(370, 130, 150, 20);

		messageCompress = "Espere un momento mientras se comprime el archivo. Esto puede tardar " +
		"un tiempo, dependiendo el tamaño del archivo.";
		frag = messageCompress.substring(messageCompress.indexOf('.') + 1, messageCompress.length());
		messageDescompress = "Espere un momento mientras se descomprime el archivo. " + frag;
		messageCipher = "Espere un momento mientras se cifra el archivo. " + frag;
		messageDecipher = "Espere un momento mientras se descifra el  archivo." + frag;
		messageDestroy = "Espere un momento mientras se destruye el archivo."  + frag;
		messageHash = "Espere un momento mientras se verifican las firmas hash." + frag;

		items = new ArrayList<JMenuItem>();
		addItem("Salir (CTRL + Q)");
		addItem("Cifrar un archivo o directorio (CTRL + E)");
		addItem(crypt);
		addItem("Descifrar un archivo (CTRL + D)");
		addItem(decrypt);
		addItem("Acerca de (CTRL + I)");
		addItem("Destruir archivo(s)");
		addItem("Verificar firma hash sha256");
		addItem("Verificar firma hash sha512");
		addItem("Ayuda");

		combo = new JComboBox<String>();
		combo.addItem("Nada");
		combo.addItem(items.get(2).getText());
		combo.addItem(items.get(4).getText());
		combo.addItem("Destruir archivo(s)");
		combo.addItem("Verificar firma hash sha256");
		combo.addItem("Verificar firma hash sha512");
		combo.setBounds(280, 160, 200, 25);
		combo.addKeyListener(listener);
		combo.addMouseListener(listener);

		election = new JButton("Elegir");
		election.setBounds(490, 160, 70, 25);
		election.addActionListener(listener);
		election.addKeyListener(listener);
		election.addMouseListener(listener);

		checkDeleteFile = new JCheckBox("Destruir archivo(s)", true);
		checkDeleteFile.addKeyListener(listener);
		checkDeleteFile.addMouseListener(listener);
		checkDeleteFile.setBounds(350, 190, 135, 25);

		menu = new ArrayList<JMenu>();
		menu.add(new JMenu("Archivo"));
		menu.add(new JMenu("Herramientas"));
		menu.add(new JMenu("Ayuda"));
		addItemToBar();

		bar = new JMenuBar();
		bar.add(menu.get(0));
		bar.add(menu.get(1));
		bar.add(menu.get(2));
		bar.addMouseListener(listener);

		ec = new ManageCrypt(this);

		s = new JPanel(null);
		s.addMouseListener(listener);
		s.add(text);
		s.add(combo);
		s.add(election);
		s.add(checkDeleteFile);
		s.setFocusable(true);
		s.addKeyListener(listener);

		message = new JLabel("");

		s2 = new JPanel();
		s2.add(message);

		add(bar, BorderLayout.NORTH);
		add(s, BorderLayout.CENTER);
		add(s2, BorderLayout.SOUTH);
	}
	
	private void addItem(String name)
	{
		items.add(new JMenuItem(name));
		items.get(items.size() - 1).addActionListener(listener);
	}
	
	private void addItemToBar()
	{
		menu.get(0).add(items.get(0));
		menu.get(1).add(items.get(1));
		menu.get(1).add(items.get(3));
		menu.get(2).add(items.get(9));
		menu.get(2).add(items.get(5));
		menu.get(1).add(items.get(6));
		menu.get(1).add(items.get(7));
		menu.get(1).add(items.get(8));
	}
	
	private final class EventManager extends KeyAdapter implements ActionListener, MouseListener
	{
		public void keyReleased(KeyEvent e)
		{
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q) {
				exit();
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
				if (!message.getText().equals("")) {
					message.setText("");
				}
				try {
					ec.encrypt();
					if (checkDeleteFile.isSelected()) {
						message.setText("Espere un momento mientras se destruyen los archivos (Esto " +
						"puede tardar mucho dependiendo del tama�o de los archivos).");
						ec.destroyFiles(ec.getOriginalFile());
						ec.destroyFiles(ec.getOriginalFileZip());
						message.setText("¡Archivos destruidos!");
					} 
				} catch (CancellationException ex) {
					JOptionPane.showMessageDialog(new PrincipalSheet(), ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
				}
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
				decrypt();
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I) {
				showInfo();
			}
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource().equals(items.get(0))) {
				exit();
			} else if (e.getSource().equals(items.get(1))) {
				encrypt();
			} else if (e.getSource().equals(items.get(3))) {
				decrypt();
			} else if (e.getSource().equals(items.get(9))) {
				showHelp();
			} else if (e.getSource().equals(election)) {
				String selection = combo.getSelectedItem().toString();
				if (selection.equals(crypt)) {
					encrypt();
				} else if (selection.equals(decrypt)) {
					decrypt();
				} else if (selection.equals("Destruir archivo(s)")) {
					destroyFile();
				} else if (selection.equalsIgnoreCase("Verificar firma hash sha256")) {
					verifySums("SHA-256");
				} else if (selection.equalsIgnoreCase("Verificar firma hash sha512")) {
					verifySums("SHA-512");
				}
			} else if (e.getSource().equals(items.get(6))) {
				destroyFile();
			} else if (e.getSource().equals(items.get(7))) {
				verifySums("SHA-256");
			} else if (e.getSource().equals(items.get(8))) {
				verifySums("SHA-512");
			} else {
				showInfo();
			}
		}

		public void mouseClicked(MouseEvent e)
		{
			if (!message.getText().equals("")) {
				message.setText("");
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	private void exit()
	{
		final String message = "¿Está seguro que desea salir?";
		final String title = "Salida";
		int ask = JOptionPane.showConfirmDialog(this, message, title,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (ask == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}
	
	private void encrypt()
	{
		if (!message.getText().equals("")) {
			message.setText("");
		}
		try {
			ec.openFile("Abrir archivo para comprimir en ZIP", null, JFileChooser.FILES_AND_DIRECTORIES);
			message.setText(messageCompress);
			ec.compress(ec.getOriginalFile());
			message.setText(messageCipher);
			ec.encrypt();
			if (checkDeleteFile.isSelected()) {
				message.setText(messageDestroy);
				ec.destroyFiles(ec.getOriginalFile());
				ec.destroyFiles(ec.getOriginalFileZip());
				message.setText("¡Archivo destruido!");
			}
		} catch (CancellationException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ZipException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void decrypt()
	{
		if (!message.getText().equals("")) {
			message.setText("");
		}
		try {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo cifrado de BrookieCrypt", "enc");
			message.setText(messageHash);
			ec.openFile("Abrir archivo para descifrar (.enc)", filter, JFileChooser.FILES_ONLY);
			ec.verifyHash("SHA-256", false);
			ec.verifyHash("SHA-512", false);
			message.setText(messageDecipher);
			ec.decrypt();
			message.setText(messageDescompress);
			ec.descompress();
			if (checkDeleteFile.isSelected()) {
				message.setText(messageDestroy);
				ec.destroyFiles(ec.getOriginalFileZip());
				message.setText("�Archivo destruido!");
			}
			JOptionPane.showMessageDialog(this, "Descifrado finalizado.", "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (CancellationException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ZipException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (DigestException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (GeneralSecurityException ex) {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void destroyFile()
	{
		try {
			if (!message.getText().equals("")) {
				message.setText("");
			}
			ec.openFile("Abrir archivo para destruirlo", null, JFileChooser.FILES_ONLY);
			message.setText(messageDestroy);
			ec.destroyFiles(ec.getOriginalFile());
			message.setText("¡Archivo(s) destruido(s)!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (CancellationException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void verifySums(@NonNull String algorithm)
	{
		try {
			ec.openFile("Abrir archivo firmado", null, JFileChooser.FILES_ONLY);
			message.setText(messageHash);
			ec.verifyHash(algorithm, true);
			message.setText("¡Firmas hash verificadas!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (CancellationException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (DigestException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void showHelp()
	{
		final String TITLE = "Ayuda de BrookieCrypt";
		final String GITHUB = "https://github.com/brookiestein/BrookieCrypt";
		final String GITLAB = "https://gitlab.com/LordBrookie/BrookieCrypt";
		final String MESSAGE = "Cómo utilizar BrookieCrypt está detallado en un archivo: \"DOCUMENTATION\"\n" +
		"que debería de habérsele suministrado al momento de la obtención de este software.\n" +
		"Si ese no fue el caso, también puede leer dicho documento, entre otras cosas en las siguientes páginas:\n" +
		GITHUB + "\n" + GITLAB;
		JOptionPane.showMessageDialog(this, MESSAGE, TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

	private void showInfo()
	{
		final String title = "Información - " + name;
		final String message = String.format("Nombre de software: %s\n\nVersión: %s\n\nLicencia: %s\n\nAutor: %s\n\n",
		name, version, license, author);
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
