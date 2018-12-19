package models.Thread;


public interface ThreadGenerator {
	Thread generate(Long userId,
					String imageURL,
					String title,
					String msg);
}
