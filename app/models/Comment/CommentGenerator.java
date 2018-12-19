package models.Comment;

import java.io.UnsupportedEncodingException;

public interface CommentGenerator {
	Comment generate(Long userId,
					 Long threadId,
					 String msg) throws UnsupportedEncodingException;
}
