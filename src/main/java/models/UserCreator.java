package models;

import static utils.Utils.randomString;
public class UserCreator {
    public static User randomUserAllData(){
        return new User()
                .withEmail(randomString(10)+"@gmail.com")
                .withPassword(randomString(12))
                .withName(randomString(20));
    }
}
