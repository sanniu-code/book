package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 9:55 上午
 */
@Setter
@Getter
public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Integer type;
    private String code;

    public User(String username, String password, Integer type, String code) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.code = code;
    }

    public User() {
    }
}
