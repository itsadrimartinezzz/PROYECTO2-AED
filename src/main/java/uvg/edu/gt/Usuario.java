//Proyecto 2 Grupo 6
package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nombre;
    private Map<Genero, Integer> preferencias;
    private Map<Anime, Integer> calificaciones;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preferencias = new HashMap<>();
        this.calificaciones = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Map<Genero, Integer> obtenerPreferencias() {
        return preferencias;
    }

    public Map<Anime, Integer> getCalificaciones() {
        return calificaciones;
    }

    public void agregarPreferencia(Genero genero, int valor) {
        preferencias.put(genero, valor);
    }

    public void calificarAnime(Anime anime, int calificacion) {
        if (calificacion >= 0 && calificacion <= 10) {
            calificaciones.put(anime, calificacion);
            // Update preferences based on anime genres
            for (String generoStr : anime.getGeneros()) {
                Genero genero = Genero.valueOf(generoStr);
                // Calculate the average rating for this genre
                long count = calificaciones.keySet().stream()
                    .filter(a -> a.getGeneros().contains(generoStr))
                    .count(); // count() returns a long
                if (count > 0) { // Ensure we don't divide by zero
                    int sum = calificaciones.keySet().stream()
                        .filter(a -> a.getGeneros().contains(generoStr))
                        .mapToInt(a -> calificaciones.get(a))
                        .sum(); // Sum the ratings for this genre
                    int newRating = (int) (sum / count); // Calculate average
                    preferencias.put(genero, newRating);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', preferencias=" + preferencias + "}";
    }
}
