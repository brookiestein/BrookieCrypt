**¡Bienvenido a la documentación de BrookieCrypt!**

**Conceptos utilizados en este documento:**
```
Archivo = Tanto archivo como carpeta, esto se aplica para todo lugar en el que se encuentra esta palabra.
Archivo intermedio = Archivo cifrado y comprimido ZIP.
Primera capa de protección = Cifrado y compresión ZIP.
Segunda capa de protección = Cifrado AES de 256 bits.
Proceso de obtención de firma hash = Realización de suma de comprobación u obtención de la
firma digital de un determinado archivo.
Cifrar = Proteger. Convertir un archivo entendible por humanos, en un conjunto de bits incomprensibles
para los mismos.
```

**El uso de BrookieCrypt es relativamente sencillo:**

Se tiene una interfáz sencilla (lo más sencilla posible).
En el centro hay un menú desplegable en el cual puede seleccionar la opción que desee llevar a cabo:
```
1- Nada
2- Cifrar un archivo
3- Descifrar un archivo
4- Destruir un archivo
5- Verificar firma hash SHA-256
6- Verificar firma hash SHA-512
```

Más abajo, se encuentra un elemento que puede estar habilitado o deshabilitado. Este sirve para cuándo se
cifrará un archivo o cuándo se descifrará un archivo, destruir los archivos intermedios, estos son:
```
Archivo intermedio ZIP y archivo original a cifrar para la opción de cifrado.
Archivo intermedio ZIP para el descifrado.
```

**¿Por qué le podría interesar marcar como habilitada esta opción?**

«El punto o sentido» de BrookieCrypt es proteger archivos, por lo que si desea hacer esto (y si lo piensa un momento)
no le interesará que después de proteger un archivo, el original siga por ahí, sin proteger.
Ergo; esta opción está habilitada por defecto, pero siempre puede deshabilitarla libremente si así lo desea.

Bien, una vez ha entendido esto... Casi todas las opciones funcionan de la misma manera, salvo algún matíz diferenciador:

**Si desea cifrar archivo:**

Tiene varias maneras de realizar esto:
```
1- Desde el menú desplegable ubicado en el centro del software
2- Desde la barra de herramientas, o;
3- Con un atajo de teclado, véase: Algunos atajos de teclado que puede utilizar, más abajo.
```

Independientemente de cómo lo ha hecho, se abrirá una ventana en la cual podrá elegir el archivo que desea cifrar.

**¿Cómo BrookieCrypt realiza este proceso?**

Una vez ha elegido el archivo, **BrookieCrypt** le pedirá la contraseña que desea utilizar para la **primera capa de
protección**. Una vez obtenida, cifrará y comprimirá el archivo. Luego, le pedirá la contraseña que se utilizará 
para **la segunda capa de protección.** Aquí debe notar algo: Para que el concepto de protección con dos capas tenga
sentido, se recomienda encarecidamente que utilice dos contraseñas distintas para ambas capas de protección. De todos
modos **BrookieCrypt NO** verifica si ambas si iguales o no, así que esto queda a su entera disposición. Una vez le haya
ofrecido la contraseña de **la segunda capa de protección a BrookieCrypt** este procederá a cifrar **el archivo intermedio**.
Una vez haya finalizado, le mostrará un mensaje avisándole  a través de un cuadro de diálogo.
Si la opción para la destrucción de archivos está habilitada, se procederá a realizar dicho proceso.
Por último, tomará el archivo de **la segunda capa de protección** y le realizará un proceso de obtención de una suma de
comprobación utilizando los algoritmos: [SHA-256 y SHA-512](https://es.wikipedia.org/wiki/SHA-2)
y los almacenará en la ruta dónde se encuentra el archivo cifrado con el mismo nombre, pero con extensiones:
```
.sha256sum para SHA-256, y;
.sha512sum para SHA-512
```
Es importante que recuerde esto, puesto que el descifrado de un archivo, es básicamente el proceso inverso a este, es decir:

**Si desea descifrar un archivo:**

Igualmente que lo anteriormente dicho: Tiene las mismas opciones de realizar esto, véase:

**Tiene varias maneras de realizar esto** más arriba.

Una vez a elegido el archivo que desea descifrar (**BrookieCrypt** utiliza extensión .enc) se tomará en cuenta que los 
archivos en los cuáles se almacenaron las firmas hash **SHA-256** y **SHA-512** tienen el mismo nombre que el archivo a 
descifrar. Se leerán estos archivos para almacenar las firmas en búfer, para posteriormente realizar un proceso de 
obtención de suma de comprobación al archivo cifrado. Y, de esta manera, verificar, antes de proceder al descifrado que 
la integridad del archivo no ha sido comprometida. Si **BrookieCrypt** determina que todo está bien, es decir, 
[que el archivo no ha sido comprometido](https://es.wikipedia.org/wiki/Funci%C3%B3n_hash)
se procede entonces al descifrado del mismo, pidiéndole así, la contraseña de **la segunda capa de protección**. 
En este paso, debe de asegurarse que la contraseña introducida es realmente la que estableció al momento del cifrado del
archivo, puesto que, **BrookieCrypt NO** le avisa si es incorrecta y le dará la ilusión de haber descifrado el archivo.
Sin embargo; esto no es así. Independientemente de si introdujo bien o mal la contraseña **BrookieCrypt**
le pedirá la contraseña de la primera capa de protección. Si la contraseña de **la segunda capa de protección** es incorrecta,
el error estará al momento de descifrar y/o descomprimir **la primera capa de protección.** Si todo ha ido bien, es decir,
si ambas contraseñas han sido correctas y se ha podido descifrar ambas capas de protección, se obtendrá el archivo originalmente
cifrado. Si la opción: **Destruir archivos** está habilitada, **BrookieCrypt** procederá a realizar la **destrucción del 
archivo** intermedio. Dejando así el archivo cifrado y el archivo originalmente cifrado.

Para las demás opciones:
```
Destruir un archivo
Verificar firma hash SHA-256
Verificar firma hash SHA-512
```
Su funcionamiento está explicado en las dos opciones anteriores. La diferencia es que:

**Si desea destruir un archivo:**

**BrookieCrypt** le mostrará una ventana en la que podrá elegir el archivo que desea destruir, este no está limitado
a si, por ejemplo, cifró algún archivo. Es decir, puede destruir **CUALQUIER** archivo.

**Si desea verificar firma hash SHA-256 y/o SHA-512:**

De igual manera **BrookieCrypt** le mostará una ventana en la que deberá elegir **SÓLO** un archivo firmado. **BrookieCrypt**
tomará en cuenta que la suma de comprobación SHA-256 o SHA-512 está en un archivo con el mismo nombre que el que ha elegido
como archivo firmado. Entonces, procederá a leer dicho archivo, almacenará su contenido en búfer, realizará un proceso de
obtención de firma hash SHA-256 o SHA-512 al archivo firmado y por último le avisará el resultado.

**Algunos atajos de teclado que puede utilizar:**

```
CTRL + Q = Salir del programa
CTRL + E = Cifrar un archivo
CTRL + D = Descifrar un archivo
CTRL + 1 = Destruir un archivo
CTRL + 2 = Verificar firma hash SHA-256
CTRL + 3 = Verificar firma hash SHA-512
CTRL + I = Obtener información sobre BrookieCrypt
F1 = Obtener ayuda de BrookieCrypt
```
