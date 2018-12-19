package models;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("patient")
public class PatientUser extends User {
}
