package models;

public class User {
    private String email;
    private String password;
    private String name;
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public User withEmail(String email){this.email = email; return this;}
    public User withPassword(String password){this.password = password; return this;}
    public User withName(String name){this.name = name; return this;}
}
