package uvg.edu.gt;

import java.util.Map;
import org.neo4j.driver.Record;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

public class RecomendadorColaborativo {
    private float umbralSimilitud;
    private BaseDeDatos baseDeDatos;

    public RecomendadorColaborativo(float umbralSimilitud, BaseDeDatos baseDeDatos) {
        this.umbralSimilitud = umbralSimilitud;
        this.baseDeDatos = baseDeDatos;
    }

    public float calcularSimilaridadUsuarios(String userId1, String userId2) {
        List<Record> ratings1 = baseDeDatos.getRatingsForUser(userId1);
        List<Record> ratings2 = baseDeDatos.getRatingsForUser(userId2);

        // Extraer animes y ratings en común
        var animeRatings1 = ratings1.stream().collect(Collectors.toMap(
                r -> r.get("animeId").asString(),
                r -> r.get("rating").asInt()));
        var animeRatings2 = ratings2.stream().collect(Collectors.toMap(
                r -> r.get("animeId").asString(),
                r -> r.get("rating").asInt()));

        var comunes = new java.util.HashSet<>(animeRatings1.keySet());
        comunes.retainAll(animeRatings2.keySet());

        if (comunes.isEmpty()) {
            return 0f;
        }

        // Calcular medias
        float avg1 = (float) animeRatings1.entrySet().stream()
                .filter(e -> comunes.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .average().orElse(0f);
        float avg2 = (float) animeRatings2.entrySet().stream()
                .filter(e -> comunes.contains(e.getKey()))
                .mapToInt(Map.Entry::getValue)
                .average().orElse(0f);

        // Calcular correlación de Pearson
        float num = 0f, den1 = 0f, den2 = 0f;
        for (String animeId : comunes) {
            float d1 = animeRatings1.get(animeId) - avg1;
            float d2 = animeRatings2.get(animeId) - avg2;
            num += d1 * d2;
            den1 += d1 * d1;
            den2 += d2 * d2;
        }

        if (den1 == 0f || den2 == 0f) {
            return 0f;
        }
        return num / ((float) (Math.sqrt(den1) * Math.sqrt(den2)));
    }
}