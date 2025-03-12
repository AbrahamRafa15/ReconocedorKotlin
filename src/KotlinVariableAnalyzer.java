import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class KotlinVariableAnalyzer {
    private static final String VAR_PATTERN = """
        (?<const>val|var)\\s+  # Tipo de declaración (val o var)
        (?<name>\\w+)\\s*       # Nombre de la variable
        (?::\\s*(?<type>[\\w<>]+))?  # Tipo opcional (ej. Int, Float, String, etc.)
        \\s*(?:=\\s*(?<value>.+))?  # Valor opcional después del =
    """.stripIndent();

    private static final Pattern pattern = Pattern.compile(VAR_PATTERN, Pattern.COMMENTS);

    public static void main(String[] args) throws IOException {
        String filePath = args.length > 0 ? args[0] : openFileDialog();
        if (filePath == null){
            System.out.println("No se ha encontrado el archivo");
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        analyzeVariables(lines);
    }

    private static String openFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo de código Kotlin");

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private static void analyzeVariables(List<String> lines) {
        Map<String, Integer> typeCount = new HashMap<>();
        Map<String, List<String>> typeToNames = new HashMap<>();
        int totalVars = 0, initializedVars = 0, arrayVars = 0, constantVars = 0;

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                totalVars++;

                String varType = Optional.ofNullable(matcher.group("type")).orElse("Unknown");
                String varName = matcher.group("name");
                boolean isConst = matcher.group("const").equals("val");
                boolean isInitialized = matcher.group("value") != null;
                boolean isArray = varType.matches(".*Array.*|arrayOf<.*>");

                typeCount.put(varType, typeCount.getOrDefault(varType, 0) + 1);
                typeToNames.computeIfAbsent(varType, k -> new ArrayList<>()).add(varName);

                if (isConst) constantVars++;
                if (isInitialized) initializedVars++;
                if (isArray) arrayVars++;
            }
        }

        System.out.println("Numero total de variables declaradas: " + totalVars);
        System.out.println("Numero total de tipos utilizados: " + typeCount.size());
        System.out.println("Numero total de variables declaradas por tipo:");
        typeCount.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("Numero total de variables inicializadas: " + initializedVars);
        System.out.println("Numero total de variables de tipo arreglo: " + arrayVars);
        System.out.println("Numero total de declaraciones constantes: " + constantVars);
        System.out.println("Clasificación de nombres de variables por tipo:");
        typeToNames.forEach((k, v) -> System.out.println(k + ": " + String.join(", ", v)));
    }
}