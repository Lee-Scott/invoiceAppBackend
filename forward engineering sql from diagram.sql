-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema securecapita
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema securecapita
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `securecapita` DEFAULT CHARACTER SET utf8 ;
USE `securecapita` ;

-- -----------------------------------------------------
-- Table `securecapita`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `securecapita`.`Users` (
  `id` BIGINT(50) NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `last__name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `securecapita`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `securecapita`.`Roles` (
  `id` BIGINT(50) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `permissions` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `securecapita`.`UserRoles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `securecapita`.`UserRoles` (
  `id` BIGINT(50) NOT NULL AUTO_INCREMENT,
  `users_id` BIGINT(50) NOT NULL,
  `role_id` BIGINT(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_UserRoles_Roles1_idx` (`role_id` ASC) VISIBLE,
  INDEX `fk_UserRoles_Users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_UserRoles_Roles1`
    FOREIGN KEY (`role_id`)
    REFERENCES `securecapita`.`Roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_UserRoles_Users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `securecapita`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
