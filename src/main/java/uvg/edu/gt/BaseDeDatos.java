package uvg.edu.gt;

import org.neo4j.driver.*;

import java.util.List;
import java.util.Map;

public class BaseDeDatos {
    private final Driver driver;

    public BaseDeDatos(Driver driver) {
        this.driver = driver;
    }

    public List<Record> getRatingsForUser(String userId) {
        try (Session session = driver.session()) {
            return session.run("MATCH (u:User {id: $userId})-[:RATED]->(a:Anime) " +
                    "RETURN a.id AS animeId, a.name AS animeName, (u)-[:RATED]->(a).rating AS rating",
                    Map.of("userId", userId)).list();
        }
    }

    public List<Record> getSimilarUsers(String userId) {
        try (Session session = driver.session()) {
            return session.run(
                    "MATCH (u1:User {id: $userId})-[r1:RATED]->(a:Anime)<-[r2:RATED]-(u2:User) " +
                            "WHERE u1 <> u2 AND u2.id <> $userId " +
                            "WITH u2, COLLECT(a.name) AS commonAnimeNames, " + // Cambiado para devolver nombres de
                                                                               // animes
                            "     COLLECT(r1.rating) AS ratings1, " +
                            "     COLLECT(r2.rating) AS ratings2 " +
                            "WHERE SIZE(commonAnimeNames) > 0 " +
                            "WITH u2, commonAnimeNames, ratings1, ratings2, " +
                            "     REDUCE(s = 0.0, i IN RANGE(0, SIZE(ratings1)-1) | s + ratings1[i] * ratings2[i]) AS dotProduct, "
                            +
                            "     SQRT(REDUCE(s = 0.0, x IN ratings1 | s + x * x)) AS norm1, " +
                            "     SQRT(REDUCE(s = 0.0, x IN ratings2 | s + x * x)) AS norm2 " +
                            "RETURN u2.id AS userId, COALESCE(u2.name, u2.id) AS userName, " + // Usar COALESCE para
                                                                                               // evitar null
                            "       commonAnimeNames AS commonAnimes, " +
                            "       CASE WHEN norm1 * norm2 = 0 THEN 0 ELSE dotProduct / (norm1 * norm2) END AS similarity "
                            +
                            "ORDER BY similarity DESC LIMIT 3",
                    Map.of("userId", userId)).list();
        }
    }

    public void agregarRating(String userId, String animeId, int rating) {
        try (Session session = driver.session()) {
            session.run(
                    "MATCH (u:User {id: $userId}), (a:Anime {id: $animeId}) " +
                            "MERGE (u)-[r:RATED]->(a) " +
                            "SET r.rating = $rating",
                    Map.of("userId", userId, "animeId", animeId, "rating", rating));
        }
    }
}