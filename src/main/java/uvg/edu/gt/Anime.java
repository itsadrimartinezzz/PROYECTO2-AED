package uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anime {
    private String id; // Unique identifier for the anime
    private String nombre; // Anime name
    private List<String> generos; // List of genres (e.g., ["Acción", "Fantasia"])
    private Map<String, Float> caracteristicas; // Map of characteristics (e.g., "Acción" -> 0.8)

    // Constructor
    public Anime(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.generos = new ArrayList<>();
        this.caracteristicas = new HashMap<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public Map<String, Float> obtenerCaracteristicas() {
        return caracteristicas;
    }

    // Metodo para añadir géneros y características
    public void agregarGenero(String genero) {
        if (!generos.contains(genero)) {
            generos.add(genero);
        }
    }

    public void agregarCaracteristica(String clave, float valor) {
        if (valor >= 0 && valor <= 1) { // Assuming characteristics are normalized between 0 and 1
            caracteristicas.put(clave, valor);
        }
    }

    @Override
    public String toString() {
        return "Anime{id='" + id + "', nombre='" + nombre + "', generos=" + generos + "}";
    }
}