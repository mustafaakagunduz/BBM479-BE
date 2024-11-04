CREATE TABLE response (
                          id SERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          survey_id BIGINT NOT NULL,
                          question_id BIGINT NOT NULL,
                          option_id BIGINT,
                          entered_level INT CHECK (entered_level BETWEEN 1 AND 5),
                          answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
                          FOREIGN KEY (survey_id) REFERENCES survey(id) ON DELETE CASCADE,
                          FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
                          FOREIGN KEY (option_id) REFERENCES option(id) ON DELETE SET NULL
);
