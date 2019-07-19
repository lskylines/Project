package cn.nowcode.domain;

/**
 * @author
 * @create 2019-07-13 20:52
 */
public class User{
    private int age;
    private String username;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", username='" + username + '\'' +
                '}';
    }
}
