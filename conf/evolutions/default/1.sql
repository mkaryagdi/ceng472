# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table admin_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  constraint pk_admin_user primary key (id)
);

create table doctor_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  major                         varchar(255),
  birth_year                    integer,
  gender                        varchar(255),
  constraint pk_doctor_user primary key (id)
);

create table doctor_user_patient_user (
  doctor_user_id                bigint not null,
  patient_user_id               bigint not null,
  constraint pk_doctor_user_patient_user primary key (doctor_user_id,patient_user_id)
);

create table nurse_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  major                         varchar(255),
  gender                        varchar(255),
  constraint pk_nurse_user primary key (id)
);

create table nurse_user_doctor_user (
  nurse_user_id                 bigint not null,
  doctor_user_id                bigint not null,
  constraint pk_nurse_user_doctor_user primary key (nurse_user_id,doctor_user_id)
);

create table patient_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  birth_year                    integer,
  address                       varchar(255),
  gender                        varchar(255),
  constraint pk_patient_user primary key (id)
);

create table patient_user_doctor_user (
  patient_user_id               bigint not null,
  doctor_user_id                bigint not null,
  constraint pk_patient_user_doctor_user primary key (patient_user_id,doctor_user_id)
);

create table patient_user_relative_user (
  patient_user_id               bigint not null,
  relative_user_id              bigint not null,
  constraint pk_patient_user_relative_user primary key (patient_user_id,relative_user_id)
);

create table record (
  id                            bigserial not null,
  diagnostic                    varchar(256) not null,
  doctor_user_id                bigint,
  patient_user_id               bigint,
  constraint pk_record primary key (id)
);

create table relative_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  phone_number                  float,
  constraint pk_relative_user primary key (id)
);

create table relative_user_record (
  relative_user_id              bigint not null,
  record_id                     bigint not null,
  constraint pk_relative_user_record primary key (relative_user_id,record_id)
);

alter table doctor_user_patient_user add constraint fk_doctor_user_patient_user_doctor_user foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_doctor_user_patient_user_doctor_user on doctor_user_patient_user (doctor_user_id);

alter table doctor_user_patient_user add constraint fk_doctor_user_patient_user_patient_user foreign key (patient_user_id) references patient_user (id) on delete restrict on update restrict;
create index ix_doctor_user_patient_user_patient_user on doctor_user_patient_user (patient_user_id);

alter table nurse_user_doctor_user add constraint fk_nurse_user_doctor_user_nurse_user foreign key (nurse_user_id) references nurse_user (id) on delete restrict on update restrict;
create index ix_nurse_user_doctor_user_nurse_user on nurse_user_doctor_user (nurse_user_id);

alter table nurse_user_doctor_user add constraint fk_nurse_user_doctor_user_doctor_user foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_nurse_user_doctor_user_doctor_user on nurse_user_doctor_user (doctor_user_id);

alter table patient_user_doctor_user add constraint fk_patient_user_doctor_user_patient_user foreign key (patient_user_id) references patient_user (id) on delete restrict on update restrict;
create index ix_patient_user_doctor_user_patient_user on patient_user_doctor_user (patient_user_id);

alter table patient_user_doctor_user add constraint fk_patient_user_doctor_user_doctor_user foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_patient_user_doctor_user_doctor_user on patient_user_doctor_user (doctor_user_id);

alter table patient_user_relative_user add constraint fk_patient_user_relative_user_patient_user foreign key (patient_user_id) references patient_user (id) on delete restrict on update restrict;
create index ix_patient_user_relative_user_patient_user on patient_user_relative_user (patient_user_id);

alter table patient_user_relative_user add constraint fk_patient_user_relative_user_relative_user foreign key (relative_user_id) references relative_user (id) on delete restrict on update restrict;
create index ix_patient_user_relative_user_relative_user on patient_user_relative_user (relative_user_id);

alter table record add constraint fk_record_doctor_user_id foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_record_doctor_user_id on record (doctor_user_id);

alter table record add constraint fk_record_patient_user_id foreign key (patient_user_id) references patient_user (id) on delete restrict on update restrict;
create index ix_record_patient_user_id on record (patient_user_id);

alter table relative_user_record add constraint fk_relative_user_record_relative_user foreign key (relative_user_id) references relative_user (id) on delete restrict on update restrict;
create index ix_relative_user_record_relative_user on relative_user_record (relative_user_id);

alter table relative_user_record add constraint fk_relative_user_record_record foreign key (record_id) references record (id) on delete restrict on update restrict;
create index ix_relative_user_record_record on relative_user_record (record_id);


# --- !Downs

alter table if exists doctor_user_patient_user drop constraint if exists fk_doctor_user_patient_user_doctor_user;
drop index if exists ix_doctor_user_patient_user_doctor_user;

alter table if exists doctor_user_patient_user drop constraint if exists fk_doctor_user_patient_user_patient_user;
drop index if exists ix_doctor_user_patient_user_patient_user;

alter table if exists nurse_user_doctor_user drop constraint if exists fk_nurse_user_doctor_user_nurse_user;
drop index if exists ix_nurse_user_doctor_user_nurse_user;

alter table if exists nurse_user_doctor_user drop constraint if exists fk_nurse_user_doctor_user_doctor_user;
drop index if exists ix_nurse_user_doctor_user_doctor_user;

alter table if exists patient_user_doctor_user drop constraint if exists fk_patient_user_doctor_user_patient_user;
drop index if exists ix_patient_user_doctor_user_patient_user;

alter table if exists patient_user_doctor_user drop constraint if exists fk_patient_user_doctor_user_doctor_user;
drop index if exists ix_patient_user_doctor_user_doctor_user;

alter table if exists patient_user_relative_user drop constraint if exists fk_patient_user_relative_user_patient_user;
drop index if exists ix_patient_user_relative_user_patient_user;

alter table if exists patient_user_relative_user drop constraint if exists fk_patient_user_relative_user_relative_user;
drop index if exists ix_patient_user_relative_user_relative_user;

alter table if exists record drop constraint if exists fk_record_doctor_user_id;
drop index if exists ix_record_doctor_user_id;

alter table if exists record drop constraint if exists fk_record_patient_user_id;
drop index if exists ix_record_patient_user_id;

alter table if exists relative_user_record drop constraint if exists fk_relative_user_record_relative_user;
drop index if exists ix_relative_user_record_relative_user;

alter table if exists relative_user_record drop constraint if exists fk_relative_user_record_record;
drop index if exists ix_relative_user_record_record;

drop table if exists admin_user cascade;

drop table if exists doctor_user cascade;

drop table if exists doctor_user_patient_user cascade;

drop table if exists nurse_user cascade;

drop table if exists nurse_user_doctor_user cascade;

drop table if exists patient_user cascade;

drop table if exists patient_user_doctor_user cascade;

drop table if exists patient_user_relative_user cascade;

drop table if exists record cascade;

drop table if exists relative_user cascade;

drop table if exists relative_user_record cascade;

