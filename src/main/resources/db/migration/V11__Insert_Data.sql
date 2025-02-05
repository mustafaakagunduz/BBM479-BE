-- Role tablosuna veriler ekleyin
INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

-- App_User tablosuna veriler ekleyin
INSERT INTO app_user ( name, email, username, password, role_id) VALUES ( 'Admin User', 'admin@example.com', 'admin', 'password_hash', 1);
INSERT INTO app_user ( name, email, username, password, role_id) VALUES ( 'Regular User', 'user@example.com', 'user', 'password_hash', 2);

-- Insert into Industry table
INSERT INTO industry ( name) VALUES ( 'IT');

-- Update Skill table with industry relationships
INSERT INTO skill ( name, industry_id) VALUES ( 'Analytical Thinking', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Business Plan Development',1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Change Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Configuration Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Critical Thinking', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Data Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Data Science', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Digital Transformation Strategy Planning', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Document Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Enterprise and Business Architecture', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Financial Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Information Security Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'IT Service Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Knowledge Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Leadership and Social Influence', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Machine Learning', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Marketing', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Networks and Cybersecurity', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Problem Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Project Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Requirements Definition and Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Resource Management and Operations', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Software Design', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Software Development', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Software Maintenance', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Systems Analysis and Design', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Talent Management', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'Software Verification and Validation', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'User Experience', 1);
INSERT INTO skill ( name, industry_id) VALUES ( 'User Support', 1);


-- Update Profession table with industry relationships
INSERT INTO profession (name, industry_id) VALUES
                                               ('AI and Big Data Engineers', 1),
                                               ('Blockchain Engineers', 1),
                                               ('Business Intelligence Analysts', 1),
                                               ('Database Architects', 1),
                                               ('Digital Forensics Analysts', 1),
                                               ('Digital Marketing and Strategy Specialists', 1),
                                               ('Digital Transformation Specialists', 1),
                                               ('FinTech Engineers', 1),
                                               ('Information Security Engineers', 1),
                                               ('ML-Ops Engineers', 1),
                                               ('Product Owners', 1),
                                               ('Robotics Engineers', 1),
                                               ('Robotics Technicians', 1),
                                               ('Business Analysts', 1),
                                               ('Database Specialists', 1),
                                               ('Enterprise Architecture Specialists', 1),
                                               ('Head of IT Departments', 1),
                                               ('Information Security Managers', 1),
                                               ('Network Administrators', 1),
                                               ('Network Specialists', 1),
                                               ('Project Managers', 1),
                                               ('Software Development Specialists', 1),
                                               ('Software Technologies Managers', 1),
                                               ('Software Testing Specialists', 1),
                                               ('Systems Administrators', 1),
                                               ('Systems Specialists', 1),
                                               ('Software Architectures', 1);


-- RequiredLevel tablosuna veriler ekleyin
-- AI and Big Data Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 2),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Machine Learning'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Resource Management and Operations'), 3),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'AI and Big Data Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

-- Blockchain Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 2),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Data Science'), 2),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 2),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'Blockchain Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
-- Business Intelligence Analysts
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 5),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Document Management'), 2),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 3),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 3),
((SELECT id FROM profession WHERE name = 'Business Intelligence Analysts'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 3);

INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
-- Database Architects
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 2),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Data Science'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Problem Management'), 4),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 3),
((SELECT id FROM profession WHERE name = 'Database Architects'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 3);

-- Digital Forensics Analysts
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 5),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Data Management'), 3),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Data Science'), 3),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Document Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Digital Forensics Analysts'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 3);

-- Digital Marketing and Strategy Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Data Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Data Science'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Document Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 3),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Financial Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Marketing'), 4),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Project Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'Digital Marketing and Strategy Specialists'), (SELECT id FROM skill WHERE name = 'User Experience'), 4);

--Digital Transformation Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Data Management'), 3),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Data Science'), 2),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Digital Transformation Strategy Planning'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Financial Management'), 2),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Problem Management'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Project Management'), 4),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Resource Management and Operations'), 3),
((SELECT id FROM profession WHERE name = 'Digital Transformation Specialists'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 4);

--FinTech Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 2),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Data Science'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Financial Management'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'FinTech Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

--Information Security Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Knowledge Management'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'Information Security Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

