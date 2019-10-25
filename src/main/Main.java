/*
 * Copyright (C) 19aa Lord Brookie
 * Este programa es software libre. Puede redistribuirlo y/o
 * modificarlo bajo los términos de la Licencia P�blica General
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
	private static final String version = "0.0.4";
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
