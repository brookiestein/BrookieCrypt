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

// Paquetes SWING
import javax.swing.JFrame;

// Paquetes AWT
import java.awt.Toolkit;
import java.awt.Image;

public class Main
{
	private static final int x = 240;
	private static final int y = 150;
	private static final int width = 886;
	private static final int height = 473;
	private static final String version = "0.0.2";
	public static void main(String[] args)
	{
		PrincipalSheet s = new PrincipalSheet();
		JFrame f = new JFrame("BrookieCrypt - " + version);
		Image icon = Toolkit.getDefaultToolkit().getImage("images/icon.jpg");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setIconImage(icon);
		f.setBounds(x, y, width, height);
		f.add(s);
		f.setResizable(false);
		f.setVisible(true);
	}
}
