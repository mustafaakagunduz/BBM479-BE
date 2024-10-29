CREATE TABLE option (
                        id SERIAL PRIMARY KEY,
                        level INT CHECK (level BETWEEN 1 AND 5),
                        description TEXT NOT NULL,
                        question_id BIGINT,
                        FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);