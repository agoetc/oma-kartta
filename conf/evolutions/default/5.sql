# relation schema

# --- !Ups
CREATE TABLE relation (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  follow_id VARCHAR(255) NOT NULL,
  follower_id VARCHAR(255) NOT NULL
);

# --- !Downs
drop table relation