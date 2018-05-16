# kartalla schema

# --- !Ups
CREATE TABLE kartalla (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  restaurant_id INT NOT NULL,
  user_id VARCHAR(255) NOT NULL,
  star int(2) NOT NULL,
  sana VARCHAR(255) NOT NULL ,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp
);

# --- !Downs
drop table kartalla