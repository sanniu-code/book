package cn.duansanniu.service;

import cn.duansanniu.entity.Task;
import cn.duansanniu.entity.Teacher;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 20:58 下午
 */
public interface LeaderService {

    public Integer preserveCommonFileInfo(Map map);

    public List<Teacher> getTeachersByDepartId(Integer id);

    public Integer importSubject(Map map);

    public Integer updateSubject(Map map);

    public Integer deleteSubject(Integer id);

    public Integer getDepartIdBySubjectId(Integer id);

    public Integer createTask(Map map);
}
