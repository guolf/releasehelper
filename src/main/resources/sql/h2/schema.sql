drop table if exists release_task;
drop table if exists release_user;
drop table if exists release_app;
drop table if exists release_version;

CREATE TABLE IF NOT EXISTS release_task (
  id          BIGINT generated BY DEFAULT AS IDENTITY,
  title       VARCHAR(128) NOT NULL,
  description VARCHAR(255),
  user_id     BIGINT       NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS release_user (
  id            BIGINT generated BY DEFAULT AS IDENTITY,
  login_name    VARCHAR(64)  NOT NULL UNIQUE,
  name          VARCHAR(64)  NOT NULL,
  password      VARCHAR(255) NOT NULL,
  salt          VARCHAR(64)  NOT NULL,
  roles         VARCHAR(255) NOT NULL,
  register_date TIMESTAMP    NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS release_app (
  id           BIGINT generated BY DEFAULT AS IDENTITY,
  uuid         VARCHAR(64)  NOT NULL UNIQUE,
  name         VARCHAR(64)  NOT NULL,
  intro        VARCHAR(255),
  package_name VARCHAR(64)  NOT NULL,
  versionName VARCHAR(255) NOT NULL,
  icon         VARCHAR(265),
  versionCode BIGINT,
  status       INT,
  create_date  TIMESTAMP    NOT NULL,
  update_date  TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS release_version (
  id           BIGINT generated BY DEFAULT AS IDENTITY,
  app_id       BIGINT       NOT NULL,
  versionName VARCHAR(64) ,
  intro        VARCHAR(255) ,
  icon         VARCHAR(265),
  downUrl      VARCHAR(265),
  versionCode BIGINT,
  status       INT,
  create_date  TIMESTAMP,
  update_date  TIMESTAMP,
  PRIMARY KEY (id)
);


insert into release_user (id, login_name, name, password, salt, roles, register_date) values(1,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','admin','2012-06-04 01:00:00');
insert into release_user (id, login_name, name, password, salt, roles, register_date) values(2,'user','Calvin','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','user','2012-06-04 02:00:00');