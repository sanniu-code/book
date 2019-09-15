package cn.duansanniu.mapper;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Subjects;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:26 下午
 */
@Repository
public interface StudentMapper {
    public List<Subjects> getSubjects();

    public Subjects getSubject(Student student);

    public Integer selectSubject(Map map);

    public Integer isSelect(Map map);

}
