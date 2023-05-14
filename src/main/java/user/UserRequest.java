package user;

import java.util.Random;

public class UserRequest {
    private String email;
    private String password;
    private String name;

    public UserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserRequest generate() {
        return new UserRequest(getRandomString() + "@ya.ru", getRandomString(), getRandomString());
    }

    private static String getRandomString() {
        String ALPHABET = new String("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPRQRSTUVWXYZ".toCharArray());
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int rndCharAt = random.nextInt(ALPHABET.length());
            char rndChar = ALPHABET.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
