package cn.duansanniu.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-22 19:29 下午
 */
@Getter
@Setter
public class Task {
    private Integer id;
    private String name;
    private Depart depart;
    private String year;
    private Date startTime;
    private Date endTime;

    public Task(Integer id, String name, Depart depart, String year, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.depart = depart;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task() {
    }
}
