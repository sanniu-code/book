package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.Task;
import cn.duansanniu.entity.Teacher;
import cn.duansanniu.mapper.LeaderMapper;
import cn.duansanniu.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 20:58 下午
 */
@Service
public class LeaderServiceImpl implements LeaderService {

    @Autowired
    private LeaderMapper leaderMapper;

    @Override
    public Integer preserveCommonFileInfo(Map map) {

        //判断该文件是否已经存在
        Integer num = leaderMapper.isPreserve(map);
        if(num >= 1){
            //存在
            //修改该数据
            Integer flag = leaderMapper.updateCommonFileInfo(map);
            return flag;
        }else {
            return leaderMapper.preserveCommonFileInfo(map);
        }



    }

    @Override
    public List<Teacher> getTeachersByDepartId(Integer id) {

        return leaderMapper.getTeachersByDepartId(id);
    }

    @Override
    public Integer importSubject(Map map) {
        return leaderMapper.importSubject(map);
    }

    @Override
    public Integer updateSubject(Map map) {
        return leaderMapper.updateSubject(map);
    }

    @Override
    public Integer deleteSubject(Integer id) {
        return leaderMapper.deleteSubject(id);
    }

    @Override
    public Integer getDepartIdBySubjectId(Integer id) {
        return leaderMapper.getDepartIdBySubjectId(id);
    }

    @Override
    public Integer createTask(Map map) {
        //判断 这个系  当前是否存在 有效的任务
        Date time = new Date();
        map.put("time",time);
        Integer num = leaderMapper.isEffectiveTask(map);
        if(num <= 0)
            return leaderMapper.createTask(map);
        return 0;
    }
}
