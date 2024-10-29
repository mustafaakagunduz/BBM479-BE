CREATE TABLE survey (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        admin_id BIGINT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (admin_id) REFERENCES app_user(id) ON DELETE CASCADE
);
CREATE TABLE entered_survey (
                                id SERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                survey_id BIGINT NOT NULL,
                                entered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
                                FOREIGN KEY (survey_id) REFERENCES survey(id) ON DELETE CASCADE
);
