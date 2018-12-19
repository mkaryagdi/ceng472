package models;

import io.ebean.Model;

import javax.persistence.Entity;

@Entity
public class Record extends Model {

    private Long id;

    private String Diagnose;
}
