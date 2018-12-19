package models.Comment;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="comments")
public class Comment extends Model {

	@Id
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long threadId;

	@NotNull
	private String msg;

	@NotNull
	private Long voteCount;

	@CreatedTimestamp
	private Date createdDate;

	//	private Image?

	public static final Finder<Long, Comment> find = new Finder<>(Comment.class);

	public Comment(Long userId, Long threadId, String msg){
		this.userId = userId;
		this.threadId = threadId;
		this.msg = msg;
		this.voteCount = 0L;
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

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public void upVote() { this.voteCount += 1;	}

	public void downVote() { this.voteCount -= 1; }

	// public void report();
	// public void editComment;
}
