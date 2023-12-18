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

    public static String REMOVE_VOUCHER = "UPDATE clients " +
                                        "SET restaurant = ( " +
                                        "SELECT jsonb_set(restaurant, '{restaurantInfo,mealVouchers}', ( " +
                                        "    SELECT jsonb_agg(element) " +
                                        "    FROM jsonb_array_elements(restaurant->'restaurantInfo'->'mealVouchers') AS vouchers(element) " +
                                        "    WHERE NOT (element->>'code' = ?) " +
                                        ")) " +
                                        ") " +
                                        "WHERE username = ? AND restaurant->'restaurantInfo'->>'restaurant' = ?";
}
