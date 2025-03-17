# **Kotlin Variable Analyzer**

## **Descripción**
Este programa de Java analiza un archivo que contiene **declaraciones de variables en Kotlin** y devuelve estadísticas detalladas sobre ellas.

Utilizando **expresiones regulares**, el programa reconoce y muestra la siguiente información sobre el archivo `.kt` que el usuario proporciona:

- 📌 **Cantidad total de variables declaradas.**  
- 📌 **Número de tipos de variables diferentes.**  
- 📌 **Cantidad de variables por tipo de dato.**  
- 📌 **Cantidad de variables inicializadas.**  
- 📌 **Lista de variables clasificadas por tipo.**  

---

## **¿Cómo se utiliza?**
### **1️⃣ Requisitos previos**
Este programa fue desarrollado y probado con **Java 23.0.1**.  
Si aún no tienes instalada esta versión, puedes descargarla aquí:  
🔗 [Descargar JDK 23.0.1](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)  

⚠ **Nota:** También puede ejecutarse en otras versiones recientes de Java, pero se recomienda esta versión para evitar problemas de compatibilidad.

---

### **2️⃣ Ejecución del programa**
#### **📂 Método 1: Usando el explorador de archivos**
1. Descarga y descomprime el archivo fuente `KotlinVariableAnalyzer.java` en una carpeta de tu computadora.
2. Abre una terminal y navega hasta la carpeta donde está ubicado el archivo.
3. Compila el programa con:
   ```sh
   javac KotlinVariableAnalyzer.java
   ```
4. Ejecuta el programa con:
   ```sh
   java KotlinVariableAnalyzer
   ```
5. Se abrirá una ventana donde podrás **seleccionar un archivo `.kt`**.
6. El programa analizará el archivo y mostrará **las estadísticas en la terminal**.

---

#### **📝 Método 2: Especificando el archivo desde la terminal**
Si prefieres indicar el archivo directamente sin usar el explorador de archivos, puedes hacerlo con:

```sh
java KotlinVariableAnalyzer ruta/al/archivo.kt
```

Ejemplo:
```sh
java KotlinVariableAnalyzer ejemplos/codigo.kt
```

Esto **saltará la ventana emergente** y analizará directamente el archivo proporcionado.

---

## **📌 Limitaciones y Consideraciones**
1️⃣ **El programa asume que el archivo tiene una sintaxis válida.**  
   - No notificará errores de sintaxis si existen en el código Kotlin.  

2️⃣ **No detecta múltiples declaraciones en una sola línea sin separadores.**  
   - Kotlin permite la declaración de varias variables en la misma línea si están separadas por `;`.  
   - Si no se incluye `;`, **el analizador solo detectará la primera variable** y omitirá el resto.  
   - **Ejemplo de problema:**  
     ```kotlin
     val a: Int = 5 val b: String = "Hola"  // ❌ Falta el ';', el programa solo detectará 'a'
     ```

3️⃣ **El programa no realiza inferencia avanzada de tipos.**  
   - Variables declaradas sin un tipo explícito y con valores complejos (`mutableMapOf(...)`) podrían clasificarse como **Unknown**.  
   - **Ejemplo:**
     ```kotlin
     var mapa = mutableMapOf("clave1" to 10)  // 🔍 Se marcará como 'Unknown'
     ```
   - Para evitar esto, **se recomienda declarar los tipos explícitamente**:
     ```kotlin
     var mapa: MutableMap<String, Int> = mutableMapOf("clave1" to 10)  // ✅ Se detectará correctamente
     ```

---

## **📜 Ejemplo de salida**
Si el archivo `codigo.kt` contiene lo siguiente:

```kotlin
val x: Int = 10
var mensaje: String = "Hola"
val lista: List<Double> = listOf(1.2, 3.4, 5.6)
var arreglo = arrayOf("Uno", "Dos", "Tres")
```

El programa mostrará en la terminal:

```
Numero total de variables declaradas: 4
Numero total de tipos utilizados: 3
Numero total de variables declaradas por tipo:
  - Int: 1
  - String: 1
  - List<T>: 1
  - Array<T>: 1
Numero total de variables inicializadas: 4
Numero total de variables de tipo arreglo: 1
Numero total de declaraciones constantes: 2
Clasificación de nombres de variables por tipo:
  - Int: x
  - String: mensaje
  - List<T>: lista
  - Array<T>: arreglo
```

---

