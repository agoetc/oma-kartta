# paikka schema

# --- !Ups
CREATE TABLE paikka (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id VARCHAR(20) NOT NULL,
  name VARCHAR(255) NOT NULL,
  kana VARCHAR(255) NOT NULL,
  tag VARCHAR(20) NOT NULL,
  text VARCHAR(255),
  postal_code VARCHAR(8) NOT NULL,
  address VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

# --- !Downs
drop table paikka