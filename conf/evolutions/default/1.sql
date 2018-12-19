# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table doctor (
  id                            bigserial not null,
  name                          varchar(255),
  constraint pk_doctor primary key (id)
);


# --- !Downs

drop table if exists doctor cascade;

