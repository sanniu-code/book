package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-23 10:44 上午
 */
@Setter
@Getter
public class TeacherFile {
    private Integer id;
    private Teacher teacher;
    private String name;
    private String url;
    private Date createTime;
    private Student student;
    private Integer fileType;

    public TeacherFile() {
    }

    public TeacherFile(Integer id, Teacher teacher, String name, String url, Date createTime, Student student, Integer fileType) {
        this.id = id;
        this.teacher = teacher;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
        this.student = student;
        this.fileType = fileType;
    }
}
