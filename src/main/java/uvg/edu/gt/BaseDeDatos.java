package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;

public class BaseDeDatos {
    private Map<Usuario, Map<Anime, Integer>> ratings;

    public BaseDeDatos() {
        this.ratings = new HashMap<>();
    }

    public void agregarRating(Usuario usuario, Anime anime, int rating) {
        ratings.computeIfAbsent(usuario, k -> new HashMap<>()).put(anime, rating);
    }

    public Map<Usuario, Map<Anime, Integer>> getRatings() {
        return ratings;
    }
}