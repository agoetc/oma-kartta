# users schema

# --- !Ups
grant all privileges on *.* to user@'%' IDENTIFIED BY 'Password' WITH GRANT OPTION;

CREATE TABLE users (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

# --- !Downs
drop table users