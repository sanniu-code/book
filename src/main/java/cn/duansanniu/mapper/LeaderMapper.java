package cn.duansanniu.mapper;

import cn.duansanniu.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 21:01 下午
 */
@Repository
public interface LeaderMapper {
    public Integer preserveCommonFileInfo(Map map);

    public Integer isPreserve(Map map);

    public Integer updateCommonFileInfo(Map map);

    public List<Teacher> getTeachersByDepartId(Integer id);

    public Integer importSubject(Map map);

    public Integer updateSubject(Map map);

    public Integer deleteSubject(Integer id);

    public Integer getDepartIdBySubjectId(Integer id);
}
