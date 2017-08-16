-- MySQL Workbench Forward Engineering

USE `spring_social_test` ;

-- -----------------------------------------------------
-- Table `Touch_User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Touch_User` ;

CREATE TABLE IF NOT EXISTS `Touch_User` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `hash_id` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT NULL,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Account` ;

CREATE TABLE IF NOT EXISTS `Account` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `cell` VARCHAR(255) NULL DEFAULT NULL COMMENT 'register|login cell|email is different from contact methods!',
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `user_id`),
  CONSTRAINT `fk_Account_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `uq_email` ON `Account` (`email` ASC);

CREATE UNIQUE INDEX `uq_Account_User` ON `Account` (`user_id` ASC);

CREATE INDEX `fk_Account_User1_idx` ON `Account` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `Address_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Address_Type` ;

CREATE TABLE IF NOT EXISTS `Address_Type` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL COMMENT 'could be home, work, head office, client, branch#0+, ',
  `code` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Country`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Country` ;

CREATE TABLE IF NOT EXISTS `Country` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `phone_code` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Province`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Province` ;

CREATE TABLE IF NOT EXISTS `Province` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `territory` TINYINT(1) NULL DEFAULT NULL,
  `country_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Province_Country1`
    FOREIGN KEY (`country_id`)
    REFERENCES `Country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Province_Country1_idx` ON `Province` (`country_id` ASC);


-- -----------------------------------------------------
-- Table `City`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `City` ;

CREATE TABLE IF NOT EXISTS `City` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `province_id` BIGINT(20) NOT NULL,
  `territory` TINYINT(1) NULL DEFAULT NULL,
  `country_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_City_Country1`
    FOREIGN KEY (`country_id`)
    REFERENCES `Country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_City_Province1`
    FOREIGN KEY (`province_id`)
    REFERENCES `Province` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_City_Province1_idx` ON `City` (`province_id` ASC);

CREATE INDEX `fk_City_Country1_idx` ON `City` (`country_id` ASC);


-- -----------------------------------------------------
-- Table `Community`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Community` ;

CREATE TABLE IF NOT EXISTS `Community` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  `zone` VARCHAR(255) NULL DEFAULT NULL,
  `city_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Community_City1`
    FOREIGN KEY (`city_id`)
    REFERENCES `City` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Community_City1_idx` ON `Community` (`city_id` ASC);


-- -----------------------------------------------------
-- Table `Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Address` ;

CREATE TABLE IF NOT EXISTS `Address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `suite` VARCHAR(45) NULL DEFAULT NULL,
  `building_name` VARCHAR(45) NULL DEFAULT NULL,
  `community` VARCHAR(45) NULL DEFAULT NULL,
  `district` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `state` VARCHAR(255) NULL DEFAULT NULL,
  `country` VARCHAR(255) NULL DEFAULT NULL,
  `postal_code` VARCHAR(20) NULL DEFAULT NULL,
  `country_id` BIGINT(20) NOT NULL,
  `province_id` BIGINT(20) NOT NULL,
  `city_id` BIGINT(20) NOT NULL,
  `community_id` BIGINT(20) NOT NULL,
  `type_id` INT(11) NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Address_Address_Type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `Address_Type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Address_City1`
    FOREIGN KEY (`city_id`)
    REFERENCES `City` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Address_Community1`
    FOREIGN KEY (`community_id`)
    REFERENCES `Community` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Address_Country1`
    FOREIGN KEY (`country_id`)
    REFERENCES `Country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Address_Province1`
    FOREIGN KEY (`province_id`)
    REFERENCES `Province` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Address_Country1_idx` ON `Address` (`country_id` ASC);

CREATE INDEX `fk_Address_Province1_idx` ON `Address` (`province_id` ASC);

CREATE INDEX `fk_Address_City1_idx` ON `Address` (`city_id` ASC);

CREATE INDEX `fk_Address_Community1_idx` ON `Address` (`community_id` ASC);

CREATE INDEX `fk_Address_Address_Type1_idx` ON `Address` (`type_id` ASC);


-- -----------------------------------------------------
-- Table `Authority`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Authority` ;

