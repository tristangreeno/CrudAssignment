import java.util.HashMap;


class User {
    String name;
    String password;

    private HashMap<String, String> passwords = new HashMap<>();

    User(String name, String password){
        this.name = name;
        this.password = password;

        passwords.put(name, password);
    }
}
