package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-15 14:35 下午
 */
@Setter
@Getter
public class Subjects {
    private Integer id;
    private String title;
    private String detail;
    private String type;
    private Teacher teacher;
    private Student student;

    public Subjects(Integer id, String title, String detail, String type, Teacher teacher, Student student) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.teacher = teacher;
        this.student = student;
    }

    public Subjects() {
    }
}
