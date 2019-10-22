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

// Paquetes UTIL
import java.util.ArrayList;

// Paquetes AWT
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Paquetes SWING
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
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
	private JLabel text;
	private JMenuBar bar;
	private ArrayList<JMenu> menu;
	private ArrayList<JMenuItem> items;
	private JComboBox<String> combo;
	private JPanel s;
	private Encrypt ec;
	private JButton enter;
	private final String crypt = "Cifrar un archivo o directorio";
	private final String decrypt = "Descifrar un archivo";
	
	public PrincipalSheet()
	{		
		listener = new EventManager();
		setLayout(new BorderLayout());
		setFocusable(true);
		addKeyListener(listener);
		
		text = new JLabel("¿Qué desea hacer?");
		text.setBounds(370, 130, 150, 20);
		
		items = new ArrayList<JMenuItem>();
		addItem("Salir (CTRL + Q)");
		addItem("Cifrar un archivo o directorio (CTRL + E)");
		addItem(crypt);
		addItem("Descifrar un archivo (CTRL + D)");
		addItem(decrypt);
		addItem("Acerca de (CTRL + I)");
		
		combo = new JComboBox<String>();
		combo.addItem("Nada");
		combo.addItem(items.get(2).getText());
		combo.addItem(items.get(4).getText());
		combo.setBounds(280, 160, 200, 25);
		combo.addKeyListener(listener);
		
		enter = new JButton("Entrar");
		enter.setBounds(490, 160, 70, 25);
		enter.addActionListener(listener);
		enter.addKeyListener(listener);
		
		menu = new ArrayList<JMenu>();
		menu.add(new JMenu("Archivo"));
		menu.add(new JMenu("Herramientas"));
		menu.add(new JMenu("Ayuda"));
		
		menu.get(0).add(items.get(0));
		menu.get(1).add(items.get(1));
		menu.get(1).add(items.get(3));
		menu.get(2).add(items.get(5));
		
		bar = new JMenuBar();
		bar.add(menu.get(0));
		bar.add(menu.get(1));
		bar.add(menu.get(2));
		
		ec = new Encrypt();

		s = new JPanel(null);
		s.add(text);
		s.add(combo);
		s.add(enter);
		s.setFocusable(true);
		s.addKeyListener(listener);
		
		add(bar, BorderLayout.NORTH);
		add(s, BorderLayout.CENTER);
	}
	
	private void addItem(String name)
	{
		items.add(new JMenuItem(name));
		items.get(items.size() - 1).addActionListener(listener);
		items.get(items.size() - 1).addKeyListener(listener);
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
			if (e.getSource().equals(items.get(0))) {
				exit();
			} else if (e.getSource().equals(items.get(1))) {
				ec.encrypt();
			} else if (e.getSource().equals(items.get(3))) {
				ec.decrypt();
			} else if (e.getSource().equals(enter)) {
				if (combo.getSelectedItem().toString().equals(crypt)) {
					ec.encrypt();
				} else {
					ec.decrypt();
				}
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
