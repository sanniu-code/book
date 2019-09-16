package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 9:21 上午
 */
@Setter
@Getter
public class Depart {
    private Integer id;
    private String name;


    public Depart(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Depart() {
    }
}
