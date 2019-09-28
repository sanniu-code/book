package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-26 16:02 下午
 */
@Setter
@Getter
public class DoTeacherFile {
    private Integer id;
    private String username;
    private String name;
    private String url;
    private Date createTime;
    private String studentusername;
    private Integer fileType;

    public DoTeacherFile(Integer id, String username, String name, String url, Date createTime, String studentusername, Integer fileType) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
        this.studentusername = studentusername;
        this.fileType = fileType;
    }

    public DoTeacherFile() {
    }
}
