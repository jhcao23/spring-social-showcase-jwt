DROP TABLE IF EXISTS `User`;
CREATE TABLE `User`
(
   id       BIGINT         NOT NULL AUTO_INCREMENT,
   hash_id  VARCHAR(255)   NOT NULL,
   PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uc_User_hash  ON `User` (hash_id ASC);

DROP TABLE IF EXISTS Account;
CREATE TABLE Account
(
   id          BIGINT         NOT NULL AUTO_INCREMENT,
   user_id     BIGINT            NOT NULL,
   username    VARCHAR(255)   NOT NULL,
   password    VARCHAR(255)   NOT NULL,
   first_name  VARCHAR(255),
   last_name   VARCHAR(255),
   PRIMARY KEY (id)
);
ALTER TABLE Account
  ADD CONSTRAINT fk_Account_userId FOREIGN KEY (user_id)
  REFERENCES `User` (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;
CREATE INDEX user_id
   ON Account (user_id ASC);
CREATE UNIQUE INDEX uc_Account_username
   ON Account (username ASC);

DROP TABLE IF EXISTS User_Connection;
CREATE TABLE User_Connection
(
   id                BIGINT         NOT NULL AUTO_INCREMENT,
   user_id           BIGINT         NOT NULL,
   provider_id       VARCHAR(255)   NOT NULL,
   provider_user_id  VARCHAR(255)   NOT NULL,
   rank              INT            DEFAULT 0 NOT NULL,
   display_name      VARCHAR(255),
   profile_url       VARCHAR(255),
   image_url         VARCHAR(255),
   access_token      VARCHAR(255),
   secret            VARCHAR(255),
   refresh_token     VARCHAR(255),
   expire_time       BIGINT,
   PRIMARY KEY (id)
);
ALTER TABLE User_Connection
  ADD CONSTRAINT user_connection_ibfk_1 FOREIGN KEY (user_id)
  REFERENCES `User` (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;
CREATE UNIQUE INDEX uc_User_Connection_UserConnectionRank
   ON User_Connection (user_id ASC, provider_id ASC, rank ASC);
CREATE UNIQUE INDEX uc_User_Connection_UserConnectionId
   ON User_Connection (user_id ASC, provider_id ASC, provider_user_id ASC);

DROP TABLE IF EXISTS User_Connection_Wechat;
CREATE TABLE User_Connection_Wechat (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    app_id          VARCHAR(255)    NOT NULL,
    open_id         VARCHAR(255)    NOT NULL,
    union_id        VARCHAR(255)            ,
    subscribe       TINYINT                 ,
    session_key     VARCHAR(255)            ,
    expires         BIGINT                  ,
    nickname        VARCHAR(255)            ,
    sex             TINYINT                 ,
    city            VARCHAR(255)            ,
    province        VARCHAR(255)            ,
    country         VARCHAR(255)            ,
    language        VARCHAR(100)            ,
    avatar_Url      VARCHAR(255)            ,
    subscribe_time  BIGINT                  ,
    remark          VARCHAR(255)            ,
    PRIMARY KEY (id)
);
ALTER TABLE User_Connection_Wechat
  ADD CONSTRAINT user_connection_wechat_user FOREIGN KEY (user_id)
  REFERENCES `User` (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;    
CREATE UNIQUE INDEX uc_User_Connection_Wechat_UserApp
    ON User_Connection_Wechat (user_id ASC, app_id ASC);
CREATE UNIQUE INDEX uc_User_Connection_Wechat_AppOpenId
    ON User_Connection_Wechat (open_id ASC, app_id ASC);


DROP TABLE IF EXISTS Authority;
CREATE TABLE Authority (
    id              INT             NOT NULL,
    authority_name  VARCHAR(100)    NOT NULL,
    description     VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uc_Authority_AuthorityName ON Authority (authority_name ASC);
INSERT INTO Authority (id, authority_name, description) VALUES (1, 'ROLE_USER', 'general user');
INSERT INTO Authority (id, authority_name, description) VALUES (2, 'ROLE_ADMIN', 'site admin');

DROP TABLE IF EXISTS User_Authority;
CREATE TABLE User_Authority (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    authority_id    INT             NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uc_User_Authority 
    ON User_Authority (user_id ASC, authority_id ASC);
ALTER TABLE User_Authority 
    ADD CONSTRAINT fk_User_Authority_User
    FOREIGN KEY (user_id) REFERENCES `User` (id);
ALTER TABLE User_Authority 
    ADD CONSTRAINT fk_User_Authority_Authority
    FOREIGN KEY (authority_id) REFERENCES Authority (id);

COMMIT;