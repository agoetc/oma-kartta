# user_reviews schema

# --- !Ups
CREATE TABLE user_reviews (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id VARCHAR(255) NOT NULL,
  review VARCHAR(255) NOT NULL
);

# --- !Downs
drop table user_reviews