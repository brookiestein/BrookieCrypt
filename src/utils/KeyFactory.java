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
 * Créditos a Martín
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
