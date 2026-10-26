-- V9__insert_sample_data.sql
-- Purpose: Inserts comprehensive demonstration records for Atrang 2026 Fest DBMS Laboratory viva
-- Note: All events, dates, and prizes are structured demonstration data for academic laboratory evaluation.

-- 1. Insert 10 Administrator Users
-- Password hash for 'admin123': $2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda
INSERT INTO admin_users (username, password_hash, full_name, email, role, is_active) VALUES
('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Chief Technical Administrator', 'admin@atrang.iiitt.ac.in', 'ROLE_SUPERADMIN', TRUE),
('shreyash_lead', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Shreyash Backend Lead', 'shreyash@iiitt.ac.in', 'ROLE_ADMIN', TRUE),
('yogesh_db', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Yogesh Database Architect', 'yogesh@iiitt.ac.in', 'ROLE_ADMIN', TRUE),
('madhav_ui', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Madhav Frontend Specialist', 'madhav@iiitt.ac.in', 'ROLE_ADMIN', TRUE),
('coord_gaming', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Esports Lead Coordinator', 'gaming.coord@atrang.iiitt.ac.in', 'ROLE_COORDINATOR', TRUE),
('coord_coding', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Algorithmic Arena Coordinator', 'coding.coord@atrang.iiitt.ac.in', 'ROLE_COORDINATOR', TRUE),
('coord_robotics', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Robotics & Hardware Incharge', 'robotics.coord@atrang.iiitt.ac.in', 'ROLE_COORDINATOR', TRUE),
('coord_design', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'UI/UX & Creative Coordinator', 'design.coord@atrang.iiitt.ac.in', 'ROLE_COORDINATOR', TRUE),
('faculty_incharge', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Dr. Faculty Advisor', 'advisor@iiitt.ac.in', 'ROLE_SUPERADMIN', TRUE),
('audit_viewer', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTkFLKda', 'Quality Assurance Auditor', 'auditor@atrang.iiitt.ac.in', 'ROLE_ADMIN', TRUE);

-- 2. Insert 10 Event Categories (Minecraft Biome & Technical Fest Domains)
INSERT INTO event_categories (code, name, description, color_hex, icon_name, display_order, is_active) VALUES
('coding', 'Hackers'' Hollow', 'Competitive programming, algorithmic showdowns, and live bug-hunting duels.', '#a46bff', 'Terminal', 1, TRUE),
('gaming', 'Pixel Fortress', 'LAN tournaments, Valorant clutches, BGMI skirmishes, and Minecraft bedwars.', '#f97316', 'Gamepad2', 2, TRUE),
('webdev', 'Devs'' Den', 'Full-stack hackathons, rapid web prototyping, and pixel-perfect design sprints.', '#38bdf8', 'Code2', 3, TRUE),
('robotics', 'Redstone Realm', 'Autonomous line-followers, robo-soccer, and microcontroller combat leagues.', '#ef4444', 'Cpu', 4, TRUE),
('ai-ml', 'Neural Nether', 'Deep learning challenges, NLP hackathons, and generative model benchmarks.', '#8b5cf6', 'BrainCircuit', 5, TRUE),
('cybersecurity', 'Cipher Keep', 'Capture The Flag (CTF), reverse engineering, cryptography, and network forensics.', '#10b981', 'ShieldAlert', 6, TRUE),
('design', 'Artisan''s Atelier', 'UI/UX redesign contests, Minecraft voxel modeling, and digital creative showcase.', '#ec4899', 'Palette', 7, TRUE),
('quizzing', 'Triva Tundra', 'General tech trivia, pop-culture fandom battles, and sci-tech quiz bowls.', '#eab308', 'HelpCircle', 8, TRUE),
('open-source', 'Bazaar of Blocks', 'Open-source contribution marathons, documentation sprints, and Git masterclasses.', '#06b6d4', 'GitBranch', 9, TRUE),
('management', 'Tycoon Terrace', 'Product management pitches, tech venture case competitions, and strategy war games.', '#14b8a6', 'Briefcase', 10, TRUE);

-- 3. Insert 12 Comprehensive Demonstration Events
INSERT INTO events (
    category_id, slug, title, summary, description, organizer, participation, 
    team_size, duration, prize, venue, eligibility, fee, registration_url, 
    registration_deadline, is_published, display_order
) VALUES
(
    1, 'speed-debug-2026', 'Speed Debugging Clash',
    'Find and patch memory leaks, off-by-one errors, and segfaults against a 45-minute ticking timer.',
    'Test your command over C++, Python, and Java. Participants are given obfuscated codebases with intentional bugs and failing unit tests. Points awarded for test suite completion and execution speed.',
    'Quantum Coding Club', 'Individual', '1 member', '2 hours', '₹10,000 + Tech Merch',
    'Computer Lab 2, Academic Block', 'Undergraduate students', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '10 days', TRUE, 1
),
(
    1, 'code-craft-hackathon', 'CodeCraft 24-Hour Hackathon',
    'Design and deploy full-stack community-driven applications powered by modern open-source stacks.',
    'Flagship hackathon of Atrang. Teams conceptualize, build, and deploy working web or mobile solutions addressing campus life, sustainability, or education. Mentorship checkpoints throughout the night.',
    'Developer Student Club', 'Team', '2 - 4 members', '24 hours', '₹35,000 + Cloud Credits',
    'Central Auditorium & Seminar Hall', 'Open to all college students', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '12 days', TRUE, 2
),
(
    2, 'valorant-clutch-cup', 'Valorant Tactical Shootout',
    'High-stakes 5v5 tactical shooter showdown on dedicated LAN servers with live shoutcasting.',
    'Standard tournament rules on Ascent, Bind, and Haven. Double elimination bracket with professional tournament organizers ensuring ping parity and fair competitive conditions.',
    'IIITT Esports Committee', 'Team', '5 members + 1 sub', '6 hours', '₹20,000 + Gaming Gear',
    'Recreation Hall & LAN Zone', 'All registered fest participants', '₹200 per team', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '8 days', TRUE, 3
),
(
    2, 'minecraft-speedbuild', 'Voxel Architects: Minecraft Build Battle',
    'Construct grand architectural wonders, functional redstone machinery, and fantasy terrains within 90 minutes.',
    'Competitors play on a custom vanilla server with world-edit disabled. A secret prompt is announced at the start. Evaluated on aesthetic appeal, thematic fidelity, and creativity.',
    'Design & Gaming Society', 'Individual or Team', '1 - 2 members', '90 mins', '₹8,000 + Custom Trophy',
    'Virtual Server / Computer Lab 1', 'Open to all', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '9 days', TRUE, 4
),
(
    3, 'web-sprint-duel', 'CSS Battle & Web Sprint',
    'Recreate pixel-perfect responsive layouts using vanilla CSS within minutes without inspector tools.',
    'Fast-paced visual challenges where participants race to achieve the highest visual diff match percentage against reference designs under strict time limits.',
    'Web Development Guild', 'Individual', '1 member', '2 hours', '₹7,500 + Certificate',
    'Lab 3, Department of CSE', 'Open to all college students', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '11 days', TRUE, 5
),
(
    4, 'robo-maze-runner', 'Autonomous Line Follower Derby',
    'Program autonomous micro-rovers to traverse convoluted tracks, 90-degree hairpin turns, and elevation ramps.',
    'Robots must be completely self-navigating using onboard infrared or camera arrays. Time penalties apply for track departure. The fastest three completion times advance to finals.',
    'Robotics & Automation Society', 'Team', '2 - 3 members', '4 hours', '₹15,000 + Sensor Kits',
    'Indoor Sports Complex', 'Engineering students', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '10 days', TRUE, 6
),
(
    5, 'neural-hack-bench', 'Kaggle Sprint: Predictive Modeling',
    'Analyze multimodal datasets, engineer features, and train predictive models to maximize ROC-AUC.',
    'Participants receive an anonymized real-world dataset at 9:00 AM. Submissions evaluated against a hidden test set. Top performers give a 5-minute lightning presentation on model interpretability.',
    'AI Research Collective', 'Individual or Team', '1 - 2 members', '5 hours', '₹12,000 + Compute Credits',
    'Smart Class 104', 'Undergraduates & Postgraduates', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '12 days', TRUE, 7
),
(
    6, 'flag-hunt-ctf', 'OverTheWire Capture The Flag',
    'Jeopardy-style cyber security competition spanning web exploits, reverse engineering, and binary puzzles.',
    'Categories include Web Security, Binary Exploitation, Cryptography, OSINT, and Digital Forensics. Dynamic scoring ensures balanced competition across all difficulty tiers.',
    'CyberSec Defense Club', 'Team', '1 - 3 members', '8 hours', '₹18,000 + Security Vouchers',
    'Online & Lab 4', 'Open to all', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '7 days', TRUE, 8
),
(
    7, 'ui-ux-designathon', 'Figma Wireframe Sprint',
    'Transform raw product briefs into intuitive, accessible mobile app prototypes within 3 hours.',
    'Teams are provided with user personas and edge cases. Judged by senior industry designers on usability heuristics, design system consistency, and interaction delight.',
    'Creative & Media Wing', 'Individual or Team', '1 - 2 members', '3 hours', '₹9,000 + Subscriptions',
    'Design Studio, 2nd Floor', 'All creative minds', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '9 days', TRUE, 9
),
(
    8, 'tech-trivia-clash', 'Mega Tech & Sci-Fi Quiz',
    'Fast-paced buzzer rounds, audio-visual audio clues, and tech history brain-teasers.',
    'Written preliminary elimination followed by a 6-team stage final with pounce-and-bounce scoring rules hosted by seasoned quizmasters.',
    'Literary & Quiz Society', 'Team', '2 members', '3 hours', '₹10,000 + Books',
    'Main Seminar Hall', 'Open to all collegiate teams', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '6 days', TRUE, 10
),
(
    9, 'open-source-jam', 'Git Mastery & PR Jam',
    'Contribute documentation, test coverage, and bugfixes to curated open-source repositories.',
    'Live workshop followed by a 4-hour contribution sprint. Maintainers review and merge pull requests on site.',
    'Open Source Society', 'Individual', '1 member', '4 hours', '₹6,000 + Swag Packs',
    'Computer Lab 1', 'Open to all', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '13 days', FALSE, 11
),
(
    10, 'pitch-the-product', 'Tech Venture Elevator Pitch',
    'Pitch innovative hardware or software prototypes to an esteemed panel of angel investors and professors.',
    '5 minutes pitch followed by 3 minutes rapid Q&A. Pitch decks must outline problem-market fit, unit economics, and technical viability.',
    'E-Cell IIIT Tiruchirappalli', 'Team', '2 - 4 members', '3 hours', '₹25,000 Seed Grant',
    'Conference Room Alpha', 'All student startups', 'Free', 'https://unstop.com',
    CURRENT_TIMESTAMP + INTERVAL '14 days', FALSE, 12
);

-- 4. Insert 15 Event Sessions (Timetable / Day-wise Slots)
INSERT INTO event_sessions (event_id, fest_day, start_time, end_time, session_title, venue, details) VALUES
(1, 1, '10:00 AM', '11:00 AM', 'Round 1 — Bug Swarm Elimination', 'Computer Lab 2', 'C++ and Python short debugging puzzles'),
(1, 1, '11:30 AM', '12:30 PM', 'Grand Finale — Segfault Showdown', 'Computer Lab 2', 'Complex multi-threaded systems debugging'),
(2, 1, '11:00 AM', '12:00 PM', 'Problem Statement Unveiling', 'Main Auditorium', 'Theme release and rules walkthrough'),
(2, 1, '06:00 PM', '07:30 PM', 'Midway Architecture Review', 'Seminar Hall', 'Mentors evaluate project schematics'),
(2, 2, '10:00 AM', '12:00 PM', 'Final Demos & Jury Deliberation', 'Auditorium Stage', 'Top 8 teams present live deployments'),
(3, 1, '02:00 PM', '05:00 PM', 'Group Stages & Qualifiers', 'LAN Zone', 'Best of 1 matches across 4 brackets'),
(3, 2, '03:00 PM', '06:00 PM', 'Grand Finals & Live Cast', 'Auditorium Big Screen', 'Best of 3 series with audience viewing'),
(4, 2, '01:30 PM', '03:00 PM', 'Theme Reveal & Live Building', 'Lab 1', 'Secret theme revealed on big screen'),
(5, 1, '02:00 PM', '04:00 PM', 'CSS Battle Sprint', 'Lab 3', 'Live layout reproduction round'),
(6, 2, '10:00 AM', '01:00 PM', 'Qualifying Time Trials', 'Sports Complex', 'Solo time trial on track A and B'),
(6, 2, '02:30 PM', '04:00 PM', 'Final Circuit Race', 'Sports Complex', 'High-speed duel on championship track'),
(7, 2, '09:00 AM', '02:00 PM', 'Model Training Sprint', 'Smart Class 104', 'Continuous Kaggle leaderboard updates'),
(8, 2, '10:00 AM', '06:00 PM', '24-Hour CTF Challenge', 'Lab 4 / Remote', 'Challenges open across 5 categories'),
(9, 1, '03:00 PM', '06:00 PM', 'Wireframe Prototyping', 'Design Studio', 'Hands-on Figma design sprint'),
(10, 1, '04:00 PM', '07:00 PM', 'Stage Finals & Buzzer Round', 'Main Seminar Hall', '6 finalist teams compete on stage');

-- 5. Insert Initial Audit Log Records
INSERT INTO event_audit_logs (event_id, admin_id, action, details, performed_by, created_at) VALUES
(1, 1, 'CREATE', 'Initial seed creation of Speed Debugging event', 'ADMIN_SEED', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 1, 'PUBLISH', 'Published Speed Debugging for fest release', 'ADMIN_SEED', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(2, 2, 'CREATE', 'Initial seed creation of CodeCraft Hackathon', 'SHREYASH', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(2, 2, 'PUBLISH', 'Published CodeCraft Hackathon', 'SHREYASH', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(3, 5, 'CREATE', 'Initial seed creation of Valorant Clutch Cup', 'GAMING_LEAD', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(3, 5, 'PUBLISH', 'Published Valorant Tournament', 'GAMING_LEAD', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 4, 'CREATE', 'Initial seed creation of Minecraft Build Battle', 'MADHAV', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(4, 4, 'PUBLISH', 'Published Minecraft Build Battle', 'MADHAV', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(6, 7, 'CREATE', 'Initial seed creation of Robo Maze Runner', 'ROBOTICS_LEAD', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(6, 7, 'PUBLISH', 'Published Robo Maze Runner', 'ROBOTICS_LEAD', CURRENT_TIMESTAMP - INTERVAL '1 day');
