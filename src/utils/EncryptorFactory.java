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

//Factory interface for constructing Encryptor instances.
public interface EncryptorFactory
{
	// AES EncryptorFactory implementation
    EncryptorFactory AES = new AESEncryptorFactory();
	
	// DES EncryptorFactory implementation
    EncryptorFactory DES = new DESEncryptorFactory();

	// Returns an Encryptor instance usable for message encryption.
	Encryptor messageEncryptor(Key key);
	
	// Returns an Encryptor instance usable for stream encryption.
	Encryptor streamEncryptor(Key key);
}
