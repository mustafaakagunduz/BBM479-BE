-- Role data
INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('USER');

-- Company data
INSERT INTO company (name, description) VALUES
                                            ('Tech Solutions Inc.', 'Leading technology solutions provider'),
                                            ('HR Innovations', 'Human Resources consulting firm'),
                                            ('Digital Dynamics', 'Digital transformation company');

-- Admin user with hashed password (password123)
INSERT INTO app_user (name, email, username, password, role_id, company_id, email_verified) VALUES
                                                                                                ('Admin User', 'admin@example.com', 'admin', '$2a$10$rBV7Sjz9h9ZNSPxVbZzX6Oq7LtgNT0WJPZqYwxBL.qKtXkQ9T5DXe', 1, 1, true),
                                                                                                ('Test User', 'user@example.com', 'testuser', '$2a$10$rBV7Sjz9h9ZNSPxVbZzX6Oq7LtgNT0WJPZqYwxBL.qKtXkQ9T5DXe', 2, 1, true);

-- Industry data
INSERT INTO industry (name) VALUES
                                ('Software Development'),
                                ('Data Science'),
                                ('Project Management'),
                                ('Digital Marketing');

-- Skill data
INSERT INTO skill (name, industry_id) VALUES
-- Software Development Skills
('Java Programming', 1),
('Python Programming', 1),
('Web Development', 1),
('Database Design', 1),

-- Data Science Skills
('Machine Learning', 2),
('Statistical Analysis', 2),
('Data Visualization', 2),
('Big Data Technologies', 2),

-- Project Management Skills
('Agile Methodologies', 3),
('Risk Management', 3),
('Team Leadership', 3),
('Stakeholder Management', 3),

-- Digital Marketing Skills
('SEO', 4),
('Social Media Marketing', 4),
('Content Strategy', 4),
('Analytics', 4);

-- Profession data
INSERT INTO profession (name, industry_id) VALUES
                                               ('Software Engineer', 1),
                                               ('Data Scientist', 2),
                                               ('Project Manager', 3),
                                               ('Digital Marketing Manager', 4);

-- Link professions with required skills
INSERT INTO profession_skill (profession_id, skill_id) VALUES
-- Software Engineer skills
(1, 1), -- Java
(1, 2), -- Python
(1, 3), -- Web Development
(1, 4), -- Database Design

-- Data Scientist skills
(2, 2), -- Python
(2, 5), -- Machine Learning
(2, 6), -- Statistical Analysis
(2, 7), -- Data Visualization

-- Project Manager skills
(3, 9), -- Agile
(3, 10), -- Risk Management
(3, 11), -- Team Leadership
(3, 12), -- Stakeholder Management

-- Digital Marketing Manager skills
(4, 13), -- SEO
(4, 14), -- Social Media
(4, 15), -- Content Strategy
(4, 16); -- Analytics

-- Required skill levels for professions
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
-- Software Engineer required levels
(1, 1, 4), -- Java level 4
(1, 2, 3), -- Python level 3
(1, 3, 4), -- Web Development level 4
(1, 4, 3), -- Database Design level 3

-- Data Scientist required levels
(2, 2, 4), -- Python level 4
(2, 5, 4), -- Machine Learning level 4
(2, 6, 4), -- Statistical Analysis level 4
(2, 7, 3), -- Data Visualization level 3

-- Project Manager required levels
(3, 9, 4),  -- Agile level 4
(3, 10, 4), -- Risk Management level 4
(3, 11, 5), -- Team Leadership level 5
(3, 12, 4), -- Stakeholder Management level 4

-- Digital Marketing Manager required levels
(4, 13, 4), -- SEO level 4
(4, 14, 4), -- Social Media level 4
(4, 15, 4), -- Content Strategy level 4
(4, 16, 3); -- Analytics level 3

-- Correct sample data insertion
INSERT INTO survey (title, user_id) VALUES
                                        ('Software Engineering Skills Assessment', 1),
                                        ('Data Science Proficiency Test', 1);
INSERT INTO question (text, skill_id, survey_id) VALUES
                                                     ('How proficient are you in Java programming?', 1, 1),
                                                     ('Rate your experience with Python development', 2, 1),
                                                     ('Assess your web development skills', 3, 1),
                                                     ('How experienced are you with database design?', 4, 1);

-- Questions for Data Science Survey
INSERT INTO question (text, skill_id, survey_id) VALUES
                                                     ('Rate your machine learning expertise', 5, 2),
                                                     ('How proficient are you in statistical analysis?', 6, 2),
                                                     ('Assess your data visualization skills', 7, 2),
                                                     ('Rate your experience with big data technologies', 8, 2);

-- Options for all questions (standard Likert scale)
INSERT INTO option (level, description, question_id) VALUES
-- Options for Java question
(1, 'Beginner - Basic understanding of syntax', 1),
(2, 'Elementary - Can write simple programs', 1),
(3, 'Intermediate - Can develop moderate applications', 1),
(4, 'Advanced - Proficient in complex development', 1),
(5, 'Expert - Master level knowledge and experience', 1),

-- Options for Python question
(1, 'Beginner - Basic syntax knowledge', 2),
(2, 'Elementary - Can write basic scripts', 2),
(3, 'Intermediate - Can develop small applications', 2),
(4, 'Advanced - Proficient in Python development', 2),
(5, 'Expert - Master level Python developer', 2);

-- Add more options for other questions as needed...