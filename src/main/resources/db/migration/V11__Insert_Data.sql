-- Role tablosuna veriler ekleyin
INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

-- App_User tablosuna veriler ekleyin
INSERT INTO app_user ( name, email, username, password, role_id) VALUES ( 'Admin User', 'admin@example.com', 'admin', 'password_hash', 1);
INSERT INTO app_user ( name, email, username, password, role_id) VALUES ( 'Regular User', 'user@example.com', 'user', 'password_hash', 2);

-- Insert into Industry table
INSERT INTO industry ( name) VALUES ( 'Software Industry');
INSERT INTO industry ( name) VALUES ('Data Science Industry');

-- Update Skill table with industry relationships
INSERT INTO skill ( name, industry_id) VALUES ( 'Java', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Python',1);
INSERT INTO skill ( name, industry_id) VALUES ( 'SQL', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'C++', 2);

-- Update Profession table with industry relationships
INSERT INTO profession ( name, industry_id) VALUES ('Software Developer', 1);
INSERT INTO profession ( name, industry_id) VALUES ('X Developer', 2);
INSERT INTO profession ( name, industry_id) VALUES ('Y Developer', 1);

-- RequiredLevel tablosuna veriler ekleyin
INSERT INTO required_level ( profession_id, skill_id, required_level) VALUES ( 1, 1, 4); -- Software Developer için Java seviyesi 4
INSERT INTO required_level ( profession_id, skill_id, required_level) VALUES ( 1, 2, 3); -- Software Developer için Python seviyesi 3
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES ( 2, 3, 4); -- Data Analyst için SQL seviyesi 4
INSERT INTO required_level ( profession_id, skill_id, required_level) VALUES ( 3, 3, 4); -- Data Analyst için SQL seviyesi 4

-- Survey tablosuna veri ekleyin
INSERT INTO survey ( title, admin_id) VALUES ( 'Developer Skills Survey', 1);

-- Question tablosuna veri ekleyin
INSERT INTO question ( text, skill_id, survey_id) VALUES ('How proficient are you in Java?', 1, 1);
INSERT INTO question ( text, skill_id, survey_id) VALUES ( 'Rate your Python skills', 2, 1);

-- Question tablosuna 3. ve 4. soruları ekleyin
INSERT INTO question ( text, skill_id, survey_id) VALUES ( 'How proficient are you in C#?', 3, 1);
INSERT INTO question (text, skill_id, survey_id) VALUES ( 'Rate your JavaScript skills', 4, 1);

-- Option tablosuna veriler ekleyin
INSERT INTO option (level, description, question_id) VALUES (1, 'Beginner', 1);
INSERT INTO option (level, description, question_id) VALUES (2, 'Intermediate', 1);
INSERT INTO option (level, description, question_id) VALUES (3, 'Advanced', 1);
INSERT INTO option (level, description, question_id) VALUES (4, 'Expert', 1);
INSERT INTO option (level, description, question_id) VALUES (5, 'Master', 1);

INSERT INTO option (level, description, question_id) VALUES (1, 'Beginner', 2);
INSERT INTO option (level, description, question_id) VALUES (2, 'Intermediate', 2);
INSERT INTO option (level, description, question_id) VALUES (3, 'Advanced', 2);
INSERT INTO option (level, description, question_id) VALUES (4, 'Expert', 2);
INSERT INTO option (level, description, question_id) VALUES (5, 'Master', 2);

INSERT INTO option (level, description, question_id) VALUES (1, 'Beginner', 3);
INSERT INTO option (level, description, question_id) VALUES (2, 'Intermediate', 3);
INSERT INTO option (level, description, question_id) VALUES (3, 'Advanced', 3);
INSERT INTO option (level, description, question_id) VALUES (4, 'Expert', 3);
INSERT INTO option (level, description, question_id) VALUES (5, 'Master', 3);

INSERT INTO option (level, description, question_id) VALUES (1, 'Beginner', 4);
INSERT INTO option (level, description, question_id) VALUES (2, 'Intermediate', 4);
INSERT INTO option (level, description, question_id) VALUES (3, 'Advanced', 4);
INSERT INTO option (level, description, question_id) VALUES (4, 'Expert', 4);
INSERT INTO option (level, description, question_id) VALUES (5, 'Master', 4);

INSERT INTO entered_survey (user_id, survey_id) VALUES
    ( 2, 1);
-- Insert sample responses
INSERT INTO response (user_id, survey_id, question_id, option_id, entered_level)
VALUES (2, 1, 1, 1, 2); -- Java Intermediate selected

INSERT INTO response (user_id, survey_id, question_id, option_id, entered_level)
VALUES (2, 1, 2, 2, 3); -- Python Advanced selected

INSERT INTO response (user_id, survey_id, question_id, option_id, entered_level)
VALUES (2, 1, 3, 3, 1); -- C# Beginner selected

INSERT INTO response (user_id, survey_id, question_id, option_id, entered_level)
VALUES (2, 1, 4, 4, 2); -- JavaScript Intermediate selected

-- /src/main/resources/db/migration/V5__Insert_Company_Data.sql

INSERT INTO company (name, description) VALUES
                                            ('None', 'I am not currently working'),
                                            ('Apple Inc.', 'Technology company that designs, develops, and sells consumer electronics'),
                                            ('Microsoft Corporation', 'Technology company specializing in software, cloud computing, and artificial intelligence'),
                                            ('Amazon.com Inc.', 'E-commerce and technology company focusing on e-commerce, cloud computing, and artificial intelligence'),
                                            ('Google LLC', 'Technology company specializing in internet-related services and artificial intelligence'),
                                            ('Meta Platforms Inc.', 'Technology company focusing on social networking and virtual reality'),
                                            ('Tesla, Inc.', 'Electric vehicle and clean energy company'),
                                            ('Samsung Electronics', 'Consumer electronics manufacturer'),
                                            ('Intel Corporation', 'Semiconductor chip manufacturer'),
                                            ('IBM Corporation', 'Technology company focusing on cloud computing and artificial intelligence'),
                                            ('Oracle Corporation', 'Database software and technology company'),
                                            ('Cisco Systems, Inc.', 'Networking hardware, software, and telecommunications company'),
                                            ('Adobe Inc.', 'Software company focusing on creativity and multimedia software'),
                                            ('Salesforce, Inc.', 'Cloud-based software company focusing on CRM'),
                                            ('VMware, Inc.', 'Cloud computing and virtualization technology company'),
                                            ('NVIDIA Corporation', 'Technology company focusing on graphics processing units and artificial intelligence');
