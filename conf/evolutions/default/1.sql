# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table record (
  id                            bigserial not null,
  diagnostic                    varchar(256) not null,
  doctor_user_id                bigint,
  patient_user_id               bigint,
  constraint uq_record_doctor_user_id unique (doctor_user_id),
  constraint pk_record primary key (id)
);

create table usr (
  dtype                         varchar(10) not null,
  id                            bigserial not null,
  token                         varchar(2048),
  username                      varchar(255),
  password                      varchar(255),
  name                          varchar(255),
  surname                       varchar(255),
  major                         varchar(255),
  doctor_id                     bigint,
  birth_year                    integer,
  address                       varchar(255),
  gender                        varchar(255),
  patient_id                    bigint,
  phone_number                  bigint,
  constraint pk_usr primary key (id)
);

alter table record add constraint fk_record_doctor_user_id foreign key (doctor_user_id) references usr (id) on delete restrict on update restrict;

alter table record add constraint fk_record_patient_user_id foreign key (patient_user_id) references usr (id) on delete restrict on update restrict;
create index ix_record_patient_user_id on record (patient_user_id);

alter table usr add constraint fk_usr_doctor_id foreign key (doctor_id) references usr (id) on delete restrict on update restrict;
create index ix_usr_doctor_id on usr (doctor_id);

alter table usr add constraint fk_usr_patient_id foreign key (patient_id) references usr (id) on delete restrict on update restrict;
create index ix_usr_patient_id on usr (patient_id);


# --- !Downs

alter table if exists record drop constraint if exists fk_record_doctor_user_id;

alter table if exists record drop constraint if exists fk_record_patient_user_id;
drop index if exists ix_record_patient_user_id;

alter table if exists usr drop constraint if exists fk_usr_doctor_id;
drop index if exists ix_usr_doctor_id;

alter table if exists usr drop constraint if exists fk_usr_patient_id;
drop index if exists ix_usr_patient_id;

drop table if exists record cascade;

drop table if exists usr cascade;

