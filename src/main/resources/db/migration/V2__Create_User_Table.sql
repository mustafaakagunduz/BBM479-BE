CREATE TABLE company (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) UNIQUE NOT NULL,
                         description TEXT
);
CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          role_id BIGINT,
                          company_id BIGINT,
                          email_verified BOOLEAN DEFAULT FALSE,
                          verification_token VARCHAR(255),
                          verification_token_expiry TIMESTAMP,
                          FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE SET NULL,
                          FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE SET NULL
);