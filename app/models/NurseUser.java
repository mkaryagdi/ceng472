package models;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("nurse")
public class NurseUser extends User{
}
