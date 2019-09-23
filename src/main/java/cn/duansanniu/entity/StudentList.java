package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户老师获取其学生的数据
 * @author duansanniu
 * @create 2019-09-23 15:47 下午
 */
@Setter
@Getter
public class StudentList {
    private String username;
    private String name;
    private String profession;
    private String className;
    private String title;
    private String detail;
    private Integer status;

    public StudentList(String username, String name, String profession, String className, String title, String detail, Integer status) {
        this.username = username;
        this.name = name;
        this.profession = profession;
        this.className = className;
        this.title = title;
        this.detail = detail;
        this.status = status;
    }

    public StudentList() {
    }
}
