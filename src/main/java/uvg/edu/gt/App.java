package uvg.edu.gt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Initialize some anime data
        Map<String, Float> caracteristicasNaruto = new HashMap<>();
        caracteristicasNaruto.put("violencia", 0.8f);
        caracteristicasNaruto.put("humor", 0.3f);

        Map<String, Float> caracteristicasKimiNoNaWa = new HashMap<>();
        caracteristicasKimiNoNaWa.put("romance", 0.9f);
        caracteristicasKimiNoNaWa.put("drama", 0.7f);

        Map<String, Float> caracteristicasSAO = new HashMap<>();
        caracteristicasSAO.put("fantasia", 0.9f);
        caracteristicasSAO.put("accion", 0.6f);

        Anime naruto = new Anime("A1", "Naruto", Arrays.asList(Genero.ACCION), caracteristicasNaruto);
        Anime kimiNoNaWa = new Anime("A2", "Kimi no Na wa", Arrays.asList(Genero.ROMANCE), caracteristicasKimiNoNaWa);
        Anime sao = new Anime("A3", "Sword Art Online", Arrays.asList(Genero.FANTASIA), caracteristicasSAO);

        List<Anime> allAnimes = Arrays.asList(naruto, kimiNoNaWa, sao);

        // Step 2: Initialize BaseDeDatos with some example users
        BaseDeDatos baseDeDatos = new BaseDeDatos();
        Usuario luis = new Usuario("U2", "Luis");
        luis.calificarAnime(naruto, 4);
        luis.calificarAnime(kimiNoNaWa, 3);
        baseDeDatos.agregarRating(luis, naruto, 4);
        baseDeDatos.agregarRating(luis, kimiNoNaWa, 3);

        // Step 3: Get user information
        System.out.println("Bienvenido al Sistema de Recomendación de Animes!");
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();
        Usuario usuario = new Usuario("U" + (baseDeDatos.getRatings().size() + 1), nombre);

        // Step 4: Get user preferences
        System.out.println("Seleccione sus géneros preferidos (ingrese el número, separados por comas, o '0' para terminar):");
        System.out.println("1. Acción");
        System.out.println("2. Comedia");
        System.out.println("3. Romance");
        System.out.println("4. Fantasía");
        System.out.println("5. Slice of Life");
        String input = scanner.nextLine();
        while (!input.equals("0")) {
            String[] generos = input.split(",");
            for (String g : generos) {
                try {
                    int opcion = Integer.parseInt(g.trim());
                    Genero genero = null;
                    switch (opcion) {
                        case 1: genero = Genero.ACCION; break;
                        case 2: genero = Genero.COMEDIA; break;
                        case 3: genero = Genero.ROMANCE; break;
                        case 4: genero = Genero.FANTASIA; break;
                        case 5: genero = Genero.SLICE_OF_LIFE; break;
                        default: System.out.println("Opción inválida: " + opcion);
                    }
                    if (genero != null) {
                        usuario.agregarPreferencia(genero, 5); // Default preference score
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida: " + g);
                }
            }
            System.out.println("Ingrese más géneros o '0' para terminar:");
            input = scanner.nextLine();
        }

        // Step 5: Let the user rate some animes
        System.out.println("Ahora puede calificar algunos animes (0-10). Ingrese '0' para terminar.");
        for (Anime anime : allAnimes) {
            System.out.print("Califique '" + anime.getNombre() + "' (" + anime.getGeneros() + ") [0 para omitir]: ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            if (rating == 0) continue;
            if (rating < 0 || rating > 10) {
                System.out.println("Calificación inválida, debe estar entre 0 y 10.");
                continue;
            }
            usuario.calificarAnime(anime, rating);
            baseDeDatos.agregarRating(usuario, anime, rating);
        }

        // Step 6: Collaborative filtering - find similar users
        RecomendadorColaborativo rc = new RecomendadorColaborativo(0.5f, baseDeDatos);
        Map<Usuario, Float> similarUsers = new HashMap<>();
        for (Usuario otherUser : baseDeDatos.getRatings().keySet()) {
            if (!otherUser.getId().equals(usuario.getId())) {
                float similitud = rc.calcularSimilaridadUsuarios(usuario, otherUser);
                if (similitud >= 0.5f) {
                    similarUsers.put(otherUser, similitud);
                }
            }
        }

        // Step 7: Content-based filtering - find similar animes
        Map<String, Float> pesosCaracteristicas = new HashMap<>();
        pesosCaracteristicas.put("violencia", 1.0f);
        pesosCaracteristicas.put("humor", 0.5f);
        pesosCaracteristicas.put("romance", 1.0f);
        pesosCaracteristicas.put("drama", 0.8f);
        pesosCaracteristicas.put("fantasia", 0.7f);
        pesosCaracteristicas.put("accion", 0.9f);

        RecomendadorContenido rcont = new RecomendadorContenido(pesosCaracteristicas);
        Map<Anime, Float> similarAnimes = new HashMap<>();
        for (Anime ratedAnime : usuario.getCalificaciones().keySet()) {
            if (usuario.getCalificaciones().get(ratedAnime) >= 4) { // Only consider animes rated 4 or higher
                for (Anime otherAnime : allAnimes) {
                    if (!otherAnime.getId().equals(ratedAnime.getId()) && !usuario.getCalificaciones().containsKey(otherAnime)) {
                        float similitud = rcont.calcularSimilaridadAnimes(ratedAnime, otherAnime);
                        if (similitud > 0.3f) {
                            similarAnimes.put(otherAnime, similitud);
                        }
                    }
                }
            }
        }

        // Step 8: Display recommendations
        System.out.println("\n=== Recomendaciones para " + usuario.getNombre() + " ===");
        if (!similarUsers.isEmpty()) {
            System.out.println("Basado en usuarios similares:");
            for (Map.Entry<Usuario, Float> entry : similarUsers.entrySet()) {
                Usuario similarUser = entry.getKey();
                for (Map.Entry<Anime, Integer> rating : similarUser.getCalificaciones().entrySet()) {
                    Anime anime = rating.getKey();
                    if (!usuario.getCalificaciones().containsKey(anime)) {
                        System.out.println(" - " + anime.getNombre() + " (" + anime.getGeneros() + "), rated " + rating.getValue() + " by " + similarUser.getNombre());
                    }
                }
            }
        } else {
            System.out.println("No se encontraron usuarios similares para recomendaciones colaborativas.");
        }

        if (!similarAnimes.isEmpty()) {
            System.out.println("Basado en contenido (animes similares a los que te gustaron):");
            for (Map.Entry<Anime, Float> entry : similarAnimes.entrySet()) {
                Anime anime = entry.getKey();
                System.out.println(" - " + anime.getNombre() + " (" + anime.getGeneros() + "), similitud: " + entry.getValue());
            }
        } else {
            System.out.println("No se encontraron animes similares para recomendaciones basadas en contenido.");
        }

        scanner.close();
    }
}