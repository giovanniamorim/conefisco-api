CREATE TABLE password_reset_token (
  id BIGINT NOT NULL AUTO_INCREMENT,
  token VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL,
  expiry_date TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (token),
  FOREIGN KEY (user_id) REFERENCES user(id)
);
