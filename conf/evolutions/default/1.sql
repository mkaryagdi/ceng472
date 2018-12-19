# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comments (
  id                            bigserial not null,
  user_id                       bigint not null,
  thread_id                     bigint not null,
  msg                           varchar(255) not null,
  vote_count                    bigint not null,
  created_date                  timestamptz not null,
  constraint pk_comments primary key (id)
);

create table threads (
  id                            bigserial not null,
  user_id                       bigint not null,
  title                         varchar(255) not null,
  msg                           varchar(255) not null,
  sub_count                     bigint not null,
  vote_count                    bigint not null,
  image_url                     varchar(255),
  created_date                  timestamptz not null,
  constraint pk_threads primary key (id)
);

create table usr (
  id                            bigserial not null,
  token                         varchar(255),
  name                          varchar(255) not null,
  email                         varchar(255) not null,
  password                      varchar(255) not null,
  karma                         bigint not null,
  joined_date                   timestamptz not null,
  constraint pk_usr primary key (id)
);


# --- !Downs

drop table if exists comments cascade;

drop table if exists threads cascade;

drop table if exists usr cascade;

