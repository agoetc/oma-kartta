grant all privileges on *.* to user@'%' IDENTIFIED BY 'Password' WITH GRANT OPTION;

CREATE TABLE users (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE karttana (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  restaurant_id INT NOT NULL,
  user_id VARCHAR(255) NOT NULL,
  star int(2) NOT NULL,
  sana VARCHAR(255) NOT NULL ,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp
);

CREATE TABLE user_reviews (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id VARCHAR(255) NOT NULL,
  review VARCHAR(255) NOT NULL
);

CREATE TABLE restaurants (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  kana VARCHAR(255) NOT NULL,
  text VARCHAR(255),
  postal_code VARCHAR(8) NOT NULL,
  address VARCHAR(255) NOT NULL
);

CREATE TABLE relation (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  follow_id VARCHAR(255) NOT NULL,
  follower_id VARCHAR(255) NOT NULL
);