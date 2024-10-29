CREATE TABLE profession_skill (
                                  profession_id BIGINT,
                                  skill_id BIGINT,
                                  PRIMARY KEY (profession_id, skill_id),
                                  FOREIGN KEY (profession_id) REFERENCES profession(id) ON DELETE CASCADE,
                                  FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);