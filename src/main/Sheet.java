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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class PrincipalSheet extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final String name = "BrookieCrypt";
	private static final String version = "0.0.1";
	private static final String license = "GNU GPL v2";
	private static final String author = "Lord Brookie";

	private EventManager listener;
	private JButton bExit, bCrypt, bDCrypt;
	private JLabel text;
	private JMenuBar bar;
	private JMenu mFile, mTools, mHelp;
	private JMenuItem iExit, iCrypt, iDCrypt, iAbout;
	private JPanel s;
	private Encrypt ec;
	
	public PrincipalSheet()
	{		
		listener = new EventManager();
		setLayout(new BorderLayout());
		setFocusable(true);
		addKeyListener(listener);
		
		bExit = new JButton("Salir");
		bExit.addActionListener(listener);
		bExit.addKeyListener(listener);
		bExit.setBounds(400, 195, 60, 25);
		
		bCrypt = new JButton("Cifrar un archivo o directorio");
		bCrypt.addActionListener(listener);
		bCrypt.addKeyListener(listener);
		bCrypt.setBounds(230, 160, 200, 25);
		
		bDCrypt = new JButton("Descifrar un archivo");
		bDCrypt.addActionListener(listener);
		bDCrypt.addKeyListener(listener);
		bDCrypt.setBounds(440, 160, 150, 25);
		
		text = new JLabel("¿Qué desea hacer?");
		text.setBounds(370, 130, 150, 20);
		
		iExit = new JMenuItem("Salir (CTRL + Q)");
		iExit.addActionListener(listener);
		iExit.addKeyListener(listener);
		
		iCrypt = new JMenuItem("Cifrar un archivo o directorio (CTRL + E)");
		iCrypt.addActionListener(listener);
		iCrypt.addKeyListener(listener);
		
		iDCrypt = new JMenuItem("Descifrar un archivo (CTRL + D)");
		iDCrypt.addActionListener(listener);
		iDCrypt.addKeyListener(listener);
		
		iAbout = new JMenuItem("Acerca de (CTRL + I)");
		iAbout.addActionListener(listener);
		iAbout.addKeyListener(listener);
		
		mFile = new JMenu("Archivo");
		mFile.add(iExit);
		
		mTools = new JMenu("Herramientas");
		mTools.add(iCrypt);
		mTools.add(iDCrypt);
		
		mHelp = new JMenu("Ayuda");
		mHelp.add(iAbout);
		
		bar = new JMenuBar();
		bar.add(mFile);
		bar.add(mTools);
		bar.add(mHelp);
		
		ec = new Encrypt();

		s = new JPanel(null);
		s.add(text);
		s.add(bCrypt);
		s.add(bDCrypt);
		s.add(bExit);
		s.setFocusable(true);
		s.addKeyListener(listener);
		
		add(bar, BorderLayout.NORTH);
		add(s, BorderLayout.CENTER);
	}
	
	private final class EventManager extends KeyAdapter implements ActionListener
	{
		public void keyReleased(KeyEvent e)
		{
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q) {
				exit();
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
				ec.encrypt();
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
				ec.decrypt();
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I) {
				showInfo();
			}
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource().equals(iExit) || e.getSource().equals(bExit)) {
				exit();
			} else if (e.getSource().equals(iCrypt) || e.getSource().equals(bCrypt)) {
				ec.encrypt();
			} else if (e.getSource().equals(iDCrypt) || e.getSource().equals(bDCrypt)) {
				ec.decrypt();
			} else {
				showInfo();
			}
		}
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
	
	private void showInfo()
	{
		final String title = "Información - " + name;
		final String message = String.format("Nombre de software: %s\n\nVersión: %s\n\nLicencia: %s\n\nAutor: %s\n\n",
		name, version, license, author);
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
