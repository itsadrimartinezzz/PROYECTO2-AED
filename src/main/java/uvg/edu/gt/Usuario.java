package uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario {
    private String id; // Identificador único del usuario
    private String nombre; // Nombre del usuario
    private List<String> preferencias; // Lista de géneros preferidos (e.g., ["Acción", "Comedia"])
    private Map<Anime, Integer> calificaciones; // Mapa de los animes con sus user ratings

    // Constructor
    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preferencias = new ArrayList<>();
        this.calificaciones = new HashMap<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getPreferencias() {
        return preferencias;
    }

    public Map<Anime, Integer> getCalificaciones() {
        return calificaciones;
    }

    // Methods to add preferences and ratings
    public void agregarPreferencia(String genero) {
        if (!preferencias.contains(genero)) {
            preferencias.add(genero);
        }
    }

    public void calificarAnime(Anime anime, int calificacion) {
        if (calificacion >= 0 && calificacion <= 10) { // Assuming a 0-10 rating scale
            calificaciones.put(anime, calificacion);
        }
    }

    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', preferencias=" + preferencias + "}";
    }
}