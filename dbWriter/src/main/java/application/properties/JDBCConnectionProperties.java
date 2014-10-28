package application.properties;

/**
 * @author: Andrey Kozlov
 */
public class JDBCConnectionProperties {

    private String userName;
    private String userPassword;
    private String url;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JDBCConnectionProperties{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
