CREATE TABLE permissions (
    permission_id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
);