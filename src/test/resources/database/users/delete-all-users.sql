DELETE FROM users_roles WHERE user_id IN (SELECT id FROM users);
DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE shopping_cards AUTO_INCREMENT = 1;
ALTER TABLE cards_items AUTO_INCREMENT = 1;