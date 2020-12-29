DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS  projects;

DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS city_seq;
DROP SEQUENCE IF EXISTS groups_seq;
DROP SEQUENCE IF EXISTS projects_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_type AS ENUM ('FINISHED','CURRENT');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE city_seq START 100000;
CREATE SEQUENCE groups_seq START 100000;
CREATE SEQUENCE projects_seq START 100000;

CREATE TABLE cities (
  id            INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  name          TEXT NOT NULL,
  short_name    TEXT NOT NULL
);
CREATE UNIQUE INDEX city_name_idx ON cities (name);
CREATE UNIQUE INDEX city_short_name_idx ON cities (short_name);

CREATE TABLE groups (
  id        INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
  name      TEXT NOT NULL,
  type      group_type NOT NULL
);

CREATE TABLE projects (
   id           INTEGER PRIMARY KEY DEFAULT nextval('projects_seq'),
   description  TEXT NOT NULL
);

CREATE TABLE users (
    id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    full_name TEXT NOT NULL,
    email     TEXT NOT NULL,
    flag      user_flag NOT NULL,
    city_id   INTEGER REFERENCES cities(id),
    group_id  INTEGER REFERENCES groups(id)
);
CREATE UNIQUE INDEX email_idx ON users (email);