CREATE TABLE IF NOT EXISTS `Authority` (
  `id` INT NOT NULL,
  `name` VARCHAR(50) NOT NULL COMMENT 'could be Driver, Driver Team, Rider, Inventory[gas station, convenient store, small warehouse, private garage], buyer, seller, packer, Sender[personal|business], Receiver, Deliver',
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Contact` ;

CREATE TABLE IF NOT EXISTS `Contact` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT(20) NOT NULL,
  `firstname` VARCHAR(255) NULL DEFAULT NULL,
  `lastname` VARCHAR(255) NULL DEFAULT NULL,
  `gender` INT(11) NULL DEFAULT NULL,
  `avatar` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Contact_Touch_User1`
    FOREIGN KEY (`owner_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Contact_Touch_User1_idx` ON `Contact` (`owner_id` ASC);


-- -----------------------------------------------------
-- Table `ContactType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ContactType` ;

CREATE TABLE IF NOT EXISTS `ContactType` (
  `id` INT(11) NOT NULL COMMENT '	',
  `name` VARCHAR(45) NULL DEFAULT NULL COMMENT 'email, cellphone, home phone, work phone with extension',
  `pattern` VARCHAR(45) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ContactInfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ContactInfo` ;

CREATE TABLE IF NOT EXISTS `ContactInfo` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `contact` VARCHAR(255) NULL DEFAULT NULL,
  `extension` VARCHAR(255) NULL DEFAULT NULL,
  `contact_id` BIGINT(20) NOT NULL,
  `rank` INT(11) NULL DEFAULT NULL,
  `type_id` INT(11) NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_ContactInfo_Contact1`
    FOREIGN KEY (`contact_id`)
    REFERENCES `Contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ContactInfo_Contact_Type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `ContactType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_ContactInfo_Contact_Type1_idx` ON `ContactInfo` (`type_id` ASC);

CREATE INDEX `fk_ContactInfo_Contact1_idx` ON `ContactInfo` (`contact_id` ASC);


-- -----------------------------------------------------
-- Table `Contact_Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Contact_Address` ;

CREATE TABLE IF NOT EXISTS `Contact_Address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `contact_id` BIGINT(20) NULL DEFAULT NULL,
  `address_id` BIGINT(20) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Contact_Address1_Address1`
    FOREIGN KEY (`address_id`)
    REFERENCES `Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Contact_Address1_Contact1`
    FOREIGN KEY (`contact_id`)
    REFERENCES `Contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `address_id_UNIQUE` ON `Contact_Address` (`address_id` ASC);

CREATE INDEX `fk_Contact_Address1_Address1_idx` ON `Contact_Address` (`address_id` ASC);

CREATE INDEX `fk_Contact_Address1_Contact1_idx` ON `Contact_Address` (`contact_id` ASC);


-- -----------------------------------------------------
-- Table `Coporate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Coporate` ;

CREATE TABLE IF NOT EXISTS `Coporate` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `address_id` BIGINT(20) NULL DEFAULT NULL,
  `owner_id` BIGINT(20) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Corporate_Address`
    FOREIGN KEY (`address_id`)
    REFERENCES `Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Corporate_Owner`
    FOREIGN KEY (`owner_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Corporate_Address_idx` ON `Coporate` (`address_id` ASC);

CREATE INDEX `fk_Corporate_Owner_idx` ON `Coporate` (`owner_id` ASC);


-- -----------------------------------------------------
-- Table `Corporate_Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Corporate_Address` ;

CREATE TABLE IF NOT EXISTS `Corporate_Address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'temp upper limit  = 50',
  `corporate_id` BIGINT(20) NOT NULL,
  `address_id` BIGINT(20) NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Corporate_Address_Address`
    FOREIGN KEY (`address_id`)
    REFERENCES `Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Corporate_Address_Corporate`
    FOREIGN KEY (`corporate_id`)
    REFERENCES `Coporate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `address_id_UNIQUE` ON `Corporate_Address` (`address_id` ASC);

CREATE INDEX `fk_Corporate_Address_Address_idx` ON `Corporate_Address` (`address_id` ASC);

CREATE INDEX `fk_Corporate_Address_Corporate_idx` ON `Corporate_Address` (`corporate_id` ASC);


-- -----------------------------------------------------
-- Table `Credit_Card`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Credit_Card` ;

CREATE TABLE IF NOT EXISTS `Credit_Card` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT(20) NULL DEFAULT NULL,
  `card_no` VARCHAR(16) NULL DEFAULT NULL,
  `type` INT(11) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `pin` VARCHAR(3) NULL DEFAULT NULL,
  `expiry` VARCHAR(4) NULL DEFAULT NULL,
  `token` VARCHAR(255) NULL DEFAULT NULL,
  `token_expiry` DATETIME NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Credit_Card_Owner`
    FOREIGN KEY (`owner_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Credit_Card_Owner_idx` ON `Credit_Card` (`owner_id` ASC);


-- -----------------------------------------------------
-- Table `Currency`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Currency` ;

CREATE TABLE IF NOT EXISTS `Currency` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Device`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Device` ;

CREATE TABLE IF NOT EXISTS `Device` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `serial` VARCHAR(45) NULL DEFAULT NULL,
  `type` INT(11) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `User_Device`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Device` ;

CREATE TABLE IF NOT EXISTS `User_Device` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `device_id` BIGINT(20) NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Device_Device`
    FOREIGN KEY (`device_id`)
    REFERENCES `Device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_Device_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Device_User_idx` ON `User_Device` (`user_id` ASC);

CREATE INDEX `fk_User_Device_Device_idx` ON `User_Device` (`device_id` ASC);


-- -----------------------------------------------------
-- Table `Device_Track`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Device_Track` ;

CREATE TABLE IF NOT EXISTS `Device_Track` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_device_id` BIGINT(20) NOT NULL,
  `lat` POINT NULL DEFAULT NULL,
  `log` POINT NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Device_Track_User_Device1`
    FOREIGN KEY (`user_device_id`)
    REFERENCES `User_Device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Device_Track_User_Device1_idx` ON `Device_Track` (`user_device_id` ASC);


-- -----------------------------------------------------
-- Table `File`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `File` ;

CREATE TABLE IF NOT EXISTS `File` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `protocol` VARCHAR(45) NULL DEFAULT NULL,
  `file_path` VARCHAR(45) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Document_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Document_Type` ;

CREATE TABLE IF NOT EXISTS `Document_Type` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(255) NOT NULL COMMENT 'could be driver license, passport, criminal report, credit report, corporate article, vehicle insurance, vehicle registration, work permit, citizen card, green card',
  `code` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `code_UNIQUE` ON `Document_Type` (`code` ASC);

CREATE UNIQUE INDEX `name_UNIQUE` ON `Document_Type` (`name` ASC);


-- -----------------------------------------------------
-- Table `Document`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Document` ;

CREATE TABLE IF NOT EXISTS `Document` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT(100) NOT NULL COMMENT 'original uploader',
  `type_id` INT(11) NOT NULL,
  `file_id` BIGINT(20) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_document_file`
    FOREIGN KEY (`file_id`)
    REFERENCES `File` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_document_type`
    FOREIGN KEY (`type_id`)
    REFERENCES `Document_Type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_document_user`
    FOREIGN KEY (`owner_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_document_user_idx` ON `Document` (`owner_id` ASC);

CREATE INDEX `fk_document_type_idx` ON `Document` (`type_id` ASC);

CREATE INDEX `fk_document_file_idx` ON `Document` (`file_id` ASC);


-- -----------------------------------------------------
-- Table `User_Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Address` ;

CREATE TABLE IF NOT EXISTS `User_Address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'temp upper limit = 20',
  `user_id` BIGINT(20) NOT NULL,
  `address_id` BIGINT(20) NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Address_Address`
    FOREIGN KEY (`address_id`)
    REFERENCES `Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_Address_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Address_User1_idx` ON `User_Address` (`user_id` ASC);

CREATE INDEX `fk_User_Address_Address_idx` ON `User_Address` (`address_id` ASC);


-- -----------------------------------------------------
-- Table `User_Authority`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Authority` ;

CREATE TABLE IF NOT EXISTS `User_Authority` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `authority_id` INT NOT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `role_id`
    FOREIGN KEY (`authority_id`)
    REFERENCES `Authority` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Role_Role1_idx` ON `User_Authority` (`authority_id` ASC);

CREATE INDEX `user_id_idx` ON `User_Authority` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Connection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Connection` ;

CREATE TABLE IF NOT EXISTS `User_Connection` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `provider_id` VARCHAR(255) NOT NULL,
  `provider_user_id` VARCHAR(255) NOT NULL,
  `rank` INT(11) NULL DEFAULT NULL,
  `display_name` VARCHAR(255) NULL DEFAULT NULL,
  `profile_url` VARCHAR(255) NULL DEFAULT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `access_token` VARCHAR(255) NULL DEFAULT NULL,
  `secret` VARCHAR(255) NULL DEFAULT NULL,
  `refresh_token` VARCHAR(255) NULL DEFAULT NULL,
  `expire_time` BIGINT(100) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Connection_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Connection_User_idx` ON `User_Connection` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Connection_Wechat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Connection_Wechat` ;

CREATE TABLE IF NOT EXISTS `User_Connection_Wechat` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `app_id` VARCHAR(255) NULL DEFAULT NULL,
  `open_id` VARCHAR(255) NULL DEFAULT NULL,
  `union_id` VARCHAR(255) NULL DEFAULT NULL,
  `subscribe` TINYINT(1) NULL DEFAULT NULL,
  `session_key` VARCHAR(255) NULL DEFAULT NULL,
  `expires` BIGINT(20) NULL DEFAULT NULL,
  `nickname` VARCHAR(255) NULL DEFAULT NULL,
  `sex` INT(11) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `province` VARCHAR(255) NULL DEFAULT NULL,
  `country` VARCHAR(255) NULL DEFAULT NULL,
  `language` VARCHAR(255) NULL DEFAULT NULL,
  `avatar_Url` VARCHAR(255) NULL DEFAULT NULL,
  `subscribe_time` BIGINT(20) NULL DEFAULT NULL,
  `remark` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Connection_Wechat_Touch_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Connection_Wechat_Touch_User1_idx` ON `User_Connection_Wechat` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Corporate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Corporate` ;

CREATE TABLE IF NOT EXISTS `User_Corporate` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `corporate_id` BIGINT(20) NOT NULL,
  `start_dt` DATETIME NULL DEFAULT NULL,
  `end_dt` DATETIME NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Corporate_Corporate`
    FOREIGN KEY (`corporate_id`)
    REFERENCES `Coporate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_Corporate_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Corporate_Coporate1_idx` ON `User_Corporate` (`corporate_id` ASC);

CREATE INDEX `fk_User_Corporate_User_idx` ON `User_Corporate` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Preference`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Preference` ;

CREATE TABLE IF NOT EXISTS `User_Preference` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `device_id` BIGINT(20) NULL DEFAULT NULL,
  `notification_ind` TINYINT(1) NULL DEFAULT NULL,
  `track_ind` TINYINT(1) NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `created_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Preference_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Preference_User_idx` ON `User_Preference` (`user_id` ASC);


-- begin attached script 'script'
INSERT INTO Authority
(ID, name, description, code) VALUES
(1, 'ROLE_USER', 'ROLE_USER', 'ROLE_USER');
INSERT INTO Authority
(ID, name, description, code) VALUES
(2, 'ROLE_ADMIN','ROLE_ADMIN','ROLE_ADMIN');

-- end attached script 'script'
commit;