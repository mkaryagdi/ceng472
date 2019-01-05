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

create table nurse_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  major                         varchar(255),
  gender                        varchar(255),
  doctor_id                     bigint,
  constraint pk_nurse_user primary key (id)
);

create table patient_user (
  id                            bigserial not null,
  doctor_user_id                bigint not null,
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

create table record (
  id                            bigserial not null,
  diagnostic                    varchar(256) not null,
  doctor_user_id                bigint,
  patient_user_id               bigint,
  constraint uq_record_doctor_user_id unique (doctor_user_id),
  constraint pk_record primary key (id)
);

create table relative_user (
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  patient_id                    bigint,
  phone_number                  bigint,
  constraint pk_relative_user primary key (id)
);

alter table nurse_user add constraint fk_nurse_user_doctor_id foreign key (doctor_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_nurse_user_doctor_id on nurse_user (doctor_id);

alter table patient_user add constraint fk_patient_user_doctor_user_id foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;
create index ix_patient_user_doctor_user_id on patient_user (doctor_user_id);

alter table record add constraint fk_record_doctor_user_id foreign key (doctor_user_id) references doctor_user (id) on delete restrict on update restrict;

alter table record add constraint fk_record_patient_user_id foreign key (patient_user_id) references patient_user (id) on delete restrict on update restrict;
create index ix_record_patient_user_id on record (patient_user_id);

alter table relative_user add constraint fk_relative_user_patient_id foreign key (patient_id) references patient_user (id) on delete restrict on update restrict;
create index ix_relative_user_patient_id on relative_user (patient_id);


# --- !Downs

alter table if exists nurse_user drop constraint if exists fk_nurse_user_doctor_id;
drop index if exists ix_nurse_user_doctor_id;

alter table if exists patient_user drop constraint if exists fk_patient_user_doctor_user_id;
drop index if exists ix_patient_user_doctor_user_id;

alter table if exists record drop constraint if exists fk_record_doctor_user_id;

alter table if exists record drop constraint if exists fk_record_patient_user_id;
drop index if exists ix_record_patient_user_id;

alter table if exists relative_user drop constraint if exists fk_relative_user_patient_id;
drop index if exists ix_relative_user_patient_id;

drop table if exists admin_user cascade;

drop table if exists doctor_user cascade;

drop table if exists nurse_user cascade;

drop table if exists patient_user cascade;

drop table if exists record cascade;

drop table if exists relative_user cascade;

