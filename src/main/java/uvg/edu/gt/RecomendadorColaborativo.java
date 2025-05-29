// Proyecto 2 Grupo 6
package uvg.edu.gt;

import java.util.Map;
import org.neo4j.driver.Record;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendadorColaborativo {
    private float umbralSimilitud;
    private BaseDeDatos baseDeDatos;

    // Constructor principal con conexión a Neo4j
    public RecomendadorColaborativo(float umbralSimilitud, BaseDeDatos baseDeDatos) {
        this.umbralSimilitud = umbralSimilitud;
        this.baseDeDatos = baseDeDatos;
    }

    // Constructor alternativo para pruebas unitarias sin base de datos
    public RecomendadorColaborativo(float umbralSimilitud) {
        this.umbralSimilitud = umbralSimilitud;
        this.baseDeDatos = null;
    }

    // Método para Neo4j usando IDs de usuarios
    public float calcularSimilaridadUsuarios(String userId1, String userId2) {
        if (baseDeDatos == null) return 0f;

        List<Record> ratings1 = baseDeDatos.getRatingsForUser(userId1);
        List<Record> ratings2 = baseDeDatos.getRatingsForUser(userId2);

        var animeRatings1 = ratings1.stream().collect(Collectors.toMap(
                r -> r.get("animeId").asString(),
                r -> r.get("rating").asInt()));
        var animeRatings2 = ratings2.stream().collect(Collectors.toMap(
                r -> r.get("animeId").asString(),
                r -> r.get("rating").asInt()));

        var comunes = new java.util.HashSet<>(animeRatings1.keySet());
        comunes.retainAll(animeRatings2.keySet());

        if (comunes.isEmpty()) return 0f;

        float avg1 = (float) animeRatings1.entrySet().stream()
                .filter(e -> comunes.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .average().orElse(0f);
        float avg2 = (float) animeRatings2.entrySet().stream()
                .filter(e -> comunes.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .average().orElse(0f);

        float num = 0f, den1 = 0f, den2 = 0f;
        for (String animeId : comunes) {
            float d1 = animeRatings1.get(animeId) - avg1;
            float d2 = animeRatings2.get(animeId) - avg2;
            num += d1 * d2;
            den1 += d1 * d1;
            den2 += d2 * d2;
        }

        if (den1 == 0f || den2 == 0f) return 0f;
        return num / ((float) (Math.sqrt(den1) * Math.sqrt(den2)));
    }

    // Método alternativo para pruebas unitarias con objetos Usuario
    public float calcularSimilaridadUsuarios(Usuario u1, Usuario u2) {
        Map<Anime, Integer> c1 = u1.getCalificaciones();
        Map<Anime, Integer> c2 = u2.getCalificaciones();
        float suma = 0f;
        int coincidencias = 0;

        for (Anime a : c1.keySet()) {
            if (c2.containsKey(a)) {
                int r1 = c1.get(a);
                int r2 = c2.get(a);
                suma += 1 - (Math.abs(r1 - r2) / 5.0f);
                coincidencias++;
            }
        }

        return coincidencias == 0 ? 0f : suma / coincidencias;
    }
}
