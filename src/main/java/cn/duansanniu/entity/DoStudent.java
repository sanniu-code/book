package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * @author duansanniu
 * @create 2019-09-25 17:39 下午
 */
@Setter
@Getter
public class DoStudent {
    private String username;
    private String password;
    private String name;
    private Integer sex;
    private Integer classid;
    private Date createTime;
    private Integer status;
    private Integer year;


    public DoStudent(String username, String password, String name, Integer sex, Integer classid, Date createTime, Integer status, Integer year) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.classid = classid;
        this.createTime = createTime;
        this.status = status;
        this.year = year;
    }

    public DoStudent() {
    }
}
