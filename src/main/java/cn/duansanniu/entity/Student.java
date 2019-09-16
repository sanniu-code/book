package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private Integer status;
    private Date createTime;
    private ClassGrade classGrade;


    public Student(Integer id, String username, String password, String name, Integer sex, Integer status, Date createTime, ClassGrade classGrade) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.status = status;
        this.createTime = createTime;
        this.classGrade = classGrade;
    }

    public Student() {
    }
}
