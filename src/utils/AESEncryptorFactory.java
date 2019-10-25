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

// Factory class for constructing AES Encryptor instances.
public class AESEncryptorFactory implements EncryptorFactory {

	@Override
	public final Encryptor messageEncryptor(Key key)
	{
		return new Encryptor(key, "AES/CBC/PKCS5Padding", 16);
	}

	@Override
	public final Encryptor streamEncryptor(Key key)
	{
		return new Encryptor(key, "AES/CTR/NoPadding", 16);
	}
}
