
CREATE TABLE industry (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);


CREATE TABLE skill (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       industry_id INT REFERENCES industry(id) ON DELETE SET NULL
);


