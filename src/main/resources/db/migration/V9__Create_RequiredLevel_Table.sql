CREATE TABLE required_level (
                                id SERIAL PRIMARY KEY,
                                profession_id BIGINT NOT NULL,
                                skill_id BIGINT NOT NULL,
                                required_level INT CHECK (required_level BETWEEN 1 AND 5),
                                FOREIGN KEY (profession_id) REFERENCES profession(id) ON DELETE CASCADE,
                                FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);