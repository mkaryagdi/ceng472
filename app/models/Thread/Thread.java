package models.Thread;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="threads")
public class Thread extends Model {

	@Id
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private String title;

	@NotNull
	private String msg;

	@NotNull
	private Long subCount;

	@NotNull
	private Long voteCount;

	@CreatedTimestamp
	private Date createdDate;

	private String imageURL;

	public static final Finder<Long, Thread> find = new Finder<>(Thread.class);

	public Thread(Long userId, String imageURL, String title, String msg){
		this.userId = userId;
		this.imageURL = imageURL;
		this.title = title;
		this.msg = msg;
		this.voteCount = 0L;
		this.subCount = 0L;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Long voteCount) {
		this.voteCount = voteCount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getSubCount() {
		return subCount;
	}

	public void setSubCount(Long subCount) {
		this.subCount = subCount;
	}

	public void incrementSubCount() {
		this.subCount += 1;
	}

	public void upVote() { this.voteCount += 1;	}

	public void downVote() { this.voteCount -= 1; }


	// public void report();
	// public void editThread;

}
