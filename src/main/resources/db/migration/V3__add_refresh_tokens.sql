CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    expiry_date TIMESTAMP NOT NULL
);