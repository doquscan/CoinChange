--liquibase formatted sql

--changeset liquibase-docs:sql-1
CREATE TABLE coin_inventory (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                denomination DECIMAL(10, 2) NOT NULL,
                                quantity INT NOT NULL
);


--changeset liquibase-docs:sql-2
INSERT INTO coin_inventory (denomination, quantity) VALUES (0.01,100);
INSERT INTO coin_inventory (denomination, quantity) VALUES (0.05, 100);
INSERT INTO coin_inventory (denomination, quantity) VALUES (0.10, 100);
INSERT INTO coin_inventory (denomination, quantity) VALUES (0.25, 100);

-- changeset doguscan:create-audit-log-table
CREATE TABLE audit_log (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          bill INT NOT NULL,
                          change_details VARCHAR(255) NOT NULL,
                          timestamp TIMESTAMP NOT NULL
);
