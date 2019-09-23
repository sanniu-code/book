package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-23 10:53 上午
 */
@Setter
@Getter
public class ApplyTable {
    private String username;
    private Integer status;
    private String title;
    private String detail;
    private String type;
    private String origin;
    private String address;
    private Integer fileId;
    private MultipartFile multipartFile;

    public ApplyTable(String username, Integer status, String title, String detail, String type, String origin, String address, Integer fileId, MultipartFile multipartFile) {
        this.username = username;
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.origin = origin;
        this.address = address;
        this.fileId = fileId;
        this.multipartFile = multipartFile;
    }

    public ApplyTable() {
    }
}
