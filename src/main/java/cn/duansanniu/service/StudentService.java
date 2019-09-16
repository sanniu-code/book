package cn.duansanniu.service;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Subjects;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:25 下午
 */

public interface StudentService {
    public List<Subjects> getSubjects();

    public Subjects getSubject(Student student);

    public Integer selectSubject(Map map);

    public String getFilePath(String fileName);

}
