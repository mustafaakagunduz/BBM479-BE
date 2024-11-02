-- Profession Table (each profession belongs to exactly one industry)
CREATE TABLE profession (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            industry_id INT REFERENCES industry(id) ON DELETE SET NULL
);