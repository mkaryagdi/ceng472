package models.User;

import java.io.UnsupportedEncodingException;

public interface UserGenerator {
	User generate(String name,
				  String email,
				  String password) throws UnsupportedEncodingException;
}
