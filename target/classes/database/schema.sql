-- ============================================================
--  SkillSnap Database Schema
--  Version 1.0
--  All tables designed for easy career expansion
-- ============================================================

DROP DATABASE IF EXISTS skillsnap;
CREATE DATABASE skillsnap;
USE skillsnap;

-- ============================================================
-- TABLE 1: Player
-- Stores every registered user
-- ============================================================
CREATE TABLE Player (
                        player_id       INT             PRIMARY KEY AUTO_INCREMENT,
                        username        VARCHAR(50)     NOT NULL UNIQUE,
                        password_hash   VARCHAR(255)    NOT NULL,
                        full_name       VARCHAR(100)    NOT NULL,
                        email           VARCHAR(100)    NOT NULL UNIQUE,
                        university      VARCHAR(100),
                        avatar_id       INT             DEFAULT 1,
                        total_xp        INT             DEFAULT 0,
                        level           INT             DEFAULT 1,
                        streak          INT             DEFAULT 0,
                        last_login      TIMESTAMP       NULL,
                        created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE 2: CareerPath
-- One row per career — adding new career = inserting one row
-- ============================================================
CREATE TABLE CareerPath (
                            career_id       INT             PRIMARY KEY AUTO_INCREMENT,
                            title           VARCHAR(100)    NOT NULL,
                            field           VARCHAR(50)     NOT NULL,
                            description     TEXT,
                            avg_salary_pkr  DECIMAL(12,2),
                            market_demand   DECIMAL(3,1),       -- score out of 10
                            icon_path       VARCHAR(255),       -- path to career icon image
                            is_active       BOOLEAN         DEFAULT TRUE,
                            created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- ── EXPANDED CAREER PATHS ────────────────────────────────────

-- TECH (continuing from career_id 7)
INSERT INTO CareerPath (title, field, description, avg_salary_pkr, market_demand, icon_path) VALUES
                                                                                                 ('Full-Stack Engineering',      'Tech',         'Build complete web applications — both frontend and backend — end to end.',                                           200000, 9.6, 'images/careers/fullstack.png'),
                                                                                                 ('Cloud & DevOps Engineering',  'Tech',         'Build and manage cloud infrastructure, automate deployments and keep systems running at scale.',                      220000, 9.4, 'images/careers/devops.png'),
                                                                                                 ('UI/UX Design',                'Design',       'Design beautiful, intuitive digital products that people actually enjoy using.',                                      130000, 8.9, 'images/careers/uiux.png'),
                                                                                                 ('Blockchain Development',      'Tech',         'Build decentralized applications, smart contracts and Web3 platforms.',                                               250000, 8.5, 'images/careers/blockchain.png'),

-- BUSINESS & FINANCE
                                                                                                 ('Chartered Accountant / ACCA', 'Finance',      'Master financial reporting, auditing and taxation — the highest earning path in Pakistani finance.',                  180000, 9.0, 'images/careers/ca.png'),
                                                                                                 ('Product Management',          'Management',   'Define what gets built and why — bridge between business goals and engineering teams.',                               200000, 9.2, 'images/careers/product.png'),
                                                                                                 ('Digital Marketing',           'Marketing',    'Drive growth through SEO, paid ads, social media and data-driven campaigns.',                                         120000, 8.7, 'images/careers/marketing.png'),
                                                                                                 ('Supply Chain Management',     'Operations',   'Manage the flow of goods from raw materials to customer delivery across complex global networks.',                    150000, 8.5, 'images/careers/supplychain.png'),
                                                                                                 ('HR Management',               'HR',           'Attract, develop and retain the people that make organizations successful.',                                          130000, 8.3, 'images/careers/hr.png'),
                                                                                                 ('Freelance Consulting',        'Business',     'Work independently with international clients, set your own rates and build a location-independent career.',          300000, 8.8, 'images/careers/freelance.png'),

-- HEALTHCARE
                                                                                                 ('Medical Specialist',          'Healthcare',   'Pursue advanced specialization in surgery, cardiology, radiology or other high-impact medical fields.',               500000, 9.5, 'images/careers/medspec.png'),
                                                                                                 ('Nursing & Midwifery',         'Healthcare',   'Provide essential patient care with high demand both locally and internationally.',                                   100000, 9.3, 'images/careers/nursing.png'),
                                                                                                 ('Pharmacy',                    'Healthcare',   'Ensure safe and effective use of medicines in hospitals, community pharmacies and pharmaceutical research.',           150000, 8.8, 'images/careers/pharmacy.png'),
                                                                                                 ('Allied Health',               'Healthcare',   'Support patient recovery as a physiotherapist, lab technician or diagnostic specialist.',                             110000, 8.6, 'images/careers/allied.png'),

-- ENGINEERING
                                                                                                 ('Electrical Engineering',      'Engineering',  'Design power systems, circuits and electrical infrastructure that keep the modern world running.',                   170000, 8.7, 'images/careers/electrical.png'),
                                                                                                 ('Civil Engineering',           'Engineering',  'Plan, design and oversee construction of roads, buildings, bridges and infrastructure.',                              160000, 8.5, 'images/careers/civil.png'),
                                                                                                 ('Renewable Energy Engineering','Engineering',  'Design and deploy solar and wind energy systems driving Pakistan and the world toward clean power.',                  190000, 9.1, 'images/careers/renewable.png');

-- ============================================================
-- TABLE 3: Skill
-- All skills in the system — reusable across careers
-- ============================================================
CREATE TABLE Skill (
                       skill_id        INT             PRIMARY KEY AUTO_INCREMENT,
                       name            VARCHAR(100)    NOT NULL UNIQUE,
                       category        VARCHAR(50)     NOT NULL,   -- Programming, Math, Design, etc
                       description     TEXT,
                       icon_path       VARCHAR(255)
);

-- ── NEW SKILLS FOR EXPANDED CAREERS ─────────────────────────
INSERT INTO Skill (name, category, description) VALUES

-- Tech skills
('Docker',              'DevOps',       'Container platform for packaging and deploying applications'),
('Kubernetes',          'DevOps',       'Container orchestration system for automated deployment'),
('AWS',                 'Cloud',        'Amazon Web Services — leading cloud platform'),
('Azure',               'Cloud',        'Microsoft cloud computing platform'),
('Terraform',           'DevOps',       'Infrastructure as code tool for cloud provisioning'),
('CI/CD',               'DevOps',       'Continuous integration and deployment pipelines'),
('Figma',               'Design',       'Industry standard UI/UX design and prototyping tool'),
('Adobe XD',            'Design',       'UI/UX design tool for web and mobile'),
('User Research',       'Design',       'Methods for understanding user needs and behaviors'),
('Prototyping',         'Design',       'Building interactive mockups to test design ideas'),
('Solidity',            'Blockchain',   'Smart contract programming language for Ethereum'),
('Blockchain',          'Blockchain',   'Distributed ledger technology and consensus mechanisms'),
('Web3',                'Blockchain',   'Decentralized internet protocols and dApps'),
('Smart Contracts',     'Blockchain',   'Self-executing contracts stored on blockchain'),

-- Business & Finance
('Financial Reporting',     'Finance',      'Preparing balance sheets, income statements, audits'),
('Taxation',                'Finance',      'Tax laws, filing, compliance and optimization'),
('Auditing',                'Finance',      'Examining financial records for accuracy and compliance'),
('IFRS',                    'Finance',      'International Financial Reporting Standards'),
('Product Strategy',        'Management',   'Defining product vision, roadmap and market fit'),
('Agile/Scrum',             'Management',   'Iterative project management frameworks'),
('Market Research',         'Marketing',    'Collecting and analyzing data about market and customers'),
('SEO',                     'Marketing',    'Search engine optimization for organic traffic growth'),
('Google Analytics',        'Marketing',    'Web analytics platform for tracking user behavior'),
('Digital Advertising',     'Marketing',    'Paid campaigns on Google, Facebook, Instagram etc'),
('Supply Chain Management', 'Operations',   'End-to-end flow of goods from supplier to customer'),
('Procurement',             'Operations',   'Sourcing, negotiating and purchasing goods and services'),
('ERP Systems',             'Operations',   'Enterprise resource planning software like SAP, Oracle'),
('HR Management',           'HR',           'Recruitment, onboarding, performance and retention'),
('Labor Law',               'HR',           'Employment regulations, contracts and compliance'),
('Talent Acquisition',      'HR',           'Sourcing, interviewing and hiring top candidates'),
('Freelancing Platforms',   'Business',     'Upwork, Fiverr, Toptal — finding international clients'),
('Client Communication',    'Business',     'Managing international client expectations and delivery'),
('Proposal Writing',        'Business',     'Writing winning project proposals and contracts'),

-- Healthcare
('Anatomy',                 'Medicine',     'Structure of the human body and its systems'),
('Pharmacology',            'Medicine',     'Study of drugs, their effects and mechanisms'),
('Clinical Skills',         'Medicine',     'Patient examination, diagnosis and treatment procedures'),
('Surgery',                 'Medicine',     'Surgical techniques, procedures and post-operative care'),
('Cardiology',              'Medicine',     'Heart and cardiovascular system diagnosis and treatment'),
('Patient Care',            'Medicine',     'Nursing, monitoring vitals, wound care and patient support'),
('Medical Ethics',          'Medicine',     'Ethical principles governing healthcare practice'),
('Drug Dispensing',         'Pharmacy',     'Accurate preparation and dispensing of medications'),
('Pharmaceutical Chemistry','Pharmacy',     'Chemical properties and synthesis of drug compounds'),
('Physiotherapy',           'Allied Health','Rehabilitation exercises and physical therapy techniques'),
('Lab Testing',             'Allied Health','Conducting diagnostic tests — blood, urine, imaging'),

-- Engineering
('Power Electronics',       'Electrical',   'Conversion and control of electrical power'),
('Circuit Design',          'Electrical',   'Designing and analyzing electronic circuits'),
('AutoCAD',                 'Engineering',  'Computer-aided design software for technical drawings'),
('Structural Analysis',     'Civil',        'Calculating loads, stresses and material requirements'),
('Project Management',      'Engineering',  'Planning, executing and closing engineering projects'),
('Solar Energy Systems',    'Energy',       'Design and installation of photovoltaic solar systems'),
('Wind Energy',             'Energy',       'Wind turbine technology and energy generation'),
('ETAP',                    'Electrical',   'Power system analysis and simulation software'),
('MATLAB',                  'Engineering',  'Numerical computing and simulation environment');

-- ============================================================
-- TABLE 4: CareerSkill
-- Many-to-many: which skills belong to which career
-- Adding new career's skills = inserting rows here only
-- ============================================================
CREATE TABLE CareerSkill (
                             career_id       INT             NOT NULL,
                             skill_id        INT             NOT NULL,
                             importance      INT             NOT NULL    CHECK (importance BETWEEN 1 AND 5),
                             is_core         BOOLEAN         DEFAULT TRUE,
                             PRIMARY KEY (career_id, skill_id),
                             FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                                 ON DELETE CASCADE,
                             FOREIGN KEY (skill_id)  REFERENCES Skill(skill_id)
                                 ON DELETE CASCADE
);

-- ── CAREER SKILLS — NEW CAREERS ─────────────────────────────
-- NOTE: skill_ids continue from your existing skills
-- New skills start at skill_id 21 (adjust if yours differ)
-- Run: SELECT skill_id, name FROM Skill; to confirm IDs

-- Full-Stack Engineering (career_id = 7)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (7, 3,  5, TRUE),   -- JavaScript
                                                                       (7, 10, 5, TRUE),   -- HTML/CSS
                                                                       (7, 11, 4, TRUE),   -- React
                                                                       (7, 4,  4, TRUE),   -- SQL
                                                                       (7, 19, 4, TRUE),   -- Git
                                                                       (7, 16, 4, TRUE),   -- Problem Solving
                                                                       (7, 2,  3, FALSE);  -- Java

-- Cloud & DevOps (career_id = 8)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (8, 15, 5, TRUE),   -- Cloud Computing
                                                                       (8, 21, 5, TRUE),   -- Docker
                                                                       (8, 22, 5, TRUE),   -- Kubernetes
                                                                       (8, 23, 5, TRUE),   -- AWS
                                                                       (8, 25, 4, TRUE),   -- Terraform
                                                                       (8, 26, 4, TRUE),   -- CI/CD
                                                                       (8, 8,  3, FALSE);  -- Linux

-- UI/UX Design (career_id = 9)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (9, 17, 5, TRUE),   -- UI/UX Design
                                                                       (9, 27, 5, TRUE),   -- Figma
                                                                       (9, 29, 5, TRUE),   -- User Research
                                                                       (9, 30, 4, TRUE),   -- Prototyping
                                                                       (9, 28, 3, FALSE),  -- Adobe XD
                                                                       (9, 10, 3, FALSE);  -- HTML/CSS

-- Blockchain Development (career_id = 10)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (10, 31, 5, TRUE),  -- Solidity
                                                                       (10, 32, 5, TRUE),  -- Blockchain
                                                                       (10, 33, 4, TRUE),  -- Web3
                                                                       (10, 34, 5, TRUE),  -- Smart Contracts
                                                                       (10, 1,  3, FALSE), -- Python
                                                                       (10, 3,  3, FALSE); -- JavaScript

-- Chartered Accountant (career_id = 11)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (11, 35, 5, TRUE),  -- Financial Reporting
                                                                       (11, 36, 5, TRUE),  -- Taxation
                                                                       (11, 37, 5, TRUE),  -- Auditing
                                                                       (11, 38, 4, TRUE),  -- IFRS
                                                                       (11, 4,  3, FALSE), -- SQL
                                                                       (11, 5,  3, FALSE); -- Statistics

-- Product Management (career_id = 12)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (12, 39, 5, TRUE),  -- Product Strategy
                                                                       (12, 40, 5, TRUE),  -- Agile/Scrum
                                                                       (12, 16, 4, TRUE),  -- Problem Solving
                                                                       (12, 41, 4, TRUE),  -- Market Research
                                                                       (12, 5,  3, FALSE), -- Statistics
                                                                       (12, 19, 3, FALSE); -- Git

-- Digital Marketing (career_id = 13)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (13, 42, 5, TRUE),  -- SEO
                                                                       (13, 43, 4, TRUE),  -- Google Analytics
                                                                       (13, 44, 4, TRUE),  -- Digital Advertising
                                                                       (13, 41, 5, TRUE),  -- Market Research
                                                                       (13, 14, 3, FALSE), -- Data Visualization
                                                                       (13, 3,  2, FALSE); -- JavaScript

-- Supply Chain (career_id = 14)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (14, 45, 5, TRUE),  -- Supply Chain Management
                                                                       (14, 46, 5, TRUE),  -- Procurement
                                                                       (14, 47, 4, TRUE),  -- ERP Systems
                                                                       (14, 4,  3, FALSE), -- SQL
                                                                       (14, 5,  3, FALSE), -- Statistics
                                                                       (14, 16, 4, TRUE);  -- Problem Solving

-- HR Management (career_id = 15)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (15, 48, 5, TRUE),  -- HR Management
                                                                       (15, 49, 4, TRUE),  -- Labor Law
                                                                       (15, 50, 5, TRUE),  -- Talent Acquisition
                                                                       (15, 40, 3, FALSE), -- Agile/Scrum
                                                                       (15, 41, 3, FALSE), -- Market Research
                                                                       (15, 16, 4, TRUE);  -- Problem Solving

-- Freelance Consulting (career_id = 16)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (16, 51, 5, TRUE),  -- Freelancing Platforms
                                                                       (16, 52, 5, TRUE),  -- Client Communication
                                                                       (16, 53, 4, TRUE),  -- Proposal Writing
                                                                       (16, 16, 5, TRUE),  -- Problem Solving
                                                                       (16, 19, 3, FALSE), -- Git
                                                                       (16, 3,  3, FALSE); -- JavaScript

-- Medical Specialist (career_id = 17)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (17, 54, 5, TRUE),  -- Anatomy
                                                                       (17, 55, 5, TRUE),  -- Pharmacology
                                                                       (17, 56, 5, TRUE),  -- Clinical Skills
                                                                       (17, 57, 4, TRUE),  -- Surgery
                                                                       (17, 58, 4, TRUE),  -- Cardiology
                                                                       (17, 60, 5, TRUE);  -- Medical Ethics

-- Nursing (career_id = 18)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (18, 54, 4, TRUE),  -- Anatomy
                                                                       (18, 59, 5, TRUE),  -- Patient Care
                                                                       (18, 55, 4, TRUE),  -- Pharmacology
                                                                       (18, 56, 5, TRUE),  -- Clinical Skills
                                                                       (18, 60, 4, TRUE),  -- Medical Ethics
                                                                       (18, 16, 3, FALSE); -- Problem Solving

-- Pharmacy (career_id = 19)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (19, 55, 5, TRUE),  -- Pharmacology
                                                                       (19, 61, 5, TRUE),  -- Drug Dispensing
                                                                       (19, 62, 4, TRUE),  -- Pharmaceutical Chemistry
                                                                       (19, 54, 4, TRUE),  -- Anatomy
                                                                       (19, 60, 4, TRUE),  -- Medical Ethics
                                                                       (19, 5,  3, FALSE); -- Statistics

-- Allied Health (career_id = 20)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (20, 63, 5, TRUE),  -- Physiotherapy
                                                                       (20, 64, 4, TRUE),  -- Lab Testing
                                                                       (20, 54, 4, TRUE),  -- Anatomy
                                                                       (20, 59, 5, TRUE),  -- Patient Care
                                                                       (20, 56, 4, TRUE),  -- Clinical Skills
                                                                       (20, 60, 3, FALSE); -- Medical Ethics

-- Electrical Engineering (career_id = 21)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (21, 65, 5, TRUE),  -- Power Electronics
                                                                       (21, 66, 5, TRUE),  -- Circuit Design
                                                                       (21, 70, 4, TRUE),  -- ETAP
                                                                       (21, 71, 4, TRUE),  -- MATLAB
                                                                       (21, 68, 3, FALSE), -- AutoCAD
                                                                       (21, 16, 4, TRUE);  -- Problem Solving

-- Civil Engineering (career_id = 22)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (22, 68, 5, TRUE),  -- AutoCAD
                                                                       (22, 69, 5, TRUE),  -- Structural Analysis
                                                                       (22, 70, 3, FALSE), -- Project Management
                                                                       (22, 16, 4, TRUE),  -- Problem Solving
                                                                       (22, 5,  3, FALSE), -- Statistics
                                                                       (22, 71, 3, FALSE); -- MATLAB

-- Renewable Energy (career_id = 23)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
                                                                       (23, 72, 5, TRUE),  -- Solar Energy Systems
                                                                       (23, 73, 4, TRUE),  -- Wind Energy
                                                                       (23, 65, 4, TRUE),  -- Power Electronics
                                                                       (23, 71, 4, TRUE),  -- MATLAB
                                                                       (23, 68, 3, FALSE), -- AutoCAD
                                                                       (23, 16, 4, TRUE);  -- Problem Solving

-- ============================================================
-- TABLE 5: MiniGame
-- Each career has multiple games — extensible by design
-- Adding new career's games = inserting rows here only
-- ============================================================
CREATE TABLE MiniGame (
                          game_id         INT             PRIMARY KEY AUTO_INCREMENT,
                          career_id       INT             NOT NULL,
                          title           VARCHAR(100)    NOT NULL,
                          description     TEXT,
                          instructions    TEXT,
                          difficulty      ENUM('Easy', 'Medium', 'Hard')  NOT NULL,
                          max_score       INT             NOT NULL    DEFAULT 100,
                          time_limit_sec  INT             NOT NULL    DEFAULT 60,
                          xp_reward       INT             NOT NULL    DEFAULT 50,
                          game_type       VARCHAR(50)     NOT NULL,   -- pattern, logic, bugfinder, etc
                          is_active       BOOLEAN         DEFAULT TRUE,
                          FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                              ON DELETE CASCADE
);

-- ── MINI GAMES — NEW CAREERS ─────────────────────────────────

-- Full-Stack Engineering (career_id = 7)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
                                                                                                                                    (7, 'Stack Matcher',        'Match frontend components to their backend counterparts',  'Drag each frontend element to its matching backend service. Every correct pair earns points.',                          'Easy',   100, 45,  50,  'matching'),
                                                                                                                                    (7, 'API Builder',          'Arrange REST API endpoints in correct RESTful structure',  'Given a list of API routes, identify which ones follow REST conventions and which ones violate them.',                 'Medium', 150, 60,  75,  'logic_puzzle'),
                                                                                                                                    (7, 'Full Stack Debug',     'Fix both frontend and backend bugs in one system',         'Spot all the bugs across the frontend HTML and backend logic. Every correct fix scores. Wrong clicks deduct.',         'Hard',   200, 75,  100, 'bugfinder'),

-- Cloud & DevOps (career_id = 8)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (8, 'Pipeline Architect',   'Arrange CI/CD pipeline stages in the correct order',      'Drag the deployment stages into the correct sequence. A broken pipeline costs you points.',                            'Easy',   100, 45,  50,  'logic_puzzle'),
    (8, 'Cloud Cost Optimizer', 'Pick the most cost-efficient cloud configuration',        'Given a workload description, select the cloud setup that meets requirements at the lowest cost.',                      'Medium', 150, 60,  75,  'decision'),
    (8, 'Infra Under Attack',   'Identify the misconfiguration causing the security breach','Scan the infrastructure diagram and click on the misconfigured component causing the vulnerability.',                  'Hard',   200, 50,  100, 'bugfinder'),

-- UI/UX Design (career_id = 9)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (9, 'Heatmap Reader',       'Identify where users will click on this interface',        'Given a UI design, predict the click heatmap by selecting the areas users are most likely to interact with.',          'Easy',   100, 60,  50,  'prediction'),
    (9, 'Accessibility Check',  'Find the accessibility violations in this design',         'Examine the design and identify every accessibility problem — contrast, font size, missing labels.',                   'Medium', 150, 60,  75,  'audit'),
    (9, 'Wireframe Match',      'Match the wireframe to its final high-fidelity design',    'Look at the wireframe and select which finished design correctly implements it.',                                       'Hard',   200, 45,  100, 'matching'),

-- Blockchain Development (career_id = 10)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (10,'Chain Tracer',         'Trace a transaction through the blockchain',               'Follow the transaction from wallet to confirmation. Identify which block it lands in.',                                 'Easy',   100, 60,  50,  'trace'),
    (10,'Smart Contract Audit', 'Find the vulnerability in this smart contract',            'Read the Solidity code carefully and click on the line containing the security flaw.',                                  'Medium', 150, 75,  75,  'bugfinder'),
    (10,'Consensus Builder',    'Choose the right consensus mechanism for each scenario',   'Given a blockchain use case, select which consensus mechanism (PoW, PoS, DPoS) fits best.',                            'Hard',   200, 60,  100, 'decision'),

-- Chartered Accountant (career_id = 11)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (11,'Balance Checker',      'Identify which financial statement has an error',          'Examine the balance sheet and spot the entry that breaks the accounting equation.',                                     'Easy',   100, 60,  50,  'audit'),
    (11,'Tax Calculator',       'Calculate the correct tax liability for a given scenario', 'Read the income details and select the correct tax bracket and total liability.',                                       'Medium', 150, 75,  75,  'calculation'),
    (11,'Audit Trail',          'Trace the discrepancy in the financial records',           'Follow the money through multiple journal entries and identify where the fraud occurred.',                               'Hard',   200, 90,  100, 'trace'),

-- Product Management (career_id = 12)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (12,'Priority Matrix',      'Place features in the correct Eisenhower quadrant',        'Drag each product feature into the correct urgent/important quadrant. Fast and accurate earns max points.',            'Easy',   100, 50,  50,  'sorting'),
    (12,'User Story Writer',    'Identify the best-written user story from the options',    'Select the user story that correctly follows the As a... I want... So that... format with clear acceptance criteria.', 'Medium', 150, 45,  75,  'decision'),
    (12,'Roadmap Builder',      'Arrange product milestones in strategic sequence',         'Given the features and constraints, arrange the roadmap so each quarter delivers maximum business value.',             'Hard',   200, 75,  100, 'logic_puzzle'),

-- Digital Marketing (career_id = 13)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (13,'SEO Detector',         'Identify the on-page SEO issues on this webpage',          'Scan the webpage content and click on every SEO problem you find — missing tags, keyword stuffing, broken links.',    'Easy',   100, 60,  50,  'audit'),
    (13,'Ad Budget Allocator',  'Distribute the marketing budget for maximum ROI',          'Given campaign performance data, reallocate the budget across channels to maximize return on investment.',             'Medium', 150, 60,  75,  'decision'),
    (13,'Analytics Interpreter','Read the GA4 dashboard and answer the questions',          'Given the analytics screenshot, answer questions about traffic sources, bounce rate and conversion funnels.',          'Hard',   200, 75,  100, 'data_reading'),

-- Supply Chain (career_id = 14)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (14,'Supply Chain Mapper',  'Identify the bottleneck in this supply chain',             'Examine the supply chain diagram and click on the node causing the most delay.',                                        'Easy',   100, 60,  50,  'analysis'),
    (14,'Procurement Negotiator','Choose the best supplier offer for the given criteria',   'Compare three supplier proposals and select the one that best meets quality, cost and delivery requirements.',         'Medium', 150, 75,  75,  'decision'),
    (14,'Inventory Optimizer',  'Calculate the optimal reorder point for this product',     'Given demand data and lead times, calculate when to reorder to avoid stockout without overstocking.',                  'Hard',   200, 90,  100, 'calculation'),

-- HR Management (career_id = 15)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (15,'CV Screener',          'Identify the strongest candidate from the CVs shown',      'Read three candidate profiles and select the one best matching the job description provided.',                          'Easy',   100, 60,  50,  'decision'),
    (15,'Policy Checker',       'Spot the labor law violation in this HR scenario',         'Read the workplace scenario and identify which action violates Pakistan labor law.',                                    'Medium', 150, 60,  75,  'audit'),
    (15,'Conflict Resolver',    'Choose the best HR response to this workplace conflict',   'Given a workplace conflict scenario, select the resolution approach that best follows HR best practices.',             'Hard',   200, 75,  100, 'decision'),

-- Freelance Consulting (career_id = 16)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (16,'Proposal Picker',      'Identify the winning freelance proposal from the samples', 'Read three Upwork proposals and select the one most likely to win the client based on best practices.',               'Easy',   100, 60,  50,  'decision'),
    (16,'Rate Calculator',      'Set the correct hourly rate for this project and profile', 'Given the project scope and the freelancer profile, calculate a competitive yet profitable hourly rate.',             'Medium', 150, 75,  75,  'calculation'),
    (16,'Client Red Flags',     'Spot the red flags in this client brief before accepting', 'Read the project brief and identify all the warning signs that suggest a problematic client.',                         'Hard',   200, 60,  100, 'audit'),

-- Medical Specialist (career_id = 17)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (17,'Symptom Sorter',       'Match symptoms to their most likely diagnosis',            'For each set of symptoms shown, select the correct diagnosis from the options provided.',                               'Easy',   100, 60,  50,  'matching'),
    (17,'Vitals Analyzer',      'Identify the abnormal reading in the patient vitals',      'Examine the patient vitals chart and click on the reading that falls outside normal clinical range.',                  'Medium', 150, 45,  75,  'analysis'),
    (17,'Ethics Dilemma',       'Choose the most ethical response to this medical scenario','Given a clinical ethics scenario, select the response that best follows the four principles of medical ethics.',       'Hard',   200, 90,  100, 'decision'),

-- Nursing (career_id = 18)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (18,'Patient Prioritizer',  'Triage patients in order of urgency',                     'Rank the four patients from most to least urgent based on their presenting symptoms.',                                  'Easy',   100, 60,  50,  'sorting'),
    (18,'Medication Safety',    'Identify the medication error before it reaches the patient','Spot the dosage or drug error in the medication order before confirming administration.',                            'Medium', 150, 45,  75,  'audit'),
    (18,'Care Plan Builder',    'Select the correct nursing interventions for this patient','Given the patient diagnosis, choose the interventions that form a correct and complete care plan.',                     'Hard',   200, 75,  100, 'decision'),

-- Pharmacy (career_id = 19)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (19,'Drug Interaction Check','Identify the dangerous drug combination',                 'Given two medications, determine whether the combination is safe or causes a dangerous interaction.',                   'Easy',   100, 45,  50,  'decision'),
    (19,'Dosage Calculator',    'Calculate the correct dosage for this patient',            'Given patient weight, age and condition, calculate the correct drug dosage following the formula shown.',              'Medium', 150, 60,  75,  'calculation'),
    (19,'Prescription Reader',  'Identify the error in this prescription',                  'Read the handwritten prescription carefully and click on the element that contains an error.',                         'Hard',   200, 60,  100, 'audit'),

-- Allied Health (career_id = 20)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (20,'Exercise Matcher',     'Match rehabilitation exercises to their target conditions','Drag each physiotherapy exercise to the condition it is most commonly used to treat.',                                 'Easy',   100, 60,  50,  'matching'),
    (20,'Lab Result Reader',    'Identify the abnormal lab result and what it indicates',   'Read the lab report and click on the value that is outside the normal reference range.',                               'Medium', 150, 45,  75,  'analysis'),
    (20,'Treatment Planner',    'Build the correct rehabilitation plan for this patient',   'Given a patient diagnosis, arrange the treatment stages in the correct clinical sequence.',                            'Hard',   200, 75,  100, 'logic_puzzle'),

-- Electrical Engineering (career_id = 21)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (21,'Circuit Solver',       'Identify the fault in this electrical circuit',            'Examine the circuit diagram and click on the component that is causing the fault.',                                    'Easy',   100, 60,  50,  'analysis'),
    (21,'Power Calculator',     'Calculate the power consumption for this load scenario',   'Given voltage, current and power factor values, calculate the total real power consumed by the system.',              'Medium', 150, 75,  75,  'calculation'),
    (21,'Protection Selector',  'Choose the correct protection relay for this power system','Given a power system configuration, select the relay type and setting that provides correct protection.',             'Hard',   200, 90,  100, 'decision'),

-- Civil Engineering (career_id = 22)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (22,'Load Calculator',      'Calculate the structural load for this building scenario', 'Given the building dimensions and materials, calculate the total dead and live load on the foundation.',              'Easy',   100, 75,  50,  'calculation'),
    (22,'Material Selector',    'Choose the right material for this construction scenario', 'Given the environmental conditions and structural requirements, select the optimal building material.',                'Medium', 150, 60,  75,  'decision'),
    (22,'Site Safety Audit',    'Identify the safety violations on this construction site', 'Examine the construction site image and click on every safety violation you can spot.',                               'Hard',   200, 60,  100, 'audit'),

-- Renewable Energy (career_id = 23)
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
    (23,'Solar Panel Placer',   'Position solar panels for maximum energy generation',      'Drag and orient the solar panels on the building rooftop to maximize annual energy yield.',                            'Easy',   100, 60,  50,  'spatial'),
    (23,'Energy Audit',         'Identify the biggest energy waste in this building',       'Examine the building energy report and click on the system responsible for the highest unnecessary consumption.',      'Medium', 150, 60,  75,  'analysis'),
    (23,'Grid Calculator',      'Size the solar system correctly for this energy demand',   'Given monthly consumption data and solar irradiance, calculate the correct panel count and battery capacity.',        'Hard',   200, 90,  100, 'calculation');

-- ============================================================
-- TABLE 6: GameSession
-- Every single time a player plays any game
-- ============================================================
CREATE TABLE GameSession (
                             session_id      INT             PRIMARY KEY AUTO_INCREMENT,
                             player_id       INT             NOT NULL,
                             game_id         INT             NOT NULL,
                             score           INT             NOT NULL    DEFAULT 0,
                             max_score       INT             NOT NULL    DEFAULT 100,
                             time_taken_sec  INT,
                             completed       BOOLEAN         DEFAULT FALSE,
                             xp_earned       INT             DEFAULT 0,
                             played_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (player_id) REFERENCES Player(player_id)
                                 ON DELETE CASCADE,
                             FOREIGN KEY (game_id)   REFERENCES MiniGame(game_id)
                                 ON DELETE CASCADE
);

-- ============================================================
-- TABLE 7: CareerSuggestion
-- Stores every suggestion generated for a player
-- ============================================================
CREATE TABLE CareerSuggestion (
                                  suggestion_id   INT             PRIMARY KEY AUTO_INCREMENT,
                                  player_id       INT             NOT NULL,
                                  career_id       INT             NOT NULL,
                                  match_score     DECIMAL(5,2)    NOT NULL,   -- 0.00 to 100.00
                                  rank_position   INT             NOT NULL,   -- 1 = top match
                                  generated_at    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (player_id) REFERENCES Player(player_id)
                                      ON DELETE CASCADE,
                                  FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                                      ON DELETE CASCADE
);

-- ============================================================
-- TABLE 8: RoadmapStep
-- Step by step career roadmap — add new career's steps = insert rows
-- ============================================================
CREATE TABLE RoadmapStep (
                             step_id         INT             PRIMARY KEY AUTO_INCREMENT,
                             career_id       INT             NOT NULL,
                             step_number     INT             NOT NULL,
                             title           VARCHAR(150)    NOT NULL,
                             description     TEXT,
                             duration        VARCHAR(50),                -- "2 weeks", "1 month"
                             resource_url    VARCHAR(500),               -- link to course/tutorial
                             resource_title  VARCHAR(150),               -- display name for link
                             UNIQUE (career_id, step_number),
                             FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                                 ON DELETE CASCADE
);

-- ── ROADMAP STEPS — NEW CAREERS ──────────────────────────────

-- Full-Stack Engineering (career_id = 7)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (7,1,'HTML, CSS & JavaScript','Master the three core web technologies before any framework',        '4 weeks', 'https://javascript.info/',          'The Modern JS Tutorial'),
                                                                                                                 (7,2,'React Frontend',        'Components, hooks, state management and routing',                   '5 weeks', 'https://react.dev/learn',           'React Official Docs'),
                                                                                                                 (7,3,'Node.js Backend',       'REST APIs, Express, middleware, authentication with JWT',           '4 weeks', 'https://nodejs.org/en/learn',       'Node.js Docs'),
                                                                                                                 (7,4,'Databases',             'SQL with PostgreSQL + NoSQL with MongoDB',                          '3 weeks', 'https://www.postgresql.org/docs/',  'PostgreSQL Docs'),
                                                                                                                 (7,5,'Git & Deployment',      'Version control, GitHub, deploy on Vercel and Railway',             '2 weeks', 'https://learngitbranching.js.org/', 'Learn Git Branching'),
                                                                                                                 (7,6,'Build 3 Full Projects', 'E-commerce store, social app, SaaS dashboard for portfolio',        '8 weeks', 'https://www.frontendmentor.io/',    'Frontend Mentor');

-- Cloud & DevOps (career_id = 8)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (8,1,'Linux & Networking',    'Command line, SSH, firewalls, TCP/IP fundamentals',                 '3 weeks', 'https://linuxjourney.com/',         'Linux Journey'),
                                                                                                                 (8,2,'Git & CI/CD',           'GitHub Actions, automated testing, continuous deployment pipelines', '3 weeks', 'https://docs.github.com/en/actions','GitHub Actions Docs'),
                                                                                                                 (8,3,'Docker & Containers',   'Containerize applications, Docker Compose, image management',       '4 weeks', 'https://docs.docker.com/get-started/','Docker Get Started'),
                                                                                                                 (8,4,'Kubernetes',            'Container orchestration, pods, services, deployments',              '5 weeks', 'https://kubernetes.io/docs/tutorials/','Kubernetes Tutorials'),
                                                                                                                 (8,5,'AWS Cloud',             'EC2, S3, RDS, Lambda, IAM — core AWS services',                    '6 weeks', 'https://aws.amazon.com/training/',  'AWS Training'),
                                                                                                                 (8,6,'AWS Solutions Architect','Prepare and sit the AWS SAA-C03 certification exam',               '6 weeks', 'https://aws.amazon.com/certification/','AWS Certification');

-- UI/UX Design (career_id = 9)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (9,1,'Design Fundamentals',   'Color theory, typography, spacing, visual hierarchy',               '3 weeks', 'https://www.canva.com/learn/design-elements-principles/','Canva Design School'),
                                                                                                                 (9,2,'Figma Mastery',         'Components, auto-layout, prototyping, design systems',              '4 weeks', 'https://www.figma.com/resources/learn-design/','Figma Learn'),
                                                                                                                 (9,3,'User Research',         'Interviews, surveys, usability testing, affinity mapping',          '3 weeks', 'https://www.nngroup.com/articles/', 'Nielsen Norman Group'),
                                                                                                                 (9,4,'Interaction Design',    'Micro-interactions, animations, gesture-based design',              '3 weeks', 'https://www.interaction-design.org/','Interaction Design Foundation'),
                                                                                                                 (9,5,'Portfolio Building',    'Case studies, process documentation, Behance presence',             '4 weeks', 'https://www.behance.net/',          'Behance'),
                                                                                                                 (9,6,'UX Certification',      'Google UX Design Certificate on Coursera',                         '6 weeks', 'https://grow.google/certificates/ux-design/','Google UX Certificate');

-- Blockchain Development (career_id = 10)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (10,1,'Blockchain Fundamentals','How blockchains work, consensus, cryptographic hashing',          '3 weeks', 'https://bitcoin.org/bitcoin.pdf',   'Bitcoin Whitepaper'),
                                                                                                                 (10,2,'Solidity Programming',  'Smart contract language, variables, functions, events',            '5 weeks', 'https://docs.soliditylang.org/',    'Solidity Docs'),
                                                                                                                 (10,3,'Ethereum & Web3.js',    'Deploy to testnet, interact with contracts, MetaMask',             '4 weeks', 'https://web3js.readthedocs.io/',    'Web3.js Docs'),
                                                                                                                 (10,4,'DeFi & NFT Concepts',   'Decentralized finance protocols, token standards ERC20/ERC721',   '3 weeks', 'https://ethereum.org/en/developers/','Ethereum Dev Portal'),
                                                                                                                 (10,5,'Security & Auditing',   'Common smart contract vulnerabilities and how to prevent them',    '4 weeks', 'https://consensys.github.io/smart-contract-best-practices/','ConsenSys Security'),
                                                                                                                 (10,6,'Build & Deploy dApp',   'Full decentralized application with React frontend and Solidity',  '6 weeks', 'https://hardhat.org/tutorial',      'Hardhat Tutorial');

-- Chartered Accountant (career_id = 11)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (11,1,'Accounting Fundamentals','Double entry, journals, ledgers and trial balance',               '4 weeks', 'https://www.icap.org.pk/',          'ICAP Pakistan'),
                                                                                                                 (11,2,'Financial Reporting',   'Balance sheet, income statement, cash flow under IFRS',           '5 weeks', 'https://www.ifrs.org/issued-standards/','IFRS Foundation'),
                                                                                                                 (11,3,'Taxation',              'Income tax, sales tax, withholding tax under FBR rules',          '4 weeks', 'https://www.fbr.gov.pk/',           'FBR Pakistan'),
                                                                                                                 (11,4,'Auditing Standards',    'ISA standards, internal controls, audit evidence and reporting',   '4 weeks', 'https://www.iaasb.org/',            'IAASB Audit Standards'),
                                                                                                                 (11,5,'ACCA/CA Enrollment',    'Register with ICAP for CA or ACCA global qualification',          '1 week',  'https://www.accaglobal.com/',       'ACCA Global'),
                                                                                                                 (11,6,'Articleship / Training','3-year supervised practical training at a chartered firm',         '3 years', 'https://www.icap.org.pk/students/', 'ICAP Student Portal');

-- Product Management (career_id = 12)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (12,1,'PM Fundamentals',      'Product lifecycle, PRDs, user stories, success metrics',           '3 weeks', 'https://www.productplan.com/learn/', 'ProductPlan Learn'),
                                                                                                                 (12,2,'User Research',        'Interviews, surveys, persona creation, journey mapping',           '3 weeks', 'https://www.nngroup.com/',          'Nielsen Norman Group'),
                                                                                                                 (12,3,'Agile & Scrum',        'Sprints, standups, retrospectives, working with engineering',      '3 weeks', 'https://www.scrum.org/resources/what-is-scrum','Scrum.org'),
                                                                                                                 (12,4,'Data & Analytics',     'SQL basics, A/B testing, funnel analysis, defining KPIs',         '4 weeks', 'https://mixpanel.com/blog/',        'Mixpanel Blog'),
                                                                                                                 (12,5,'Build PM Portfolio',   'Document 2-3 product decisions with data, rationale and outcome',  '4 weeks', 'https://www.lennysnewsletter.com/', 'Lenny''s Newsletter'),
                                                                                                                 (12,6,'PM Certification',     'Pragmatic Institute PMC or Google PM Certificate',                 '6 weeks', 'https://grow.google/certificates/', 'Google Career Certificates');

-- Digital Marketing (career_id = 13)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (13,1,'Marketing Fundamentals','Consumer psychology, brand positioning, marketing mix',           '3 weeks', 'https://www.hubspot.com/marketing-statistics','HubSpot Marketing'),
                                                                                                                 (13,2,'SEO Mastery',          'Keyword research, on-page optimization, backlink building',        '4 weeks', 'https://moz.com/learn/seo',         'Moz SEO Guide'),
                                                                                                                 (13,3,'Google Ads',           'Search campaigns, display ads, bidding strategy and optimization', '4 weeks', 'https://skillshop.withgoogle.com/', 'Google Skill Shop'),
                                                                                                                 (13,4,'Social Media Marketing','Content strategy, Meta Ads, LinkedIn, Instagram growth',          '3 weeks', 'https://www.meta.com/business/ads/','Meta Business Ads'),
                                                                                                                 (13,5,'Analytics & Reporting','GA4, Search Console, attribution models, dashboards',              '3 weeks', 'https://analytics.google.com/analytics/academy/','Google Analytics Academy'),
                                                                                                                 (13,6,'Get Certified',        'Google Analytics, Google Ads and HubSpot certifications',         '4 weeks', 'https://skillshop.withgoogle.com/', 'Google Certifications');

-- Supply Chain (career_id = 14)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (14,1,'SCM Fundamentals',     'Supply chain flows, demand planning, inventory management',        '4 weeks', 'https://www.apics.org/',            'APICS Supply Chain'),
                                                                                                                 (14,2,'Procurement',          'Supplier evaluation, RFQ process, contract negotiation',           '3 weeks', 'https://www.cips.org/',             'CIPS Procurement'),
                                                                                                                 (14,3,'ERP Systems',          'SAP and Oracle basics — hands-on modules for SCM',                 '4 weeks', 'https://learning.sap.com/',         'SAP Learning Hub'),
                                                                                                                 (14,4,'Logistics & Operations','Warehousing, transportation, last-mile delivery optimization',    '3 weeks', 'https://www.inboundlogistics.com/', 'Inbound Logistics'),
                                                                                                                 (14,5,'Data Analysis for SCM','Excel, SQL and Power BI for supply chain analytics',               '3 weeks', 'https://powerbi.microsoft.com/en-us/learning/','Power BI Learning'),
                                                                                                                 (14,6,'CPIM Certification',   'APICS Certified in Production and Inventory Management exam',     '8 weeks', 'https://www.apics.org/credentials-certifications/cpim','APICS CPIM');

-- HR Management (career_id = 15)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (15,1,'HR Fundamentals',      'Recruitment lifecycle, onboarding, performance management',        '3 weeks', 'https://www.shrm.org/resourcesandtools','SHRM Resources'),
                                                                                                                 (15,2,'Pakistan Labor Law',   'EOBI, SESSI, termination rules, minimum wage compliance',          '3 weeks', 'https://www.ilo.org/islamabad',     'ILO Pakistan'),
                                                                                                                 (15,3,'Talent Acquisition',   'Job description writing, interview techniques, assessment centers','3 weeks', 'https://www.linkedin.com/business/talent','LinkedIn Talent'),
                                                                                                                 (15,4,'HR Analytics',         'Headcount metrics, attrition analysis, dashboards in Excel',       '3 weeks', 'https://www.coursera.org/learn/people-analytics','People Analytics Course'),
                                                                                                                 (15,5,'Organizational Design','Org structure, job grading, compensation benchmarking',            '3 weeks', 'https://www.mercer.com/',           'Mercer HR'),
                                                                                                                 (15,6,'PHR / SHRM-CP Exam',  'Professional HR certification preparation and examination',         '8 weeks', 'https://www.hrci.org/',             'HRCI Certifications');

-- Freelance Consulting (career_id = 16)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (16,1,'Pick Your Niche',      'Identify one specific skill to monetize first — not everything',   '1 week',  'https://www.upwork.com/resources/', 'Upwork Resources'),
                                                                                                                 (16,2,'Build Your Portfolio', 'Create 3 sample projects demonstrating your niche skill',          '3 weeks', 'https://www.behance.net/',          'Behance Portfolio'),
                                                                                                                 (16,3,'Upwork Profile Setup', 'Optimized title, bio, rate, portfolio and skills section',         '1 week',  'https://www.upwork.com/',           'Upwork'),
                                                                                                                 (16,4,'First Client Strategy','Proposal writing, pricing, low initial rate strategy',             '2 weeks', 'https://www.fiverr.com/resources/', 'Fiverr Resources'),
                                                                                                                 (16,5,'Client Management',    'Contracts, communication, revisions, getting 5-star reviews',      '2 weeks', 'https://bonsai.io/templates',       'Bonsai Contract Templates'),
                                                                                                                 (16,6,'Scale Your Income',    'Raise rates, get referrals, move to Toptal or direct clients',     '4 weeks', 'https://www.toptal.com/',           'Toptal');

-- Medical Specialist (career_id = 17)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (17,1,'MBBS Degree',          '5-year medical degree covering all body systems and clinical skills','5 years','https://www.pmdc.gov.pk/',          'PMDC Pakistan'),
                                                                                                                 (17,2,'House Job',            '1-year supervised clinical rotations across all major departments', '1 year',  'https://www.pmdc.gov.pk/',          'PMDC Registration'),
                                                                                                                 (17,3,'FCPS Part 1',          'Fellowship entrance exam — choose your specialization here',        '1 year',  'https://www.cpsp.edu.pk/',          'CPSP Pakistan'),
                                                                                                                 (17,4,'Residency Training',   '3-5 year specialist training in your chosen specialty',             '4 years', 'https://www.cpsp.edu.pk/',          'CPSP Fellowship'),
                                                                                                                 (17,5,'FCPS Part 2 / MRCP',  'Final fellowship examination to become a certified specialist',     '6 months','https://www.cpsp.edu.pk/',          'CPSP Exams'),
                                                                                                                 (17,6,'International Pathway','USMLE or PLAB for USA/UK practice or academic research career',    '1 year',  'https://www.usmle.org/',            'USMLE');

-- Nursing (career_id = 18)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (18,1,'BSc Nursing Degree',   '4-year nursing degree with clinical rotations in hospitals',       '4 years', 'https://www.pnc.org.pk/',           'Pakistan Nursing Council'),
                                                                                                                 (18,2,'Clinical Competencies','IV cannulation, wound care, medication administration, vitals',    '6 months','https://www.rcn.org.uk/',           'Royal College of Nursing'),
                                                                                                                 (18,3,'Specialization',       'Choose ICU, pediatrics, oncology, or midwifery specialization',   '1 year',  'https://www.pnc.org.pk/',           'PNC Specialization'),
                                                                                                                 (18,4,'International Pathway','NCLEX-RN for USA, NMC for UK — highest paying nursing markets',   '1 year',  'https://www.ncsbn.org/nclex.htm',   'NCLEX Registration'),
                                                                                                                 (18,5,'Leadership in Nursing','Head nurse, nurse educator, hospital administration pathway',      '2 years', 'https://www.sigma.nursingrepository.org/','Sigma Nursing'),
                                                                                                                 (18,6,'Continuous Education', 'CPD points, advanced certifications, research and publication',   'Ongoing', 'https://www.icn.ch/',               'International Council of Nurses');

-- Pharmacy (career_id = 19)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (19,1,'Pharm-D Degree',       '5-year pharmacy degree covering pharmaceutics, pharmacology',      '5 years', 'https://www.pharmacy.edu.pk/',      'Pharmacy Council Pakistan'),
                                                                                                                 (19,2,'Hospital Internship',  '1-year hospital pharmacy internship under supervision',            '1 year',  'https://www.pharmacy.edu.pk/',      'Pharmacy Council'),
                                                                                                                 (19,3,'Community Pharmacy',   'Dispensing, patient counseling, OTC drug management',             '6 months','https://www.fip.org/',              'FIP Pharmacy'),
                                                                                                                 (19,4,'Clinical Pharmacy',    'Drug therapy monitoring, pharmacovigilance, clinical rounds',      '1 year',  'https://www.ashp.org/',             'ASHP Clinical Pharmacy'),
                                                                                                                 (19,5,'Pharmaceutical Industry','Quality control, regulatory affairs, drug manufacturing',        '1 year',  'https://www.ich.org/',              'ICH Guidelines'),
                                                                                                                 (19,6,'International Pathway','FPGEE for USA or GPhC for UK pharmacy practice',                  '1 year',  'https://www.nabp.pharmacy/',        'NABP USA');

-- Allied Health (career_id = 20)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (20,1,'Allied Health Degree', 'BSc Physiotherapy, Medical Lab Technology or Radiology',          '4 years', 'https://www.pmdc.gov.pk/',          'PMDC Allied Health'),
                                                                                                                 (20,2,'Clinical Placement',   'Hospital rotations — outpatient, inpatient and ICU settings',     '1 year',  'https://www.wcpt.org/',             'World Physiotherapy'),
                                                                                                                 (20,3,'Specialization',       'Sports rehab, neuro rehab, pediatrics or orthopedics',            '1 year',  'https://www.wcpt.org/',             'WCPT Specialization'),
                                                                                                                 (20,4,'Certification',        'National Board Exam and Health Care Commission registration',      '6 months','https://www.nhrc.gov.pk/',          'NHRC Pakistan'),
                                                                                                                 (20,5,'International Options','UAE, Saudi Arabia, UK and Canada actively recruit Pakistani AHPs','1 year',  'https://www.hcpc-uk.org/',          'HCPC UK Registration'),
                                                                                                                 (20,6,'Private Practice',     'Set up your own clinic after 3 years clinical experience',        '2 years', 'https://www.physio-network.com/',   'Physio Network');

-- Electrical Engineering (career_id = 21)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (21,1,'BE Electrical Degree', '4-year engineering degree — power systems, electronics, signals', '4 years', 'https://www.pec.org.pk/',           'Pakistan Engineering Council'),
                                                                                                                 (21,2,'Circuit & Power Basics','Kirchhoff laws, AC/DC analysis, transformers, motors',           '3 months','https://www.allaboutcircuits.com/', 'All About Circuits'),
                                                                                                                 (21,3,'ETAP & AutoCAD',       'Power system simulation with ETAP, technical drawing with CAD',   '3 months','https://etap.com/training',         'ETAP Training'),
                                                                                                                 (21,4,'PEC Registration',     'Register as professional engineer with Pakistan Engineering Council','1 month','https://www.pec.org.pk/',           'PEC Registration'),
                                                                                                                 (21,5,'Power Sector Projects','WAPDA, NEPRA, NTDC, K-Electric — internship and job targets',     '6 months','https://www.nepra.org.pk/',         'NEPRA Pakistan'),
                                                                                                                 (21,6,'PE License / MSc',     'Advanced certification or Masters in Power Engineering',          '2 years', 'https://www.ieee.org/',             'IEEE Power & Energy');

-- Civil Engineering (career_id = 22)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (22,1,'BE Civil Degree',      '4-year civil engineering covering structures, materials, hydraulics','4 years','https://www.pec.org.pk/',          'PEC Civil Engineering'),
                                                                                                                 (22,2,'AutoCAD & Revit',      'Technical drawing, 2D plans, 3D building models in Revit',        '3 months','https://www.autodesk.com/education/','Autodesk Education'),
                                                                                                                 (22,3,'Structural Design',    'Concrete and steel design using STAAD Pro or ETABS',              '4 months','https://www.bentley.com/en/products/brands/staad','STAAD Pro'),
                                                                                                                 (22,4,'Site Management',      'Construction management, quantity surveying, BOQ preparation',     '6 months','https://www.ciob.org/',             'CIOB'),
                                                                                                                 (22,5,'PEC Registration',     'Register with Pakistan Engineering Council after graduation',      '1 month', 'https://www.pec.org.pk/',           'PEC Registration'),
                                                                                                                 (22,6,'Project Leadership',   'Lead infrastructure projects — housing, roads, dams, bridges',    '3 years', 'https://www.pmi.org/',              'PMI Project Management');

-- Renewable Energy (career_id = 23)
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
                                                                                                                 (23,1,'Energy Fundamentals',  'Solar radiation, photovoltaic effect, wind energy principles',     '3 weeks', 'https://www.irena.org/publications','IRENA Publications'),
                                                                                                                 (23,2,'Solar System Design',  'Panel sizing, inverter selection, battery storage, grid-tie',      '4 weeks', 'https://pvwatts.nrel.gov/',         'NREL PVWatts Calculator'),
                                                                                                                 (23,3,'MATLAB & HOMER',       'Simulate energy systems, optimize hybrid solar-wind solutions',    '4 weeks', 'https://www.homerenergy.com/',      'HOMER Energy'),
                                                                                                                 (23,4,'AEDB Registration',    'Alternative Energy Development Board — professional registration', '2 weeks', 'https://www.aedb.org/',             'AEDB Pakistan'),
                                                                                                                 (23,5,'Net Metering & Policy','NEPRA net metering, solar policy, project financing in Pakistan',  '3 weeks', 'https://www.nepra.org.pk/',         'NEPRA Pakistan'),
                                                                                                                 (23,6,'Lead a Solar Project', 'Design, procure and commission a complete solar installation',     '2 months','https://www.solarenergy.org/',      'Solar Energy International');
-- ============================================================
-- TABLE 9: SeniorAdvice
-- Advice from industry seniors — per career
-- ============================================================
CREATE TABLE SeniorAdvice (
                              advice_id       INT             PRIMARY KEY AUTO_INCREMENT,
                              career_id       INT             NOT NULL,
                              senior_name     VARCHAR(100)    NOT NULL,
                              current_role    VARCHAR(100),
                              company         VARCHAR(100),
                              university      VARCHAR(100),
                              years_exp       INT,
                              advice_text     TEXT            NOT NULL,
                              avatar_path     VARCHAR(255),
                              FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                                  ON DELETE CASCADE
);

-- ── SENIOR ADVICE — NEW CAREERS ──────────────────────────────

INSERT INTO SeniorAdvice (career_id, senior_name, current_role, company, university, years_exp, advice_text) VALUES
-- Full-Stack (7)
(7, 'Omer Sheikh',      'Full Stack Engineer',          'Invozone',           'FAST',    4, 'Learn the fundamentals before any framework. JavaScript deeply, then React. Companies care far less about which framework you know and far more about whether you can think clearly and debug fast.'),
(7, 'Aisha Tariq',      'Senior Web Developer',         'Devsinc',            'NUST',    3, 'Build real projects from day one. A calculator, a portfolio, a simple e-commerce site. The learning that happens when something breaks in your own project is irreplaceable.'),
-- Cloud & DevOps (8)
(8, 'Fahad Malik',      'DevOps Engineer',              'Systems Limited',    'UET',     5, 'Get the AWS Solutions Architect Associate cert. It is the single most recognized cloud credential in Pakistan right now. Pair it with hands-on projects and you will have more offers than you can handle.'),
(8, 'Sana Mirza',       'Cloud Engineer',               'TRG Pakistan',       'GIKI',    3, 'Learn Docker and Kubernetes properly before anything else. Every company is containerizing. If you can debug a broken Kubernetes cluster, you are already in the top 10% of candidates.'),
-- UI/UX (9)
(9, 'Maira Javed',      'UX Designer',                  'Bramerz',            'NCA',     4, 'Your portfolio is everything in design. Not your degree, not your certifications — your portfolio. Make three case studies that show your thinking process from research to final design.'),
(9, 'Usman Chaudhry',   'Product Designer',             'Careem',             'LUMS',    5, 'Learn to present your work. The best designers who cannot articulate their decisions lose to average designers who can. Practice explaining why you made every choice.'),
-- Blockchain (10)
(10,'Hamza Siddiqui',   'Blockchain Developer',         'TPS Pakistan',       'FAST',    3, 'The space moves incredibly fast. Focus on fundamentals — how blockchains actually work, cryptography basics, Solidity security patterns. Trends change but fundamentals do not.'),
-- CA / ACCA (11)
(11,'Imran Qureshi',    'Senior Manager Audit',         'KPMG Pakistan',      'ICAP',    7, 'The articleship years are hard but irreplaceable. You will work long hours for modest pay. Every one of those hours is building the technical and professional judgment that defines your entire career. Push through.'),
(11,'Fareeha Ahmed',    'Finance Manager',              'Engro Corporation',  'ACCA',    5, 'Get your ACCA alongside your degree if possible. The global qualification opens doors in UAE, UK and Canada that a local qualification alone cannot.'),
-- Product Management (12)
(12,'Talha Raza',       'Product Manager',              'Careem',             'LUMS',    4, 'Come from engineering, design or marketing — it does not matter. What matters is data obsession and user empathy. Build something, launch it, measure what happens. That one experience is worth more than any PM course.'),
-- Digital Marketing (13)
(13,'Noor Fatima',      'Digital Marketing Manager',    'Foodpanda Pakistan', 'IBA',     4, 'Know your numbers. CTR, CPC, ROAS, CAC, LTV — if you cannot explain these in a job interview you will not get hired. Marketing is math now.'),
(13,'Bilal Chaudhry',   'SEO Specialist',               'Freelance',          'Virtual', 5, 'SEO is the best long-term skill in digital marketing. Paid ads stop when you stop paying. SEO compounds over time. Master it and you will never be short of clients.'),
-- Supply Chain (14)
(14,'Asad Rauf',        'Supply Chain Manager',         'Nestle Pakistan',    'IBA',     6, 'Learn SAP properly. Not just surface level. 80% of large Pakistani companies run on SAP and being able to navigate it confidently puts you ahead of most candidates immediately.'),
-- HR (15)
(15,'Sobia Khan',       'HR Business Partner',          'Jazz Pakistan',      'IBA',     5, 'HR is not just hiring. The strategic business partner role is where the real impact and pay are. Get strong in analytics, speak the language of business, and stop thinking of HR as an administrative function.'),
-- Freelancing (16)
(16,'Ahmed Waqas',      'Senior Freelancer',            'Self-employed',      'COMSATS', 6, 'Specialize ruthlessly. The generalist freelancers earn $10-15 per hour. The specialists earn $50-100. Pick one thing — automation, Shopify, HubSpot, whatever — and become the person people seek out specifically for that.'),
-- Medical Specialist (17)
(17,'Dr. Faiza Malik',  'Consultant Cardiologist',      'Aga Khan Hospital',  'DUHS',    8, 'The FCPS journey is a marathon not a sprint. The residents who succeed are not always the most brilliant — they are the most consistent. Show up every day, treat every patient like a teacher, and it pays off.'),
-- Nursing (18)
(18,'Samira Hussain',   'ICU Nurse',                    'Shaukat Khanum',     'KMU',     5, 'NCLEX opens up the world. USA, Canada, UK, Australia — they all need nurses and will sponsor your visa. Invest one year in preparation and it can change your entire financial trajectory.'),
-- Pharmacy (19)
(19,'Dr. Zara Naqvi',   'Clinical Pharmacist',          'Agha Khan Hospital', 'UCP',     4, 'Clinical pharmacy is the future in Pakistan. Sitting on a counter dispensing pills is not your ceiling. Push into hospital clinical rounds, drug therapy monitoring and pharmacovigilance.'),
-- Allied Health (20)
(20,'Kamran Ashraf',    'Senior Physiotherapist',       'South City Hospital','Riphah',  5, 'The Gulf countries are actively recruiting Pakistani physiotherapists with competitive salaries and packages. Get your degree, get 2 years local experience, then explore UAE and Saudi options.'),
-- Electrical Engineering (21)
(21,'Tariq Mehmood',    'Power Systems Engineer',       'NTDC',               'UET',     8, 'ETAP is your entry ticket to the power sector. Learn it properly during your degree. Every power engineering job in Pakistan will ask if you can run a load flow study.'),
-- Civil Engineering (22)
(22,'Waqar Ahmed',      'Senior Civil Engineer',        'Frontier Works Org', 'UET',     9, 'Site experience is everything in civil. The engineers who stay in offices do okay. The ones who get on site early, understand construction realities and build relationships with contractors — they lead projects.'),
-- Renewable Energy (23)
(23,'Danish Ali',       'Solar Energy Consultant',      'Reon Energy',        'NUST',    4, 'Pakistan has exceptional solar irradiance and a government actively pushing net metering. This sector will explode in the next 5 years. Get in now while the competition is still low and grow with the industry.');

-- ============================================================
-- TABLE 10: Badge
-- All available badges — system-wide
-- ============================================================
CREATE TABLE Badge (
                       badge_id        INT             PRIMARY KEY AUTO_INCREMENT,
                       name            VARCHAR(100)    NOT NULL UNIQUE,
                       description     TEXT,
                       icon_path       VARCHAR(255),
                       xp_reward       INT             DEFAULT 25,
                       condition_type  VARCHAR(50)     NOT NULL,   -- streak, score, explorer, master
                       condition_value INT             NOT NULL,   -- e.g. streak=7, score=90
                       career_id       INT             NULL,       -- NULL = global, not NULL = career-specific
                       FOREIGN KEY (career_id) REFERENCES CareerPath(career_id)
                           ON DELETE SET NULL
);

-- ── BADGES — NEW CAREERS ─────────────────────────────────────

INSERT INTO Badge (name, description, icon_path, xp_reward, condition_type, condition_value, career_id) VALUES
-- Career completion badges
('Stack Starter',       'Complete all Full-Stack Engineering games',        'images/badges/stack_starter.png',      150, 'career_complete', 1, 7),
('Cloud Cadet',         'Complete all Cloud & DevOps games',                'images/badges/cloud_cadet.png',        150, 'career_complete', 1, 8),
('Design Rookie',       'Complete all UI/UX Design games',                  'images/badges/design_rookie.png',      150, 'career_complete', 1, 9),
('Chain Novice',        'Complete all Blockchain games',                    'images/badges/chain_novice.png',       150, 'career_complete', 1, 10),
('Finance Rookie',      'Complete all CA/ACCA games',                       'images/badges/finance_rookie.png',     150, 'career_complete', 1, 11),
('PM Starter',          'Complete all Product Management games',            'images/badges/pm_starter.png',         150, 'career_complete', 1, 12),
('Growth Rookie',       'Complete all Digital Marketing games',             'images/badges/growth_rookie.png',      150, 'career_complete', 1, 13),
('Ops Starter',         'Complete all Supply Chain games',                  'images/badges/ops_starter.png',        150, 'career_complete', 1, 14),
('People Starter',      'Complete all HR Management games',                 'images/badges/people_starter.png',     150, 'career_complete', 1, 15),
('Solo Starter',        'Complete all Freelancing games',                   'images/badges/solo_starter.png',       150, 'career_complete', 1, 16),
('Med Rookie',          'Complete all Medical Specialist games',            'images/badges/med_rookie.png',         150, 'career_complete', 1, 17),
('Care Rookie',         'Complete all Nursing games',                       'images/badges/care_rookie.png',        150, 'career_complete', 1, 18),
('Pharma Rookie',       'Complete all Pharmacy games',                      'images/badges/pharma_rookie.png',      150, 'career_complete', 1, 19),
('Allied Rookie',       'Complete all Allied Health games',                 'images/badges/allied_rookie.png',      150, 'career_complete', 1, 20),
('Circuits Rookie',     'Complete all Electrical Engineering games',        'images/badges/circuits_rookie.png',    150, 'career_complete', 1, 21),
('Builder Rookie',      'Complete all Civil Engineering games',             'images/badges/builder_rookie.png',     150, 'career_complete', 1, 22),
('Green Rookie',        'Complete all Renewable Energy games',              'images/badges/green_rookie.png',       150, 'career_complete', 1, 23),

-- Mastery badges
('Stack Master',        'Score above 85% across all Full-Stack games',      'images/badges/stack_master.png',       300, 'career_mastery',  85, 7),
('Cloud Master',        'Score above 85% across all DevOps games',          'images/badges/cloud_master.png',       300, 'career_mastery',  85, 8),
('Design Master',       'Score above 85% across all UI/UX games',           'images/badges/design_master.png',      300, 'career_mastery',  85, 9),
('Chain Master',        'Score above 85% across all Blockchain games',      'images/badges/chain_master.png',       300, 'career_mastery',  85, 10),
('Finance Master',      'Score above 85% across all CA games',              'images/badges/finance_master.png',     300, 'career_mastery',  85, 11),
('PM Master',           'Score above 85% across all PM games',              'images/badges/pm_master.png',          300, 'career_mastery',  85, 12),
('Growth Master',       'Score above 85% across all Marketing games',       'images/badges/growth_master.png',      300, 'career_mastery',  85, 13),
('Ops Master',          'Score above 85% across all Supply Chain games',    'images/badges/ops_master.png',         300, 'career_mastery',  85, 14),
('People Master',       'Score above 85% across all HR games',              'images/badges/people_master.png',      300, 'career_mastery',  85, 15),
('Solo Master',         'Score above 85% across all Freelancing games',     'images/badges/solo_master.png',        300, 'career_mastery',  85, 16),
('Med Master',          'Score above 85% across all Medical games',         'images/badges/med_master.png',         300, 'career_mastery',  85, 17),
('Care Master',         'Score above 85% across all Nursing games',         'images/badges/care_master.png',        300, 'career_mastery',  85, 18),
('Pharma Master',       'Score above 85% across all Pharmacy games',        'images/badges/pharma_master.png',      300, 'career_mastery',  85, 19),
('Allied Master',       'Score above 85% across all Allied Health games',   'images/badges/allied_master.png',      300, 'career_mastery',  85, 20),
('Circuits Master',     'Score above 85% across all Electrical games',      'images/badges/circuits_master.png',    300, 'career_mastery',  85, 21),
('Builder Master',      'Score above 85% across all Civil games',           'images/badges/builder_master.png',     300, 'career_mastery',  85, 22),
('Green Master',        'Score above 85% across all Renewable games',       'images/badges/green_master.png',       300, 'career_mastery',  85, 23);

-- ============================================================
-- TABLE 11: PlayerBadge
-- Which badges each player has earned
-- ============================================================
CREATE TABLE PlayerBadge (
                             player_id       INT             NOT NULL,
                             badge_id        INT             NOT NULL,
                             earned_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (player_id, badge_id),
                             FOREIGN KEY (player_id) REFERENCES Player(player_id)
                                 ON DELETE CASCADE,
                             FOREIGN KEY (badge_id)  REFERENCES Badge(badge_id)
                                 ON DELETE CASCADE
);

-- ============================================================
-- TABLE 12: Quest
-- Daily/weekly challenges for players
-- ============================================================
CREATE TABLE Quest (
                       quest_id        INT             PRIMARY KEY AUTO_INCREMENT,
                       title           VARCHAR(100)    NOT NULL,
                       description     TEXT,
                       quest_type      VARCHAR(50)     NOT NULL,   -- daily, weekly, one-time
                       condition_type  VARCHAR(50)     NOT NULL,   -- play_games, score_above, streak
                       condition_value INT             NOT NULL,
                       xp_reward       INT             DEFAULT 100,
                       is_active       BOOLEAN         DEFAULT TRUE
);

-- ============================================================
-- TABLE 13: PlayerQuest
-- Tracks each player's quest progress
-- ============================================================
CREATE TABLE PlayerQuest (
                             player_id       INT             NOT NULL,
                             quest_id        INT             NOT NULL,
                             progress        INT             DEFAULT 0,
                             completed       BOOLEAN         DEFAULT FALSE,
                             completed_at    TIMESTAMP       NULL,
                             assigned_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (player_id, quest_id),
                             FOREIGN KEY (player_id) REFERENCES Player(player_id)
                                 ON DELETE CASCADE,
                             FOREIGN KEY (quest_id)  REFERENCES Quest(quest_id)
                                 ON DELETE CASCADE
);

-- ============================================================
-- TABLE 14: Leaderboard
-- Cached top 10 — refreshed after every game session
-- ============================================================
CREATE TABLE Leaderboard (
                             rank_position   INT             NOT NULL,
                             player_id       INT             NOT NULL,
                             total_xp        INT             NOT NULL,
                             level           INT             NOT NULL,
                             badge_count     INT             DEFAULT 0,
                             updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (rank_position),
                             FOREIGN KEY (player_id) REFERENCES Player(player_id)
                                 ON DELETE CASCADE
);


-- ============================================================
-- SEED DATA — starter content for all 6 careers
-- ============================================================

-- ── CAREER PATHS ────────────────────────────────────────────
INSERT INTO CareerPath (title, field, description, avg_salary_pkr, market_demand, icon_path) VALUES
                                                                                                 ('Data Science',           'Data',     'Analyze and interpret complex data to help organizations make better decisions.',           150000, 9.2, 'images/careers/datasci.png'),
                                                                                                 ('Software Engineering',   'Tech',     'Design, build and maintain software systems that power the modern world.',                  180000, 9.5, 'images/careers/softeng.png'),
                                                                                                 ('Cyber Security',         'Security', 'Protect systems, networks and data from digital attacks and unauthorized access.',          200000, 9.8, 'images/careers/cybersec.png'),
                                                                                                 ('Artificial Intelligence','AI',       'Build intelligent systems that learn, reason and solve complex human problems.',            220000, 9.7, 'images/careers/ai.png'),
                                                                                                 ('Web Development',        'Web',      'Create and maintain websites and web applications used by millions worldwide.',             120000, 8.8, 'images/careers/webdev.png'),
                                                                                                 ('Mobile Development',     'Mobile',   'Build apps for iOS and Android that people use every single day.',                         140000, 9.0, 'images/careers/mobiledev.png');


-- ── SKILLS ──────────────────────────────────────────────────
INSERT INTO Skill (name, category, description) VALUES
                                                    ('Python',              'Programming',  'General purpose language dominant in data, AI and automation'),
                                                    ('Java',                'Programming',  'Object-oriented language used in enterprise and Android development'),
                                                    ('JavaScript',          'Programming',  'The language of the web — frontend and backend'),
                                                    ('SQL',                 'Database',     'Query and manage relational databases'),
                                                    ('Statistics',          'Mathematics',  'Probability, distributions and statistical inference'),
                                                    ('Machine Learning',    'AI',           'Building models that learn from data'),
                                                    ('Networking',          'Security',     'How computers communicate — protocols and infrastructure'),
                                                    ('Linux',               'Systems',      'Command line, file systems and system administration'),
                                                    ('Cryptography',        'Security',     'Encryption, hashing and secure communication'),
                                                    ('HTML/CSS',            'Web',          'Structure and styling of web pages'),
                                                    ('React',               'Web',          'JavaScript library for building user interfaces'),
                                                    ('Swift',               'Mobile',       'Apple\'s language for iOS development'),
('Kotlin',              'Mobile',       'Modern language for Android development'),
('Data Visualization',  'Data',         'Presenting data clearly through charts and graphs'),
('Cloud Computing',     'Infrastructure','Deploying and managing services on AWS, Azure or GCP'),
('Problem Solving',     'Soft Skill',   'Breaking complex problems into manageable steps'),
('UI/UX Design',        'Design',       'Creating intuitive and beautiful user interfaces'),
('Deep Learning',       'AI',           'Neural networks for image, speech and language tasks'),
('Git',                 'Tools',        'Version control for tracking and collaborating on code'),
('Algorithms',          'CS Fundamentals','Sorting, searching, graph traversal and optimization');


-- ── CAREER SKILLS ───────────────────────────────────────────
-- Data Science (career_id = 1)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(1, 1,  5, TRUE),   -- Python
(1, 4,  4, TRUE),   -- SQL
(1, 5,  5, TRUE),   -- Statistics
(1, 6,  4, TRUE),   -- Machine Learning
(1, 14, 4, TRUE),   -- Data Visualization
(1, 15, 3, FALSE);  -- Cloud Computing

-- Software Engineering (career_id = 2)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(2, 2,  5, TRUE),   -- Java
(2, 4,  4, TRUE),   -- SQL
(2, 16, 5, TRUE),   -- Problem Solving
(2, 20, 5, TRUE),   -- Algorithms
(2, 19, 4, TRUE),   -- Git
(2, 15, 3, FALSE);  -- Cloud Computing

-- Cyber Security (career_id = 3)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(3, 7,  5, TRUE),   -- Networking
(3, 8,  5, TRUE),   -- Linux
(3, 9,  5, TRUE),   -- Cryptography
(3, 16, 4, TRUE),   -- Problem Solving
(3, 20, 3, FALSE),  -- Algorithms
(3, 19, 3, FALSE);  -- Git

-- Artificial Intelligence (career_id = 4)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(4, 1,  5, TRUE),   -- Python
(4, 5,  5, TRUE),   -- Statistics
(4, 6,  5, TRUE),   -- Machine Learning
(4, 18, 5, TRUE),   -- Deep Learning
(4, 20, 4, TRUE),   -- Algorithms
(4, 15, 3, FALSE);  -- Cloud Computing

-- Web Development (career_id = 5)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(5, 3,  5, TRUE),   -- JavaScript
(5, 10, 5, TRUE),   -- HTML/CSS
(5, 11, 4, TRUE),   -- React
(5, 17, 4, TRUE),   -- UI/UX Design
(5, 4,  3, FALSE),  -- SQL
(5, 19, 4, TRUE);   -- Git

-- Mobile Development (career_id = 6)
INSERT INTO CareerSkill (career_id, skill_id, importance, is_core) VALUES
(6, 12, 4, TRUE),   -- Swift
(6, 13, 4, TRUE),   -- Kotlin
(6, 17, 5, TRUE),   -- UI/UX Design
(6, 16, 4, TRUE),   -- Problem Solving
(6, 19, 4, TRUE),   -- Git
(6, 4,  3, FALSE);  -- SQL


-- ── MINI GAMES ──────────────────────────────────────────────
INSERT INTO MiniGame (career_id, title, description, instructions, difficulty, max_score, time_limit_sec, xp_reward, game_type) VALUES
-- Data Science games
(1, 'Pattern Detective',    'Spot the trend hidden in the data sequence',       'Identify which pattern continues the given sequence of numbers. The faster you answer, the more points you earn.',          'Easy',   100, 45,  50, 'pattern'),
(1, 'Chart Challenge',      'Read and interpret data visualizations quickly',   'Answer questions based on the charts shown. Accuracy matters more than speed here.',                                         'Medium', 150, 60,  75, 'chart_reading'),
(1, 'Outlier Hunter',       'Find the data point that does not belong',         'Scan the dataset and identify the outlier. Each correct answer adds to your score.',                                         'Hard',   200, 30,  100,'outlier'),

-- Software Engineering games
(2, 'Code Sequence',        'Arrange the code blocks in correct logical order', 'Drag and drop the code blocks to form a working program. Fewer moves = higher score.',                                       'Easy',   100, 60,  50, 'logic_puzzle'),
(2, 'Complexity Check',     'Identify the Big O complexity of algorithms',      'Given a code snippet, select the correct time complexity from the options shown.',                                            'Medium', 150, 45,  75, 'algorithm'),
(2, 'Debug Race',           'Fix the broken program before time runs out',      'Find and click all the bugs in the code. Every correct fix adds points. Wrong clicks deduct points.',                       'Hard',   200, 60,  100,'bugfinder'),

-- Cyber Security games
(3, 'Vulnerability Scan',   'Spot the security flaw in the code',               'Examine the code carefully and click on the line that contains a security vulnerability.',                                   'Easy',   100, 60,  50, 'bugfinder'),
(3, 'Cipher Breaker',       'Decode the encrypted message',                     'Use the clues provided to decode the simple cipher. Each letter correctly placed earns points.',                             'Medium', 150, 90,  75, 'cipher'),
(3, 'Network Intruder',     'Identify the suspicious activity in the logs',     'Scan through the network logs and identify which entry shows suspicious behavior.',                                          'Hard',   200, 45,  100,'log_analysis'),

-- AI / ML games
(4, 'Data Classifier',      'Sort the data points into correct categories',     'Drag each data point to its correct category. Your accuracy determines your career compatibility score.',                   'Easy',   100, 60,  50, 'classification'),
(4, 'Model Trainer',        'Choose the right algorithm for each problem',      'Given a dataset description, select which machine learning algorithm is most appropriate.',                                  'Medium', 150, 45,  75, 'algorithm_select'),
(4, 'Neural Detective',     'Trace the path through the neural network',        'Follow how the input transforms at each layer and predict the final output.',                                                'Hard',   200, 60,  100,'neural_trace'),

-- Web Development games
(5, 'CSS Color Match',      'Match the exact color to complete the design',     'Choose the correct color values to match the target design. Pixel-perfect choices earn maximum points.',                   'Easy',   100, 45,  50, 'design_match'),
(5, 'Layout Builder',       'Arrange elements to match the target layout',      'Drag the UI elements to match the given wireframe exactly. Fewer moves = higher score.',                                    'Medium', 150, 60,  75, 'layout_puzzle'),
(5, 'Responsive Tester',    'Spot which layout breaks on mobile',               'Given multiple layouts, identify which one will break on a small screen.',                                                  'Hard',   200, 30,  100,'responsive'),

-- Mobile Development games
(6, 'UX Flow Sorter',       'Arrange the app screens in correct user flow',     'Put the app screens in the order a user would naturally navigate through them.',                                            'Easy',   100, 60,  50, 'ux_flow'),
(6, 'Gesture Guesser',      'Match the gesture to the correct action',          'Given a mobile gesture description, select what action it should perform in the app.',                                      'Medium', 150, 45,  75, 'gesture_match'),
(6, 'App Debugger',         'Find what is wrong with this mobile UI',           'Identify the usability problem in the mobile interface shown.',                                                             'Hard',   200, 60,  100,'ui_audit');


-- ── ROADMAP STEPS ────────────────────────────────────────────
-- Data Science Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(1, 1, 'Python Fundamentals',       'Learn Python syntax, data structures, and basic programming',             '4 weeks',  'https://www.python.org/about/gettingstarted/', 'Python Official Guide'),
(1, 2, 'Statistics & Probability',  'Master descriptive stats, probability distributions and hypothesis testing', '4 weeks', 'https://www.khanacademy.org/math/statistics-probability', 'Khan Academy Statistics'),
(1, 3, 'Data Analysis with Pandas', 'Clean, manipulate and analyze datasets using Pandas and NumPy',           '3 weeks',  'https://pandas.pydata.org/docs/getting_started/', 'Pandas Documentation'),
(1, 4, 'Data Visualization',        'Create clear charts and graphs with Matplotlib and Seaborn',              '2 weeks',  'https://matplotlib.org/stable/tutorials/', 'Matplotlib Tutorials'),
(1, 5, 'Machine Learning Basics',   'Linear regression, classification, clustering with Scikit-learn',        '5 weeks',  'https://scikit-learn.org/stable/tutorial/', 'Scikit-learn Tutorial'),
(1, 6, 'SQL for Data Science',      'Query databases, joins, aggregations and window functions',               '3 weeks',  'https://mode.com/sql-tutorial/', 'Mode SQL Tutorial'),
(1, 7, 'Your First Project',        'Build a complete end-to-end data analysis project for your portfolio',    '4 weeks',  'https://www.kaggle.com/competitions', 'Kaggle Competitions');

-- Software Engineering Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(2, 1, 'Programming Foundations',   'Master one language deeply — Java or Python recommended',                 '6 weeks',  'https://docs.oracle.com/javase/tutorial/', 'Java Official Tutorial'),
(2, 2, 'Data Structures',           'Arrays, linked lists, stacks, queues, trees, graphs',                    '4 weeks',  'https://visualgo.net/', 'VisuAlgo — Visual DS'),
(2, 3, 'Algorithms',                'Sorting, searching, dynamic programming, recursion',                     '4 weeks',  'https://leetcode.com/', 'LeetCode Practice'),
(2, 4, 'Object Oriented Design',    'SOLID principles, design patterns, clean code practices',                 '3 weeks',  'https://refactoring.guru/', 'Refactoring Guru'),
(2, 5, 'Databases & SQL',           'Relational databases, SQL, basic database design',                       '3 weeks',  'https://sqlzoo.net/', 'SQLZoo'),
(2, 6, 'Version Control with Git',  'Branching, merging, pull requests, collaborative workflows',              '2 weeks',  'https://learngitbranching.js.org/', 'Learn Git Branching'),
(2, 7, 'Build a Real Project',      'Full stack application from scratch — design to deployment',              '6 weeks',  'https://github.com/', 'GitHub Projects');

-- Cyber Security Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(3, 1, 'Networking Fundamentals',   'TCP/IP, DNS, HTTP, firewalls, routers and protocols',                    '4 weeks',  'https://www.cisco.com/c/en/us/training-events/training-certifications/certifications/entry/ccna.html', 'Cisco CCNA Intro'),
(3, 2, 'Linux Command Line',        'Navigation, permissions, scripting and system administration',            '3 weeks',  'https://linuxjourney.com/', 'Linux Journey'),
(3, 3, 'Cryptography Basics',       'Symmetric, asymmetric encryption, hashing, TLS/SSL',                    '3 weeks',  'https://www.coursera.org/learn/crypto', 'Stanford Cryptography'),
(3, 4, 'Ethical Hacking Intro',     'Penetration testing concepts, reconnaissance, vulnerability scanning',   '5 weeks',  'https://www.hackthebox.com/', 'Hack The Box'),
(3, 5, 'Web Application Security',  'OWASP Top 10, XSS, SQL injection, authentication flaws',                 '4 weeks',  'https://owasp.org/', 'OWASP Foundation'),
(3, 6, 'Security Certifications',   'Study for CompTIA Security+ — industry standard entry cert',             '8 weeks',  'https://www.comptia.org/certifications/security', 'CompTIA Security+'),
(3, 7, 'CTF Challenges',            'Compete in Capture the Flag events to practice real skills',              'Ongoing',  'https://picoctf.org/', 'picoCTF');

-- AI / ML Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(4, 1, 'Python & Math Foundations', 'Python, linear algebra, calculus and statistics essentials',              '6 weeks',  'https://www.3blue1brown.com/', '3Blue1Brown Math'),
(4, 2, 'Machine Learning Core',     'Supervised, unsupervised learning algorithms and model evaluation',       '5 weeks',  'https://www.coursera.org/learn/machine-learning', 'Andrew Ng ML Course'),
(4, 3, 'Deep Learning Basics',      'Neural networks, backpropagation, CNNs and RNNs',                        '6 weeks',  'https://www.deeplearning.ai/', 'DeepLearning.AI'),
(4, 4, 'Natural Language Processing','Text processing, transformers, sentiment analysis, chatbots',            '4 weeks',  'https://huggingface.co/learn', 'Hugging Face Courses'),
(4, 5, 'Computer Vision',           'Image classification, object detection, OpenCV',                         '4 weeks',  'https://opencv.org/courses/', 'OpenCV Courses'),
(4, 6, 'AI Frameworks',             'TensorFlow and PyTorch hands-on practice',                               '4 weeks',  'https://pytorch.org/tutorials/', 'PyTorch Tutorials'),
(4, 7, 'AI Portfolio Project',      'Build and deploy a complete AI model — publish on GitHub',                '6 weeks',  'https://paperswithcode.com/', 'Papers With Code');

-- Web Development Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(5, 1, 'HTML & CSS Mastery',        'Semantic HTML, CSS layouts, Flexbox, Grid, responsive design',           '3 weeks',  'https://www.freecodecamp.org/', 'freeCodeCamp'),
(5, 2, 'JavaScript Fundamentals',   'Variables, functions, DOM manipulation, events, ES6+',                   '5 weeks',  'https://javascript.info/', 'The Modern JS Tutorial'),
(5, 3, 'React Framework',           'Components, props, state, hooks, routing',                               '5 weeks',  'https://react.dev/learn', 'React Official Docs'),
(5, 4, 'Backend with Node.js',      'Express server, REST APIs, middleware, authentication',                   '4 weeks',  'https://nodejs.org/en/learn', 'Node.js Docs'),
(5, 5, 'Databases for Web',         'SQL basics, MongoDB, connecting DB to your web app',                     '3 weeks',  'https://mongoosejs.com/', 'Mongoose Docs'),
(5, 6, 'Deployment',                'Git, GitHub, Netlify/Vercel deployment, domain setup',                   '2 weeks',  'https://vercel.com/docs', 'Vercel Docs'),
(5, 7, 'Full Stack Portfolio',      'Build and deploy 3 complete web projects for your portfolio',             '6 weeks',  'https://www.frontendmentor.io/', 'Frontend Mentor');

-- Mobile Development Roadmap
INSERT INTO RoadmapStep (career_id, step_number, title, description, duration, resource_url, resource_title) VALUES
(6, 1, 'Programming Foundations',   'Java or Kotlin for Android, Swift for iOS — pick one platform first',    '5 weeks',  'https://developer.android.com/courses', 'Android Developer'),
(6, 2, 'UI Design Principles',      'Color theory, typography, spacing, mobile-specific UX patterns',         '3 weeks',  'https://material.io/design', 'Material Design'),
(6, 3, 'Android Development',       'Activities, fragments, layouts, navigation, RecyclerView',                '6 weeks',  'https://developer.android.com/courses/android-basics-compose/course', 'Android Basics'),
(6, 4, 'Data & Storage',            'SharedPreferences, SQLite, Room database, API calls',                    '4 weeks',  'https://developer.android.com/training/data-storage', 'Android Storage'),
(6, 5, 'Publishing an App',         'Play Store guidelines, app signing, release process',                    '2 weeks',  'https://developer.android.com/distribute', 'Google Play Publish'),
(6, 6, 'Cross Platform with Flutter','Build once, deploy on both Android and iOS',                            '5 weeks',  'https://flutter.dev/learn', 'Flutter Official'),
(6, 7, 'Launch Your App',           'Build a real app, publish it, get your first users',                     '6 weeks',  'https://play.google.com/console/', 'Play Console');


-- ── SENIOR ADVICE ───────────────────────────────────────────
INSERT INTO SeniorAdvice (career_id, senior_name, current_role, company, university, years_exp, advice_text) VALUES
(1, 'Ahmed Raza',      'Senior Data Scientist',      'Systems Limited',    'NUST',    4, 'Start with small datasets and real problems. Kaggle competitions are the fastest way to go from theory to practice. Don''t wait until you know everything — your first model will be terrible and that is completely fine.'),
(1, 'Sara Khan',       'Data Analyst',               'Telenor Pakistan',   'LUMS',    2, 'SQL is underrated. Before chasing machine learning, master SQL deeply. 80% of your actual job will be querying and cleaning data, not building neural networks.'),
(2, 'Umar Farooq',     'Software Engineer',          'Arbisoft',           'FAST',    5, 'Learn to read other people''s code. Most of your career you will maintain existing systems, not build from scratch. Get comfortable with messy codebases early.'),
(2, 'Hira Baig',       'Backend Developer',          '10Pearls',           'NUST',    3, 'Git and communication skills matter more than you think. Technical skills get you the interview. Teamwork and clear communication get you the promotion.'),
(3, 'Bilal Tariq',     'Cyber Security Analyst',     'NTC Pakistan',       'COMSATS', 6, 'Do CTF challenges from day one. picoCTF, TryHackMe, Hack The Box — these teach you real attack and defense thinking that no textbook can. Start as soon as possible.'),
(3, 'Nadia Malik',     'Penetration Tester',         'Folio3',             'GIKI',    4, 'Get your CompTIA Security+ before graduating. It signals to employers that you are serious. Pair it with one CTF win on your CV and doors will open.'),
(4, 'Dr. Kamran Ali',  'AI Research Engineer',       'i2c Inc',            'NUST',    7, 'Mathematics is the foundation everything else builds on. If your linear algebra and calculus are weak, fix that before anything else. The math is what separates engineers from practitioners.'),
(4, 'Zainab Hussain',  'ML Engineer',                'Contour Software',   'LUMS',    3, 'Deploy something. Anyone can run a Jupyter notebook. Build an API around your model, host it, share the link. That one deployed project will stand out in 100 CVs.'),
(5, 'Ali Hassan',      'Frontend Developer',         'Clickmasters',       'UET',     4, 'Build projects immediately. Stop watching tutorials endlessly. Watch once, then close it and build something from memory. Getting stuck and solving it yourself is where the real learning happens.'),
(5, 'Maham Siddiqui',  'Full Stack Developer',       'Techverx',           'FAST',    3, 'Learn to read documentation. Google and Stack Overflow will get you far but learning to read official docs directly will make you 10x faster than your peers.'),
(6, 'Hamza Iqbal',     'Android Developer',          'Gaditek',            'NUST',    5, 'Ship your first app even if it is simple. A calculator, a to-do list, anything. The process of going from zero to published on the Play Store teaches you more than any course.'),
(6, 'Fatima Zahra',    'Flutter Developer',          'VentureDive',        'COMSATS', 3, 'Learn Flutter early. Writing one codebase that runs on both Android and iOS is a massive productivity advantage and companies are actively hiring for it.');


-- ── BADGES ──────────────────────────────────────────────────
INSERT INTO Badge (name, description, icon_path, xp_reward, condition_type, condition_value, career_id) VALUES
-- Global badges
('First Step',          'Complete your very first game',                    'images/badges/first_step.png',     25,  'games_played',  1,   NULL),
('On Fire',             'Maintain a 3-day login streak',                    'images/badges/on_fire.png',        50,  'streak',        3,   NULL),
('Dedicated',           'Maintain a 7-day login streak',                    'images/badges/dedicated.png',      100, 'streak',        7,   NULL),
('Unstoppable',         'Maintain a 30-day login streak',                   'images/badges/unstoppable.png',    500, 'streak',        30,  NULL),
('Explorer',            'Play games from 3 different career paths',         'images/badges/explorer.png',       150, 'careers_tried', 3,   NULL),
('Adventurer',          'Play games from all 6 career paths',               'images/badges/adventurer.png',     300, 'careers_tried', 6,   NULL),
('Sharp Shooter',       'Score above 90% in any single game',               'images/badges/sharp_shooter.png',  100, 'score_above',   90,  NULL),
('Perfectionist',       'Score 100% in any single game',                    'images/badges/perfectionist.png',  200, 'score_above',   100, NULL),
('Speed Demon',         'Complete a game in under half the time limit',     'images/badges/speed_demon.png',    100, 'speed',         50,  NULL),
('Grinder',             'Play 25 games total',                              'images/badges/grinder.png',        200, 'games_played',  25,  NULL),
-- Career-specific badges
('Data Rookie',         'Complete all Data Science games',                  'images/badges/data_rookie.png',    150, 'career_complete', 1, 1),
('Code Cadet',          'Complete all Software Engineering games',          'images/badges/code_cadet.png',     150, 'career_complete', 1, 2),
('Security Novice',     'Complete all Cyber Security games',                'images/badges/sec_novice.png',     150, 'career_complete', 1, 3),
('AI Apprentice',       'Complete all AI games',                            'images/badges/ai_apprentice.png',  150, 'career_complete', 1, 4),
('Web Weaver',          'Complete all Web Development games',               'images/badges/web_weaver.png',     150, 'career_complete', 1, 5),
('App Starter',         'Complete all Mobile Development games',            'images/badges/app_starter.png',    150, 'career_complete', 1, 6),
('Data Master',         'Score above 85% average across all Data Science games',  'images/badges/data_master.png',   300, 'career_mastery', 85, 1),
('Code Master',         'Score above 85% average across all SE games',            'images/badges/code_master.png',   300, 'career_mastery', 85, 2),
('Security Master',     'Score above 85% average across all Cyber Security games','images/badges/sec_master.png',    300, 'career_mastery', 85, 3),
('AI Master',           'Score above 85% average across all AI games',            'images/badges/ai_master.png',     300, 'career_mastery', 85, 4),
('Web Master',          'Score above 85% average across all Web Dev games',       'images/badges/web_master.png',    300, 'career_mastery', 85, 5),
('Mobile Master',       'Score above 85% average across all Mobile Dev games',    'images/badges/mobile_master.png', 300, 'career_mastery', 85, 6);


-- ── QUESTS ──────────────────────────────────────────────────
INSERT INTO Quest (title, description, quest_type, condition_type, condition_value, xp_reward) VALUES
('Daily Warm-Up',       'Play any 1 game today',                        'daily',    'play_games',   1,   50),
('Daily Challenger',    'Play 3 games today',                           'daily',    'play_games',   3,   150),
('Score Hunter',        'Score above 80% in any game today',            'daily',    'score_above',  80,  100),
('Weekly Explorer',     'Try games from 3 different careers this week', 'weekly',   'careers_tried',3,   300),
('Weekly Grinder',      'Play 10 games this week',                      'weekly',   'play_games',   10,  400),
('Perfect Week',        'Score above 90% in 5 games this week',         'weekly',   'high_scores',  5,   500),
('First Game',          'Play your very first game',                    'one-time', 'play_games',   1,   100),
('Career Explorer',     'Try all 6 career paths',                       'one-time', 'careers_tried',6,   500),
('Data Journey',        'Complete all Data Science games',              'one-time', 'career_complete',1, 300),
('Code Journey',        'Complete all Software Engineering games',      'one-time', 'career_complete',2, 300),
('Security Journey',    'Complete all Cyber Security games',            'one-time', 'career_complete',3, 300),
('AI Journey',          'Complete all AI games',                        'one-time', 'career_complete',4, 300),
('Web Journey',         'Complete all Web Development games',           'one-time', 'career_complete',5, 300),
('Mobile Journey',      'Complete all Mobile Development games',        'one-time', 'career_complete',6, 300);