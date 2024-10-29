CREATE TABLE question (
                          id SERIAL PRIMARY KEY,
                          text TEXT NOT NULL,
                          skill_id BIGINT,
                          survey_id BIGINT,
                          FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE SET NULL,
                          FOREIGN KEY (survey_id) REFERENCES survey(id) ON DELETE CASCADE
);