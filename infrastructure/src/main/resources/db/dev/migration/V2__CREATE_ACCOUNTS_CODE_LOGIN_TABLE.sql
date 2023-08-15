CREATE TABLE accounts_code_login (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    code VARCHAR(36) UNIQUE NOT NULL,
    code_challenge VARCHAR(100) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_account_id_code FOREIGN KEY (account_id) REFERENCES accounts (id)
);