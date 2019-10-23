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
 * Cr�ditos a Mart�n
*/
package utils;

import java.security.Key;

// Factory interface for generating keys.
public interface KeyFactory
{
	// AES KeyFactory implementation
    KeyFactory AES = new AESKeyFactory();
	
    // AES KeyFactory implementation
    KeyFactory DES = new DESKeyFactory();

	// Derives and returns a strong key from a password.
	Key keyFromPassword(char[] password);

	// Generates a strong random key.
	Key randomKey();

	// Generates a random key of size size.
	Key randomKey(int size);
}
