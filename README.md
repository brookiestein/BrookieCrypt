**BrookieCrypt** es un programa que sirve para comprimir, cifrar y crear sumas de comprobación de un archivo, todo esto en conjunto. Si desea saber cómo utilizarlo, léase el archivo 
[DOCUMENTATION](https://github.com/brookiestein/BrookieCrypt/blob/master/DOCUMENTATION.md) o bien, 
[mire este vídeo de aquí.](https://youtu.be/qTXNv065MLg)

[Este programa está basado en este de aquí.](https://github.com/brookiestein/scripts/tree/master/BrookieCrypt)
Utilizando ambos se obtiene el mismo resultado o se espera el mismo resultado. Utilizan el mismo esquema, pero
existen algunas diferencias (evidentemente) a la hora de realizar su trabajo.

También se toma en cuenta de que ambos están escritos en lenguajes de programación diferentes, ergo; uno puede 
realizar un trabajo mejor que el otro o no. Quizás por que el lenguaje sea mejor o quizás por que su(s) autor(es) 
al momento de escribirlo no tuvieron tanto conocimiento para realizarlo de la mejor forma.

Independientemente de cual sea el caso, se invita a utilizarlos a ambos y quedarse con el que más cómodo se sienta.
Quizás la diferencia «más grande» sea que uno es en consola, mientras que el otro tiene interfaz gráfica.

**Métodos de cifrado:**
```
Simétrico
```

**Algoritmos utilizados:**
```
ZIP
AES 256 bits
```

**Librerías utilizadas:**
```
Zip4j
Se utiliza código de Encrypt4j no la biblioteca completa. Dónde se utiliza
se le da créditos al autor en el texto de COPYRIGHT.
```

**Cómo ejecutar:**

1- Si está utilizando un sistema Microsoft Windows, descargue el archivo binario 
[«BrookieCrypt-Setup»](https://github.com/brookiestein/BrookieCrypt/blob/master/bin/BrookieCrypt-Setup.exe?raw=true)
e instálelo.

2- Si está utilizando un sistema basado en UNIX, por ejemplo: Mac OS o GNU/Linux, descargue el archivo binario 
[«BrookieCrypt.jar»](https://github.com/brookiestein/BrookieCrypt/blob/master/bin/BrookieCrypt.jar?raw=true), 
abra una sesión en una consola y ejecute las siguientes órdenes:
```
$ java -jar BrookieCrypt.jar
```
Nota: En un sistema Microsoft Windows también se puede utilizar este último método.
