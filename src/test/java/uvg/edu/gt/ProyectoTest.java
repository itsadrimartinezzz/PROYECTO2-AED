import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProyectoTest{
    @Test
    public void testObtenerCaracteristicas(){
        Map<String, Float> caracteristicas = new HashMap<>();
        caracteristicas.put("violencia", 0.8f);
        caracteristicas.put("romance", 0.2f);

        List<Genero> generos = Arrays.asList(Genero.ACCION, Genero.ROMANCE);
        Anime anime = new Anime("1", "Naruto", generos, caracteristicas);

        Map<String, Float> result = anime.obtenerCaracteristicas();
        assertEquals(2, result.size());
        assertEquals(0.8f, result.get("violencia"));
        assertEquals(0.2f, result.get("romance"));
    }

    @Test
    public void TestcalcularSimilaridadUsuarios(){
        //Usuarios
        Usuario u1 = new Usuario("U1", "Ana");
        Usuario u2 = new Usuario("U2", "Luis");

        //Anime
        Anime anime1 = new Anime("A1", "One Piece", List.of(Genero.ACCION), new HashMap<>());
        Anime anime2 = new Anime("A2", "Kimi no Na wa", List.of(Genero.ROMANCE), new HashMap<>());

        //Calificaciones
        u1.calificarAnime(anime1, 5);
        u1.calificarAnime(anime2, 2);
        u2.calificarAnime(anime1, 4);
        u2.calificarAnime(anime2, 3);

        //Similitud
        RecomendadorColaborativo rc = new RecomendadorColaborativo(0.5f);
        float similitud = rc.calcularSimilaridadUsuarios(u1, u2);

        assertTrue(similitud >= 0 && similitud <= 1);
    }

    @Test
    public void testCalificarAnimeYPreferencias(){
        Usuario usuario = new Usuario("U1", "Carlos");

        Map<String, Float> caracteristicas = new HashMap<>();
        caracteristicas.put("fantasia", 0.9f);
        Anime anime = new Anime("2", "SAO", Arrays.asList(Genero.FANTASIA), caracteristicas);

        usuario.calificarAnime(anime, 5);

        Map<Genero, Integer> prefs = usuario.obtenerPreferencias();
        assertEquals(1, prefs.size());
        assertEquals(5, prefs.get(Genero.FANTASIA));
    }
}
