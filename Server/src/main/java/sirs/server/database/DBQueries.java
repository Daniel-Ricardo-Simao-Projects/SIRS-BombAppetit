package sirs.server.database;

public class DBQueries {
    
    public static String GET_ALL_RESTAURANTS = "SELECT restaurant->'restaurantInfo'->>'restaurant' AS restaurant_name FROM clients";
    public static String GET_RESTAURANT_VOUCHERS;
    


}
