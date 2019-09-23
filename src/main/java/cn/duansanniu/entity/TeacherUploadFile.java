package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-18 9:28 上午
 */
@Setter
@Getter
public class TeacherUploadFile {
    private Integer id;
    private String username;
    private String name;
    private String url;
    private Integer type;
    private Teacher teacher;
    private Date createTime;

    public TeacherUploadFile(Integer id, String username, String name, String url, Integer type, Teacher teacher, Date createTime) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.url = url;
        this.type = type;
        this.teacher = teacher;
        this.createTime = createTime;
    }

    public TeacherUploadFile() {
    }
}
