/**
 * KotlinVariableAnalyzer es una utilidad para analizar archivos con código fuente de Kotlin.
 * El programa reconoce y clasifica las declaraciones de variables utilizando expresiones regulares.
 * Proporciona estadísticas detalladas sobre las variables encontradas en el archivo analizado.
 *
 * @author
 * @version 1.0
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class KotlinVariableAnalyzer {

    /**
     * Expresión regular para reconocer declaraciones de variables Kotlin.
     * Captura información sobre si es constante (val) o variable mutable (var),
     * el nombre de la variable, su tipo (opcional) y el valor asignado (opcional).
     */
    private static final String VAR_PATTERN = """
        (?<const>val|var)\\s+                       # val o var
        (?<name>[a-zA-Z_]\\w*)\\s*                  # nombre variable
        (?::\\s*(?<type>[^=;]+))?                   # tipo opcional (captura todo hasta '=' o ';')
        \\s*(?:=\\s*(?<value>[^;]+))?               # valor opcional
    """.stripIndent();

    // Objeto Pattern compilado para mejorar rendimiento en la ejecución
    private static final Pattern pattern = Pattern.compile(VAR_PATTERN, Pattern.COMMENTS);

    /**
     * Método principal que inicia la ejecución del programa.
     * Recibe opcionalmente la ruta al archivo Kotlin desde línea de comandos.
     * Si no se recibe ninguna ruta, se abre un diálogo gráfico.
     *
     * @param args Argumentos de línea de comandos (ruta opcional al archivo)
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static void main(String[] args) throws IOException {
        String filePath = args.length > 0 ? args[0] : openFileDialog();
        if (filePath == null) {
            System.out.println("No se ha encontrado el archivo");
            return;
        }

        // Lectura de todas las líneas del archivo seleccionado
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        analyzeVariables(lines);
    }

    /**
     * Abre un diálogo gráfico para seleccionar un archivo Kotlin.
     * Se usa JFileChooser para una selección amigable del archivo.
     *
     * @return Ruta absoluta al archivo seleccionado o null si no se selecciona ninguno
     */
    private static String openFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo de código Kotlin");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Analiza las líneas del archivo Kotlin para identificar y clasificar variables.
     * Utiliza varias estructuras Map para llevar un conteo claro y ordenado.
     * Imprime en pantalla estadísticas detalladas de las variables.
     *
     * @param lines Líneas del archivo Kotlin a analizar
     */
    private static void analyzeVariables(List<String> lines) {
        // Almacena la cantidad de variables por tipo encontrado
        Map<String, Integer> typeCount = new HashMap<>();
        // Clasifica los nombres de variables según su tipo
        Map<String, List<String>> typeToNames = new HashMap<>();

        // Variables para estadísticas generales
        int totalVars = 0;
        int initializedVars = 0;
        int arrayVars = 0;
        int constantVars = 0;

        for (String line : lines) {
            // Se eliminan comentarios de línea para evitar falsos positivos en el análisis
            line = line.replaceAll("//.*", "");
            Matcher matcher = pattern.matcher(line);

            // Iterar sobre todas las coincidencias encontradas por línea
            while (matcher.find()) {
                totalVars++;

                String varName = matcher.group("name");
                String originalType = matcher.group("type");

                // Determina el tipo declarado explícitamente o intenta inferirlo según el valor:
                String varType = (originalType != null)
                        ? originalType.trim()
                        : inferType(matcher.group("value"));

                // Detecta si la variable es constante ("val") o mutable ("var")
                boolean isConst = matcher.group("const").equals("val");
                boolean isInitialized = matcher.group("value") != null;

                // Tratamiento especial para variables tipo Map con clave y valor específico:
                if (varType.startsWith("Map<")) {
                    Matcher mapMatcher = Pattern.compile("Map\\s*<\\s*([^,]+)\\s*,\\s*([^>]+)\\s*>").matcher(varType);
                    if (mapMatcher.find()) {
                        String keyType = mapMatcher.group(1).trim();
                        String valueType = mapMatcher.group(2).trim();
                        varType = "Map<" + keyType + ", " + valueType + ">";
                    }
                } else if (varType.matches("\\w+<[^>]+>")) {
                    // Normaliza tipos genéricos distintos de Map para simplificar el conteo:
                    varType = varType.replaceAll("<[^>]+>", "<T>");
                }

                // Detecta si la variable es de tipo arreglo
                boolean isArray = varType.contains("Array") || varType.startsWith("Array<");

                // Actualiza mapas de conteo y clasificación
                typeCount.put(varType, typeCount.getOrDefault(varType, 0) + 1);
                typeToNames.computeIfAbsent(varType, k -> new ArrayList<>()).add(varName);

                if (isConst) constantVars++;
                if (isInitialized) initializedVars++;
                if (isArray) arrayVars++;
            }
        }

        // Imprime resultados claros y ordenados del análisis:
        System.out.println("Numero total de variables declaradas: " + totalVars);
        System.out.println("Numero total de tipos utilizados: " + typeCount.size());
        System.out.println("Numero total de variables declaradas por tipo:");
        typeCount.forEach((k, v) -> System.out.println("  - " + k + ": " + v));
        System.out.println("Numero total de variables inicializadas: " + initializedVars);
        System.out.println("Numero total de variables de tipo arreglo: " + arrayVars);
        System.out.println("Numero total de declaraciones constantes: " + constantVars);
        System.out.println("Clasificación de nombres de variables por tipo:");
        typeToNames.forEach((k, v) -> System.out.println("  - " + k + ": " + String.join(", ", v)));
    }

    /**
     * Infieren el tipo de una variable según su valor asignado, usando patrones regex.
     *
     * @param value Valor asignado a la variable
     * @return Tipo inferido como cadena, "Unknown" si no se reconoce
     */
    private static String inferType(String value) {
        if (value == null) return "Unknown";
        if (value.matches("\".*\"")) return "String";
        if (value.matches("\\d+L")) return "Long";
        if (value.matches("\\d+")) return "Int";
        if (value.matches("\\d+\\.\\d+f")) return "Float";
        if (value.matches("\\d+\\.\\d+")) return "Double";
        if (value.matches("'.'")) return "Char";
        if (value.matches("(true|false)")) return "Boolean";
        if (value.matches("booleanArrayOf.*")) return "BooleanArray";
        if (value.matches(".*arrayOf<([^>]+)>.*")) {
            Matcher m = Pattern.compile("arrayOf<([^>]+)>").matcher(value);
            if (m.find()) return "Array<" + m.group(1).trim() + ">";
            return "Array<T>";
        }
        if (value.matches("listOf.*")) return "List<T>";
        if (value.matches("setOf.*")) return "Set<T>";
        if (value.matches("mapOf.*")) return "Map<T, T>";
        return "Unknown";
    }
}

