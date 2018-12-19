package models.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jwt.JwtHelper;

import java.io.UnsupportedEncodingException;

@Singleton
public class UserGeneratorImpl implements UserGenerator {

	private JwtHelper jwtHelper;

	@Inject
	public UserGeneratorImpl(JwtHelper jwtHelper) {
		this.jwtHelper = jwtHelper;
	}

	@Override
	public User generate(String name,
						 String email,
						 String password) throws UnsupportedEncodingException {

		User user = new User(null, name, email, password);

		user.save();

		user.setToken(jwtHelper.getSignedToken(user.getId(), false));
		user.save();

		return user;
	}
}
