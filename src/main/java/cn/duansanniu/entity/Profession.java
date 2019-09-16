package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 9:27 上午
 */
@Setter
@Getter
public class Profession {
    private Integer id;
    private String name;
    private Depart depart;

    public Profession(Integer id, String name, Depart depart) {
        this.id = id;
        this.name = name;
        this.depart = depart;
    }

    public Profession() {
    }
}
