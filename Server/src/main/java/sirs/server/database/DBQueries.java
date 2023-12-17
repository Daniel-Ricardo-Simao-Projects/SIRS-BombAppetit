package sirs.server.database;

public class DBQueries {
    
    public static String GET_ALL_RESTAURANTS = "SELECT restaurant " +
                                                "FROM clients WHERE username = ?";

    public static String GET_RESTAURANT = "SELECT restaurant FROM clients " +
                                            "WHERE username = ? and " +
                            "restaurant->'restaurantInfo'->>'restaurant' = ?";

    public static String GET_CLIENTS_RESTAURANT = "SELECT * FROM clients " +
                                            "WHERE restaurant->'restaurantInfo'->>'restaurant' = ?";

    public static String UPDATE_RESTAURANT = "UPDATE clients SET restaurant = ?::JSONB " +
                                                "WHERE username = ? and " +
                            "restaurant->'restaurantInfo'->>'restaurant' = ?";
}
