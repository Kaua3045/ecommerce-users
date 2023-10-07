CREATE TABLE roles_permissions (
    role_id VARCHAR(36) NOT NULL,
    permission_id VARCHAR(36) NOT NULL,
    permission_name VARCHAR(50) NOT NULL,
    CONSTRAINT idx_roles_permissions UNIQUE (role_id, permission_id),
    CONSTRAINT fk_roles_permissions_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_roles_permissions_permission_id FOREIGN KEY (permission_id) REFERENCES permissions (id_permission) ON DELETE CASCADE
);