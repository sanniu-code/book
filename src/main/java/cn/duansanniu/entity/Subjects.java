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
    private TeacherFile teacherFile;
    private String origin;
    private String address;
    private Integer status;
    private Teacher teacher;
    private Student student;

    public Subjects(Integer id, String title, String detail, String type, TeacherFile teacherFile, String origin, String address, Integer status, Teacher teacher, Student student) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.teacherFile = teacherFile;
        this.origin = origin;
        this.address = address;
        this.status = status;
        this.teacher = teacher;
        this.student = student;
    }

    public Subjects() {
    }
}
