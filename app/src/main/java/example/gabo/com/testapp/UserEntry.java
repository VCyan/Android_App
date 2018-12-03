package example.gabo.com.testapp;

public class UserEntry {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_PASS = "pwd";
    public static final String COLUMN_PATTERN = "pattern";

    private String user;
    private String password;
    private String pattern;
    private int id;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
