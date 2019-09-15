package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-15 14:20 下午
 */

@Getter
@Setter
public class Student {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer sex;

    public Student(Integer id, String username, String password, String name, Integer sex) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
    }

    public Student() {
    }
}
