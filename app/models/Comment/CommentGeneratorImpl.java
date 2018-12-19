package models.Comment;

import com.google.inject.Singleton;

import java.io.UnsupportedEncodingException;

@Singleton
public class CommentGeneratorImpl implements CommentGenerator{

	@Override
	public Comment generate(Long userId,
							Long threadId,
							String msg) throws UnsupportedEncodingException {

		Comment comment = new Comment(userId, threadId, msg);

		comment.save();

		return comment;
	}
}