--ML-Ops Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 2),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Data Science'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 2),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Machine Learning'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'ML-Ops Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

--Product Owners
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Business Plan Development'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Document Management'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Problem Management'), 4),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'User Experience'), 3),
((SELECT id FROM profession WHERE name = 'Product Owners'), (SELECT id FROM skill WHERE name = 'User Support'), 3);

--Robotics Engineers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Data Management'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Data Science'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Machine Learning'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 2),
((SELECT id FROM profession WHERE name = 'Robotics Engineers'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 2);

--Robotics Technicians
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Robotics Technicians'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 2),
((SELECT id FROM profession WHERE name = 'Robotics Technicians'), (SELECT id FROM skill WHERE name = 'Change Management'), 2),
((SELECT id FROM profession WHERE name = 'Robotics Technicians'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Robotics Technicians'), (SELECT id FROM skill WHERE name = 'Problem Management'), 2),
((SELECT id FROM profession WHERE name = 'Robotics Technicians'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 3);

--Business Analysts
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Business Plan Development'), 4),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Software Design'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 3),
((SELECT id FROM profession WHERE name = 'Business Analysts'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 3);

-- Database Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Data Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Data Science'), 2),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Document Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Problem Management'), 2),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 3),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Software Design'), 2),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Software Development'), 2),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 2),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 4),
((SELECT id FROM profession WHERE name = 'Database Specialists'), (SELECT id FROM skill WHERE name = 'User Support'), 3);

-- Enterprise Architecture Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Change Management'), 4),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 5),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 3),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Project Management'), 3),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 4),
((SELECT id FROM profession WHERE name = 'Enterprise Architecture Specialists'), (SELECT id FROM skill WHERE name = 'User Support'), 3);

-- Head of IT Departments
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Business Plan Development'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Financial Management'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'IT Service Management'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Knowledge Management'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Project Management'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Resource Management and Operations'), 5),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 4),
((SELECT id FROM profession WHERE name = 'Head of IT Departments'), (SELECT id FROM skill WHERE name = 'Talent Management'), 5);

-- Information Security Managers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Change Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Financial Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 5),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Knowledge Management'), 5),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Project Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Systems Analysis and Design'), 4),
((SELECT id FROM profession WHERE name = 'Information Security Managers'), (SELECT id FROM skill WHERE name = 'Talent Management'), 4);

-- Network Administrators
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Change Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Financial Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Knowledge Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 5),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Problem Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Resource Management and Operations'), 4),
((SELECT id FROM profession WHERE name = 'Network Administrators'), (SELECT id FROM skill WHERE name = 'Talent Management'), 4);

-- Network Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 4),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Document Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'IT Service Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 4),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Problem Management'), 2),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 3),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 4),
((SELECT id FROM profession WHERE name = 'Network Specialists'), (SELECT id FROM skill WHERE name = 'User Support'), 3);

-- Project Managers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Business Plan Development'), 4),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Change Management'), 3),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 5),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Document Management'), 3),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 4),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'IT Service Management'), 4),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Leadership and Social Influence'), 3),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Problem Management'), 4),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Project Management'), 5),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 3),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Resource Management and Operations'), 4),
((SELECT id FROM profession WHERE name = 'Project Managers'), (SELECT id FROM skill WHERE name = 'Talent Management'), 4);

-- Software Development Specialists
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 3),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 3),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Document Management'), 3),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 2),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Networks and Cybersecurity'), 2),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Problem Management'), 2),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Requirements Definition and Management'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Software Design'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Software Development'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Software Maintenance'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'Software Verification and Validation'), 4),
 ((SELECT id FROM profession WHERE name = 'Software Development Specialists'), (SELECT id FROM skill WHERE name = 'User Support'), 3);

 -- Software Technologies Managers
INSERT INTO required_level (profession_id, skill_id, required_level) VALUES
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Analytical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Business Plan Development'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Change Management'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Configuration Management'), 3),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Critical Thinking'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Data Management'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Document Management'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Enterprise and Business Architecture'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Financial Management'), 5),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Information Security Management'), 4),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'IT Service Management'), 5),
((SELECT id FROM profession WHERE name = 'Software Technologies Managers'), (SELECT id FROM skill WHERE name = 'Knowledge Management'), 5);



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
