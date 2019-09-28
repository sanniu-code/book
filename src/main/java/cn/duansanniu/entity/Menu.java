package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author duansanniu
 * @create 2019-09-27 17:56 下午
 */
@Setter
@Getter
public class Menu {
    private Integer id;
    private String name;
    private Integer parentid;
    private List<Menu> menus;
    private String path;

    public Menu(Integer id, String name, Integer parentid, List<Menu> menus, String path) {
        this.id = id;
        this.name = name;
        this.parentid = parentid;
        this.menus = menus;
        this.path = path;
    }

    public Menu() {
    }
}
