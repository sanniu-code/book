package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-22 8:30 上午
 */
@Getter
@Setter
public class CommonFile {
    private Integer id;
    private String name;
    private String url;
    private Date createTime;

    public CommonFile(Integer id, String name, String url, Date createTime) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
    }

    public CommonFile() {
    }
}
