package uvg.edu.gt;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Busqueda {
    /**
     * Busca en el archivo anime.csv la fila cuyo título (tercera columna)
     * coincida (ignora mayúsculas/minúsculas) con el parámetro.
     * @param titulo El nombre del anime a buscar.
     * @return Optional con un Map<columna,valor> si lo encuentra, o empty() si no.
     */
    public static Optional<Map<String,String>> buscarPorTitulo(String titulo) {
        String ruta = "anime.csv";  // asegúrate de que esté en el directorio de ejecución
        try (BufferedReader br = Files.newBufferedReader(Paths.get(ruta))) {
            String headerLine = br.readLine();
            if (headerLine == null) return Optional.empty();
            String[] headers = headerLine.split("\t");
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] cols = linea.split("\t", -1);
                if (cols.length > 2 && cols[2].equalsIgnoreCase(titulo)) {
                    Map<String,String> info = new LinkedHashMap<>();
                    for (int i = 0; i < headers.length && i < cols.length; i++) {
                        info.put(headers[i], cols[i]);
                    }
                    return Optional.of(info);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
