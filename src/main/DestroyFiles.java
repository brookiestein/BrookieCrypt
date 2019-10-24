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
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

// Paquetes Annotation
import org.eclipse.jdt.annotation.NonNull;

public class DestroyFiles
{
	public static void destroy(@NonNull File file) throws IOException
	{
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < (int) file.length(); i++) {
			bw.write(0);
		}
		bw.flush();
		bw.close();
		file.delete();
	}
}
