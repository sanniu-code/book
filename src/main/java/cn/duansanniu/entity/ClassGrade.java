package cn.duansanniu.entity;

import cn.duansanniu.entity.Profession;
import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 9:37 上午
 */
@Setter
@Getter
public class ClassGrade {
    private Integer id;
    private String name;
    private Profession profession;

    public ClassGrade() {
    }

    public ClassGrade(Integer id, String name, Profession profession) {
        this.id = id;
        this.name = name;
        this.profession = profession;
    }
}
