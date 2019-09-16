package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 10:18 上午
 */
@Setter
@Getter
public class Leader {
    private Integer id;
    private String username;
    private String password;
    private String name;

    public Leader(Integer id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public Leader() {
    }
}
