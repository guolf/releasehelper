# drop table if exists release_task;
# drop table if exists release_user;
# drop table if exists release_app;
# drop table if exists release_version;

create table IF NOT EXISTS release_task (
	id bigint auto_increment,
	title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table IF NOT EXISTS release_user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	roles varchar(255) not null,
	register_date timestamp not null default 0,
	primary key (id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS release_app (
  id bigint auto_increment,
  uuid         VARCHAR(64)  NOT NULL UNIQUE,
  name         VARCHAR(64)  NOT NULL,
  intro        VARCHAR(255),
  package_name VARCHAR(64)  NOT NULL,
  version_name VARCHAR(255) NOT NULL,
  icon         VARCHAR(265),
  version_code BIGINT,
  status       INT,
  create_date  TIMESTAMP    NOT NULL,
  update_date  TIMESTAMP,
  PRIMARY KEY (id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS release_version (
  id bigint auto_increment,
  app_id       BIGINT       NOT NULL,
  version_name VARCHAR(64) ,
  intro        VARCHAR(255) ,
  icon         VARCHAR(265),
  down_url      VARCHAR(265),
  file_size     VARCHAR(64),
  md5 VARCHAR(64),
  sha1 VARCHAR(64),
  version_code BIGINT,
  status       INT,
  create_date  TIMESTAMP,
  update_date  TIMESTAMP,
  flag BOOLEAN,
  PRIMARY KEY (id)
) engine=InnoDB;



# insert into release_user (id, login_name, name, password, salt, roles, register_date) values(1,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','admin','2012-06-04 01:00:00');
# insert into release_user (id, login_name, name, password, salt, roles, register_date) values(2,'user','Calvin','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','user','2012-06-04 02:00:00');