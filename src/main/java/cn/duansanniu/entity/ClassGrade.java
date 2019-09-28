package cn.duansanniu.entity;

import cn.duansanniu.entity.Profession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-16 9:37 上午
 */
@Setter
@Getter
@ApiModel(value = "班级对象")
public class ClassGrade {
    @ApiModelProperty(name = "id",value = "班级的id")
    private Integer id;
    @ApiModelProperty(name = "name",value = "班级的名字")
    private String name;
    @ApiModelProperty(name = "profession",value = "专业对象")
    private Profession profession;

    public ClassGrade() {
    }

    public ClassGrade(Integer id, String name, Profession profession) {
        this.id = id;
        this.name = name;
        this.profession = profession;
    }
}
