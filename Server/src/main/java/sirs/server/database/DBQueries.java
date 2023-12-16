package sirs.server.database;

public class DBQueries {
    
    public static String GET_ALL_RESTAURANTS = "SELECT restaurant " +
                                                "FROM clients WHERE username = ?";
    public static String GET_RESTAURANT = "select restaurant from clients " +
                                            "where username = ? and " +
                            "restaurant->'restaurantInfo'->>'restaurant' = ?";


}
