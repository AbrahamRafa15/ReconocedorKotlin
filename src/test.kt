val x: Int = 10
val cadena: String = "Hola mundo"
var contador: Int? = null
var area: Float = 10.0f
var volumen: Float? = null
val cadena2: String = "Para el proyecto"
val arrConst: IntArray = IntArray(3)
var floatArr: FloatArray = FloatArray(10) {1.1f}
var sArr = arrayOf<String>("Hola", "es", "arreglo")

// Enteros
val y: Long = 123456789L
var z: Short = 12
val w: Byte = 127

// Flotantes
val temperatura: Double = 36.6
var radio: Double = 7.5

//Char
var caracter: Char = 'A'

// Booleanos
val esVerdadero: Boolean = true
var esFalso = false

// Arrays con diferentes tipos
var dArr = arrayOf<Double>(1.1, 2.2, 3.3)
val boolArr = booleanArrayOf(true, false, true)

// Listas
val listaEnteros: List<Int> = listOf(1, 2, 3, 4)
var listaStrings = listOf("A", "B", "C")

// Mapas
val mapaEjemplo: Map<String, Int> = mapOf("Uno" to 1, "Dos" to 2)
var mapaGenerico = mutableMapOf("Key1" to 100, "Key2" to 200)

// Casos con tipos genéricos
val conjuntoNumeros: Set<Double> = setOf(1.1, 2.2, 3.3)
var conjuntoCadenas = setOf("Uno", "Dos", "Tres")

// Variables sin inicializar (no deben contarse como inicializadas)
var sinValor: Int? = null
val otraSinValor: String? = null