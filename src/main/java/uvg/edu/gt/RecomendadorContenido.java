package uvg.edu.gt;
import java.util.Map;

public class RecomendadorContenido {
    /** 
     * Mapa de nombre de característica → peso (importancia) 
     */
    private Map<String, Float> pesosCaracteristicas;

    public RecomendadorContenido(Map<String, Float> pesosCaracteristicas) {
        this.pesosCaracteristicas = pesosCaracteristicas;
    }

    /**
     * Calcula la similitud entre dos animes usando una variante
     * de similitud coseno ponderada:
     *    sim = (Σ weight[i]·v1[i]·v2[i]) / (√Σ weight[i]·v1[i]² · √Σ weight[i]·v2[i]²)
     */
    public float calcularSimilaridadAnimes(Anime a1, Anime a2) {
        Map<String, Float> c1 = a1.obtenerCaracteristicas();
        Map<String, Float> c2 = a2.obtenerCaracteristicas();

        float dot    = 0f;
        float norm1  = 0f;
        float norm2  = 0f;

        for (Map.Entry<String, Float> e : pesosCaracteristicas.entrySet()) {
            String clave = e.getKey();
            float w       = e.getValue();
            float v1      = c1.getOrDefault(clave, 0f);
            float v2      = c2.getOrDefault(clave, 0f);

            dot   += w * v1 * v2;
            norm1 += w * v1 * v1;
            norm2 += w * v2 * v2;
        }

        if (norm1 == 0f || norm2 == 0f) {
            return 0f;
        }
        return dot / ((float)(Math.sqrt(norm1) * Math.sqrt(norm2)));
    }
}
