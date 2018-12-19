package models;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Id;


@DiscriminatorValue("doctor")
public class DoctorUser extends User {

    @Id
    private Long id;

    private String name;
}
