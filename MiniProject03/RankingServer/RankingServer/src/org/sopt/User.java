package org.sopt;

/**
 * Created by jiyoungpark on 15. 9. 14..
 */
public class User {
    int id;
    String name;
    String sex;
    int score;

    public User(int id, String name, String sex, int score) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
