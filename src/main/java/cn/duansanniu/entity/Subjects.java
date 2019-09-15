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
    private Teacher teacher;
    private Student student;

    public Subjects(Integer id, String title, String detail, Teacher teacher, Student student) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.teacher = teacher;
        this.student = student;
    }

    public Subjects() {
    }
}
