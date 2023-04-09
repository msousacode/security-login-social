DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    name VARCHAR(64),
    email VARCHAR(64) NOT NULL,
    password VARCHAR(64),
    image_url VARCHAR(150),
    email_verified boolean DEFAULT false,
    provider VARCHAR(20));

INSERT INTO users (id, email, password) VALUES
  (1, 'testone@email.com', '$2a$10$v9CUp5LIVRRebjfngXLHg.Xc7BQCCrAv6m7Bki3EQYVt7WMcIRu4K'),
  (2, 'testetwo@email.com', '$2a$10$v9CUp5LIVRRebjfngXLHg.Xc7BQCCrAv6m7Bki3EQYVt7WMcIRu4K');