CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          role_id BIGINT,
                          FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE SET NULL
);
