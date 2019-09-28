package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-26 10:42 上午
 */
@Setter
@Getter
public class DoClassGrade {
    private Integer id;
    private Integer professionid;
    private String name;

    public DoClassGrade(Integer id, Integer professionid, String name) {
        this.id = id;
        this.professionid = professionid;
        this.name = name;
    }

    public DoClassGrade() {
    }
}
