CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    password_hash VARCHAR(255)
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC(38,2),
    category VARCHAR(255),
    image_url VARCHAR(255)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    total_amount NUMERIC(38,2),
    created_at TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id),
    order_id BIGINT REFERENCES orders(id),
    quantity INTEGER NOT NULL
);