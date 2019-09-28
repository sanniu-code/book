package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-25 16:00 下午
 */
@Setter
@Getter
public class DoTeacher {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer sex;
    private String professionRank;
    private String degree;
    private Integer status;
    private Integer departId;

    public DoTeacher(Integer id, String username, String password, String name, Integer sex, String professionRank, String degree, Integer status, Integer departId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.professionRank = professionRank;
        this.degree = degree;
        this.status = status;
        this.departId = departId;
    }

    public DoTeacher() {
    }
}
