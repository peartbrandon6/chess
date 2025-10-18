package model;

public record UserData(String username, String password, String email) {
    public UserData(String username, String password) {
        this(username,password,null);
    }
}
