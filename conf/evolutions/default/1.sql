# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table record (
  id                            bigserial not null,
  info                          varchar(256) not null,
  constraint pk_record primary key (id)
);

create table usr (
  type                          varchar(31) not null,
  id                            bigserial not null,
  token                         varchar(255),
  name                          varchar(255) not null,
  surname                       varchar(255) not null,
  constraint pk_usr primary key (id)
);


# --- !Downs

drop table if exists record cascade;

drop table if exists usr cascade;

