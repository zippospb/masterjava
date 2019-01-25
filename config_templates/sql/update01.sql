CREATE TYPE group_type AS ENUM('REGISTERING','CURRENT','FINISHED');

CREATE TABLE cities (
  ref       TEXT PRIMARY KEY,
  name      TEXT NOT NULL
);

CREATE TABLE projects (
  id        INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name      TEXT NOT NULL,
  description TEXT NOT NULL
);

CREATE TABLE groups (
  id        INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  name      TEXT NOT NULL,
  type      GROUP_TYPE NOT NULL,
  project_id INTEGER NOT NULL REFERENCES projects(id)
);

ALTER TABLE users ADD COLUMN city_ref TEXT REFERENCES city (ref) ON UPDATE CASCADE;