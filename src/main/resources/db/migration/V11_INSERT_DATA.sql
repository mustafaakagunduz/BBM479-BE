-- Role tablosuna veriler ekleyin
INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

-- App_User tablosuna veriler ekleyin
INSERT INTO app_user (id, name, email, username, password, role_id) VALUES (1, 'Admin User', 'admin@example.com', 'admin', 'password_hash', 1);
INSERT INTO app_user (id, name, email, username, password, role_id) VALUES (2, 'Regular User', 'user@example.com', 'user', 'password_hash', 2);

-- Skill tablosuna veriler ekleyin
INSERT INTO skill (id, name) VALUES (1, 'Java');
INSERT INTO skill (id, name) VALUES (2, 'Python');
INSERT INTO skill (id, name) VALUES (3, 'SQL');
INSERT INTO skill (id, name) VALUES (4, 'C++');
-- Profession tablosuna veriler ekleyin
INSERT INTO profession (id, name) VALUES (1, 'Software Developer');
INSERT INTO profession (id, name) VALUES (2, 'Data Analyst');
INSERT INTO profession (id, name) VALUES (3, 'Analyst');

-- RequiredLevel tablosuna veriler ekleyin
INSERT INTO required_level (id, profession_id, skill_id, required_level) VALUES (1, 1, 1, 4); -- Software Developer için Java seviyesi 4
INSERT INTO required_level (id, profession_id, skill_id, required_level) VALUES (2, 1, 2, 3); -- Software Developer için Python seviyesi 3
INSERT INTO required_level (id, profession_id, skill_id, required_level) VALUES (3, 2, 3, 4); -- Data Analyst için SQL seviyesi 4
INSERT INTO required_level (id, profession_id, skill_id, required_level) VALUES (3, 3, 3, 4); -- Data Analyst için SQL seviyesi 4

-- Survey tablosuna veri ekleyin
INSERT INTO survey (id, title, admin_id) VALUES (1, 'Developer Skills Survey', 1);

-- Question tablosuna veri ekleyin
INSERT INTO question (id, text, skill_id, survey_id) VALUES (1, 'How proficient are you in Java?', 1, 1);
INSERT INTO question (id, text, skill_id, survey_id) VALUES (2, 'Rate your Python skills', 2, 1);

-- Question tablosuna 3. ve 4. soruları ekleyin
INSERT INTO question (id, text, skill_id, survey_id) VALUES (3, 'How proficient are you in C#?', 3, 1);
INSERT INTO question (id, text, skill_id, survey_id) VALUES (4, 'Rate your JavaScript skills', 4, 1);

-- Option tablosuna veriler ekleyin
INSERT INTO option (id, level, description, question_id) VALUES (1, 1, 'Beginner', 1);
INSERT INTO option (id, level, description, question_id) VALUES (2, 2, 'Intermediate', 1);
INSERT INTO option (id, level, description, question_id) VALUES (3, 3, 'Advanced', 1);
INSERT INTO option (id, level, description, question_id) VALUES (4, 4, 'Expert', 1);
INSERT INTO option (id, level, description, question_id) VALUES (5, 5, 'Master', 1);

INSERT INTO option (id, level, description, question_id) VALUES (6, 1, 'Beginner', 2);
INSERT INTO option (id, level, description, question_id) VALUES (7, 2, 'Intermediate', 2);
INSERT INTO option (id, level, description, question_id) VALUES (8, 3, 'Advanced', 2);
INSERT INTO option (id, level, description, question_id) VALUES (9, 4, 'Expert', 2);
INSERT INTO option (id, level, description, question_id) VALUES (10, 5, 'Master', 2);

INSERT INTO option (id, level, description, question_id) VALUES (11, 1, 'Beginner', 3);
INSERT INTO option (id, level, description, question_id) VALUES (12, 2, 'Intermediate', 3);
INSERT INTO option (id, level, description, question_id) VALUES (13, 3, 'Advanced', 3);
INSERT INTO option (id, level, description, question_id) VALUES (14, 4, 'Expert', 3);
INSERT INTO option (id, level, description, question_id) VALUES (15, 5, 'Master', 3);

INSERT INTO option (id, level, description, question_id) VALUES (16, 1, 'Beginner', 4);
INSERT INTO option (id, level, description, question_id) VALUES (17, 2, 'Intermediate', 4);
INSERT INTO option (id, level, description, question_id) VALUES (18, 3, 'Advanced', 4);
INSERT INTO option (id, level, description, question_id) VALUES (19, 4, 'Expert', 4);
INSERT INTO option (id, level, description, question_id) VALUES (20, 5, 'Master', 4);

INSERT INTO entered_survey (id, user_id, survey_id) VALUES
    (1, 2, 1);

-- Response tablosuna veriler ekleyin
INSERT INTO response (id, survey_id, question_id, option_id, entered_level) VALUES (1, 1, 1, 2, 2); -- Java Intermediate seçildi
INSERT INTO response (id, survey_id, question_id, option_id, entered_level) VALUES (2, 1, 2, 3, 3); -- Python Advanced seçildi
INSERT INTO response (id, survey_id, question_id, option_id, entered_level) VALUES (3, 1, 3, 1, 1); -- C# Beginner seçildi
INSERT INTO response (id, survey_id, question_id, option_id, entered_level) VALUES (4, 1, 4, 2, 2); -- JavaScript Intermediate seçildi
