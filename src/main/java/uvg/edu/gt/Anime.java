package uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anime {
    private String id;
    private String nombre;
    private List<String> generos;
    private Map<String, Float> caracteristicas;

    // Constructor existente
    public Anime(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.generos = new ArrayList<>();
        this.caracteristicas = new HashMap<>();
    }

    // Nuevo constructor para aceptar géneros y características
    public Anime(String id, String nombre, List<Genero> generos, Map<String, Float> caracteristicas) {
        this.id = id;
        this.nombre = nombre;
        this.generos = new ArrayList<>();
        for (Genero genero : generos) {
            this.generos.add(genero.toString());
        }
        this.caracteristicas = new HashMap<>(caracteristicas);
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

    // Métodos para añadir géneros y características
    public void agregarGenero(String genero) {
        if (!generos.contains(genero)) {
            generos.add(genero);
        }
    }

    public void agregarCaracteristica(String clave, float valor) {
        if (valor >= 0 && valor <= 1) {
            caracteristicas.put(clave, valor);
        }
    }

    @Override
    public String toString() {
        return "Anime{id='" + id + "', nombre='" + nombre + "', generos=" + generos + "}";
    }
}