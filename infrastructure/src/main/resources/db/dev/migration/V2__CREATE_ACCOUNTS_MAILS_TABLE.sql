CREATE TABLE accounts_mails (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    token VARCHAR(36) UNIQUE NOT NULL,
    type VARCHAR(100) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT FK_ACCOUNTS_MAILS_ACCOUNT_ID FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);