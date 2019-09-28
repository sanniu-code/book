package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-26 16:37 下午
 */
@Setter
@Getter
public class DoSubject {
    private Integer id;
    private String username;
    private String title;
    private String detail;
    private Integer status;
    private String type;
    private Integer fileId;
    private String origin;
    private String address;
    private Integer taskId;

    public DoSubject(Integer id, String username, String title, String detail, Integer status, String type, Integer fileId, String origin, String address, Integer taskId) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.type = type;
        this.fileId = fileId;
        this.origin = origin;
        this.address = address;
        this.taskId = taskId;
    }

    public DoSubject() {
    }
}
