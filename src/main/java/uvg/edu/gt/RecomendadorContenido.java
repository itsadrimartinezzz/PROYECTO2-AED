//Proyecto 2 Grupo 6
package uvg.edu.gt;

import java.util.Map;
import org.neo4j.driver.*;

public class RecomendadorContenido {
    private Map<String, Float> pesosCaracteristicas;
    private Driver driver;

    public RecomendadorContenido(Map<String, Float> pesosCaracteristicas, Driver driver) {
        this.pesosCaracteristicas = pesosCaracteristicas;
        this.driver = driver;
    }

    public float calcularSimilaridadAnimes(String animeId1, String animeId2) {
        try (Session session = driver.session()) {
            Record a1 = session.run("MATCH (a:Anime {id: $id}) RETURN a.characteristics AS characteristics",
                    Map.of("id", animeId1)).single();
            Record a2 = session.run("MATCH (a:Anime {id: $id}) RETURN a.characteristics AS characteristics",
                    Map.of("id", animeId2)).single();

            @SuppressWarnings("unchecked")
            Map<String, Float> c1 = (Map<String, Float>) a1.get("characteristics").asObject();
            @SuppressWarnings("unchecked")
            Map<String, Float> c2 = (Map<String, Float>) a2.get("characteristics").asObject();

            float dot = 0f, norm1 = 0f, norm2 = 0f;

            for (Map.Entry<String, Float> e : pesosCaracteristicas.entrySet()) {
                String clave = e.getKey();
                float w = e.getValue();
                float v1 = c1.getOrDefault(clave, 0f);
                float v2 = c2.getOrDefault(clave, 0f);

                dot += w * v1 * v2;
                norm1 += w * v1 * v1;
                norm2 += w * v2 * v2;
            }

            if (norm1 == 0f || norm2 == 0f) {
                return 0f;
            }
            return dot / ((float) (Math.sqrt(norm1) * Math.sqrt(norm2)));
        }
    }
}
