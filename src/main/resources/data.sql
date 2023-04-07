DROP TABLE IF EXISTS login;
CREATE TABLE login (id INTEGER PRIMARY KEY, username VARCHAR(64) NOT NULL, password VARCHAR(64) NOT NULL);

INSERT INTO login (id, username, password) VALUES
  (1, 'testone@email.com', '$2a$10$v9CUp5LIVRRebjfngXLHg.Xc7BQCCrAv6m7Bki3EQYVt7WMcIRu4K'),
  (2, 'testetwo@email.com', '$2a$10$v9CUp5LIVRRebjfngXLHg.Xc7BQCCrAv6m7Bki3EQYVt7WMcIRu4K');