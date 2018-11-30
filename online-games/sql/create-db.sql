/*
CREATE SCHEMA `online_games_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci ;

CREATE DATABASE online_games_db;
USE online_games_db;


DROP TABLE `online_games_db`.`user`;
DROP TABLE `online_games_db`.`game_type`;
DROP TABLE `online_games_db`.`match_waiting`;
DROP TABLE `online_games_db`.`match_active`;
DROP TABLE `online_games_db`.`match_players`;
DROP TABLE `online_games_db`.`match_done`;

DROP DATABASE online_games_db;
DROP SCHEMA `online_games_db`;
*/

DROP DATABASE online_games_db;

CREATE DATABASE online_games_db;
USE online_games_db;

CREATE TABLE `online_games_db`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(400) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `token` VARCHAR(400) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB;


CREATE TABLE `online_games_db`.`game_type` (
  `id` INT NOT NULL,
  `name` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);
 
 
CREATE TABLE `online_games_db`.`game_type_option` (
  `id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) NULL,
  `game_type_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `game_type_options_game_type_fk_idx` (`game_type_fk` ASC) VISIBLE,
  CONSTRAINT `game_type_options_game_type_fk`
    FOREIGN KEY (`game_type_fk`)
    REFERENCES `online_games_db`.`game_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `online_games_db`.`match_waiting` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `player_fk` INT NOT NULL,
  `game_type_fk` INT NOT NULL,
  `options` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `player_fk_UNIQUE` (`player_fk` ASC) VISIBLE,
  CONSTRAINT `match_waiting_player_fk`
    FOREIGN KEY (`player_fk`)
    REFERENCES `online_games_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `match_waiting_game_type_fk`
    FOREIGN KEY (`game_type_fk`)
    REFERENCES `online_games_db`.`game_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `online_games_db`.`match_active` (
  `id` INT NOT NULL,
  `game_type_fk` INT NOT NULL,
  `board_state` VARCHAR(40000) NOT NULL,
  `turn` INT NOT NULL,
  `options` VARCHAR(100) NULL,
  `action` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `match_active_game_type_fk`
    FOREIGN KEY (`game_type_fk`)
    REFERENCES `online_games_db`.`game_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE `online_games_db`.`match_players` (
  `player1_fk` INT NOT NULL,
  `player2_fk` INT NOT NULL,
  `active_player` INT NOT NULL,
  `match_fk` INT NOT NULL,
  UNIQUE INDEX `player1_fk_UNIQUE` (`player1_fk` ASC) VISIBLE,
  UNIQUE INDEX `player2_fk_UNIQUE` (`player2_fk` ASC) VISIBLE,
  UNIQUE INDEX `match_fk_UNIQUE` (`match_fk` ASC) VISIBLE,
  INDEX `match_players_active_player_fk_idx` (`active_player_fk` ASC) VISIBLE,
  CONSTRAINT `match_players_player1_fk`
    FOREIGN KEY (`player1_fk`)
    REFERENCES `online_games_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `match_players_player2_fk`
    FOREIGN KEY (`player2_fk`)
    REFERENCES `online_games_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;


CREATE TABLE `online_games_db`.`match_done` (
  `id` INT NOT NULL,
  `date` DATE NOT NULL,
  `game_type_fk` INT NOT NULL,
  `match_count_total` INT NOT NULL,
  `match_won_total` INT NOT NULL DEFAULT 0,
  `player_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `match_done_player_fk_idx` (`player_fk` ASC) VISIBLE,
  INDEX `match_done_game_type_fk_idx` (`game_type_fk` ASC) VISIBLE,
  CONSTRAINT `match_done_player_fk`
    FOREIGN KEY (`player_fk`)
    REFERENCES `online_games_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `match_done_game_type_fk`
    FOREIGN KEY (`game_type_fk`)
    REFERENCES `online_games_db`.`game_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
