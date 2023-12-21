DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    restaurant JSONB NOT NULL
);

INSERT INTO clients (username, restaurant) VALUES
    (
        'daniel', 
        '{
        "restaurantInfo": {
            "owner": "Maria Silva",
            "restaurant": "Dona Maria",
            "address": "Rua da Glória, 22, Lisboa",
            "genre": ["Portuguese", "Traditional"],
            "menu": [
                {
                    "itemName": "House Steak",
                    "category": "Meat",
                    "description": "A succulent sirloin grilled steak.",
                    "price": 24.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Sardines",
                    "category": "Fish",
                    "description": "A Portuguese staple, accompanied by potatoes and salad.",
                    "price": 21.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Mushroom Risotto",
                    "category": "Vegetarian",
                    "description": "Creamy Arborio rice cooked with assorted mushrooms and Parmesan cheese.",
                    "price": 16.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "John Doe",
                    "rating": 4,
                    "comment": "Great food and excellent service!"
                },
                {
                    "user": "Emily Brown",
                    "comment": "Not bad, but nothing extraordinary either.",
                    "rating": 3
                }
            ],
            "mealVouchers": [
                {
                    "code": "VOUCHER123",
                    "description": "Redeem this code for a 20% discount in the meal. Drinks not included."
                },
                {
                    "code": "EASYMONEY",
                    "description": "Redeem this code for 1 million euros. Drinks still not included."
                }
            ]
        }
        }'::JSONB
    ),
    (
        'daniel',
        '{
        "restaurantInfo": {
            "owner": "Carlos Rodriguez",
            "restaurant": "El Sabor Espanhol",
            "address": "Calle de la Paz, 15, Madrid",
            "genre": ["Spanish", "Tapas"],
            "menu": [
                {
                    "itemName": "Paella Valenciana",
                    "category": "Seafood",
                    "description": "Classic Spanish saffron-infused rice with a mix of seafood.",
                    "price": 27.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Patatas Bravas",
                    "category": "Appetizer",
                    "description": "Crispy fried potatoes served with a spicy tomato sauce.",
                    "price": 14.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Gazpacho",
                    "category": "Soup",
                    "description": "Chilled tomato-based soup with peppers, onions, and cucumbers.",
                    "price": 12.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "Alice Smith",
                    "comment": "The steak was amazing.",
                    "rating": 4
                },
                {
                    "user": "Bob Johnson",
                    "comment": "Good service but the menu can be improved.",
                    "rating": 3
                }
            ],
            "mealVouchers": [
                {
                    "code": "VOUCHER456",
                    "description": "Enjoy a 15% discount on your meal with this voucher. Limited to one use per visit."
                }
            ]
        }
        }'::JSONB
    ),
    (
        'simao', 
        '{
        "restaurantInfo": {
            "owner": "Maria Silva",
            "restaurant": "Dona Maria",
            "address": "Rua da Glória, 22, Lisboa",
            "genre": ["Portuguese", "Traditional"],
            "menu": [
                {
                    "itemName": "House Steak",
                    "category": "Meat",
                    "description": "A succulent sirloin grilled steak.",
                    "price": 24.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Sardines",
                    "category": "Fish",
                    "description": "A Portuguese staple, accompanied by potatoes and salad.",
                    "price": 21.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Mushroom Risotto",
                    "category": "Vegetarian",
                    "description": "Creamy Arborio rice cooked with assorted mushrooms and Parmesan cheese.",
                    "price": 16.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "John Doe",
                    "rating": 4,
                    "comment": "Great food and excellent service!"
                },
                {
                    "user": "Emily Brown",
                    "comment": "Not bad, but nothing extraordinary either.",
                    "rating": 3
                }
            ],
            "mealVouchers": [
                {
                    "code": "SIMAOVOUCHER123",
                    "description": "Redeem this code for a 20% discount in the meal. Drinks not included."
                }
            ]
        }
        }'::JSONB
    ),
    (
        'simao',
        '{
        "restaurantInfo": {
            "owner": "Carlos Rodriguez",
            "restaurant": "El Sabor Espanhol",
            "address": "Calle de la Paz, 15, Madrid",
            "genre": ["Spanish", "Tapas"],
            "menu": [
                {
                    "itemName": "Paella Valenciana",
                    "category": "Seafood",
                    "description": "Classic Spanish saffron-infused rice with a mix of seafood.",
                    "price": 27.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Patatas Bravas",
                    "category": "Appetizer",
                    "description": "Crispy fried potatoes served with a spicy tomato sauce.",
                    "price": 14.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Gazpacho",
                    "category": "Soup",
                    "description": "Chilled tomato-based soup with peppers, onions, and cucumbers.",
                    "price": 12.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "Alice Smith",
                    "comment": "The steak was amazing.",
                    "rating": 4
                },
                {
                    "user": "Bob Johnson",
                    "comment": "Good service but the menu can be improved.",
                    "rating": 3
                }
            ],
            "mealVouchers": [
                {
                    "code": "SIMAOVOUCHER456",
                    "description": "Enjoy a 150% discount on your meal with this voucher. Limited to one use per visit."
                }
            ]
        }
        }'::JSONB
    ),
    (
        'ricardo', 
        '{
        "restaurantInfo": {
            "owner": "Maria Silva",
            "restaurant": "Dona Maria",
            "address": "Rua da Glória, 22, Lisboa",
            "genre": ["Portuguese", "Traditional"],
            "menu": [
                {
                    "itemName": "House Steak",
                    "category": "Meat",
                    "description": "A succulent sirloin grilled steak.",
                    "price": 24.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Sardines",
                    "category": "Fish",
                    "description": "A Portuguese staple, accompanied by potatoes and salad.",
                    "price": 21.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Mushroom Risotto",
                    "category": "Vegetarian",
                    "description": "Creamy Arborio rice cooked with assorted mushrooms and Parmesan cheese.",
                    "price": 16.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "John Doe",
                    "rating": 4,
                    "comment": "Great food and excellent service!"
                },
                {
                    "user": "Emily Brown",
                    "comment": "horrible food",
                    "rating": 5
                }
            ],
            "mealVouchers": [
                {
                    "code": "RICARDOVOUCHER123",
                    "description": "Redeem this code for a 20% discount in the meal. Drinks not included."
                }
            ]
        }
        }'::JSONB
    ),
    (
        'ricardo',
        '{
        "restaurantInfo": {
            "owner": "Carlos Rodriguez",
            "restaurant": "El Sabor Espanhol",
            "address": "Calle de la Paz, 15, Madrid",
            "genre": ["Spanish", "Tapas"],
            "menu": [
                {
                    "itemName": "Paella Valenciana",
                    "category": "Seafood",
                    "description": "Classic Spanish saffron-infused rice with a mix of seafood.",
                    "price": 27.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Patatas Bravas",
                    "category": "Appetizer",
                    "description": "Crispy fried potatoes served with a spicy tomato sauce.",
                    "price": 14.99,
                    "currency": "EUR"
                },
                {
                    "itemName": "Gazpacho",
                    "category": "Soup",
                    "description": "Chilled tomato-based soup with peppers, onions, and cucumbers.",
                    "price": 12.99,
                    "currency": "EUR"
                }
            ],
            "reviews": [
                {
                    "user": "Alice Smith",
                    "comment": "The steak was amazing.",
                    "rating": 0
                },
                {
                    "user": "Bob Johnson",
                    "comment": "Good service but the menu can be improved.",
                    "rating": 3
                }
            ],
            "mealVouchers": [
                {
                    "code": "RICARDOVOUCHER456",
                    "description": "Enjoy a 150% discount on your meal with this voucher. Limited to one use per visit."
                }
            ]
        }
        }'::JSONB
    );
    