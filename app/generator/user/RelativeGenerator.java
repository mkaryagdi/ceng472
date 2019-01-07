package generator.user;

import models.Record;
import models.RelativeUser;

public interface RelativeGenerator {

    RelativeUser generate(String username, String password, String name, String surname,
                          Double phoneNumber, Record record) throws Exception;
}
