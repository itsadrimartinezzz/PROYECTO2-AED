package uvg.edu.gt;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class RecomendadorColaborativo {
    /** 
     * Umbral mínimo de similitud para considerar dos usuarios “similares” 
     */
    private float umbralSimilitud;
    private BaseDeDatos baseDeDatos;

    public RecomendadorColaborativo(float umbralSimilitud, BaseDeDatos baseDeDatos) {
        this.umbralSimilitud = umbralSimilitud;
        this.baseDeDatos = baseDeDatos;
    }

    /**
     * Calcula la similitud entre dos usuarios usando correlación de Pearson
     * sobre las calificaciones de los animes que ambos han puntuado.
     */
    public float calcularSimilaridadUsuarios(Usuario u1, Usuario u2) {
        Map<Usuario, Map<Anime, Integer>> allRatings = baseDeDatos.getRatings();
        Map<Anime, Integer> r1 = allRatings.getOrDefault(u1, Collections.emptyMap());
        Map<Anime, Integer> r2 = allRatings.getOrDefault(u2, Collections.emptyMap());

        // Solo nos interesan los animes calificados por ambos
        Set<Anime> comunes = new HashSet<>(r1.keySet());
        comunes.retainAll(r2.keySet());

        if (comunes.isEmpty()) {
            return 0f;
        }

        // Media de calificaciones en los ítems comunes
        float avg1 = 0f, avg2 = 0f;
        for (Anime a : comunes) {
            avg1 += r1.get(a);
            avg2 += r2.get(a);
        }
        avg1 /= comunes.size();
        avg2 /= comunes.size();

        // Cálculo de numerador y denominadores
        float num = 0f, den1 = 0f, den2 = 0f;
        for (Anime a : comunes) {
            float d1 = r1.get(a) - avg1;
            float d2 = r2.get(a) - avg2;
            num  += d1 * d2;
            den1 += d1 * d1;
            den2 += d2 * d2;
        }

        if (den1 == 0f || den2 == 0f) {
            return 0f;
        }
        return num / ((float)(Math.sqrt(den1) * Math.sqrt(den2)));
    }
}

