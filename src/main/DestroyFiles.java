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

// Paquetes IO
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

// Paquetes Util
import java.util.Random;

// Paquetes Annotation
import org.eclipse.jdt.annotation.NonNull;

public class DestroyFiles
{
	public static void destroy(@NonNull File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		long lenFile = file.length();
		byte bw[] = new byte[1];
		for (long i = 0; i < lenFile; i++) {
			for (long j = 0; j < lenFile; j++) {
				for (long k = 0; j < lenFile; k++) {
					bw[0] = (byte) getAleatoryNumber((int) ((int) Math.random() % (Math.random() % Integer.MAX_VALUE)));
					bos.write(bw, 0, (int) k);
				}
			}
		}
		bos.flush();
		bos.close();
		file.delete();
	}
	
	public static int getAleatoryNumber(int limit)
	{
		Random rnd = new Random(System.currentTimeMillis());
		int value = rnd.nextInt(limit);
		return value;
	}
}
