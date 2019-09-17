package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-17 19:17 下午
 */
@Setter
@Getter
public class StudentUploadFile {
    private Integer id;
    private Student student;
    private String name;
    private String url;
    private Date createTime;

    public StudentUploadFile(Integer id, Student student, String name, String url, Date createTime) {
        this.id = id;
        this.student = student;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
    }

    public StudentUploadFile() {
    }
}
