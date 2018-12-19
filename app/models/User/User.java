package models.User;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="usr")
public class User extends Model {

	@Id
	private Long id;

	private String token;

	@NotNull
	private String name;

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	private Long karma;

	@CreatedTimestamp
	private Date joinedDate;

	//  title? (student, academician)

	//	profilePicture?

	public static final Finder<Long, User> find = new Finder<>(User.class);


	public User(String token, String name, String email, String password){
		this.token = token;
		this.name = name;
		this.email = email;
		this.password = password;
		this.karma = 0L;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getKarma() {
		return karma;
	}

	public void setKarma(Long karma) {
		this.karma = karma;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
}
