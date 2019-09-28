package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-26 10:17 上午
 */
@Setter
@Getter
public class DoProfession {
    private Integer id;
    private String name;
    private Integer departId;

    public DoProfession(Integer id, String name, Integer departId) {
        this.id = id;
        this.name = name;
        this.departId = departId;
    }

    public DoProfession() {
    }
}
