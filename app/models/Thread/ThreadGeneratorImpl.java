package models.Thread;

import com.google.inject.Singleton;

@Singleton
public class ThreadGeneratorImpl implements ThreadGenerator{

	@Override
	public Thread generate(Long userId,
						   String imageURL,
						   String title,
						   String msg)  {

		Thread thread = new Thread(userId, imageURL, title, msg);
		thread.save();

		return thread;
	}
}
