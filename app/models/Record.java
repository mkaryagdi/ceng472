package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Record extends Model {

    @Id
    private Long id;

    @NotNull
    @Size(max = 256)
    private String info; // TODO: it must include name, surname, diognastics and birth date of the patient!

    public Long getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }
}
