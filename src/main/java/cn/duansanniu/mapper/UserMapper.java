package cn.duansanniu.mapper;

import cn.duansanniu.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 9:58 上午
 */
@Repository
public interface UserMapper {

    public Map studentLogin(User user);

    public Map teacherLogin(User user);

    public Map leaderLogin(User user);

    public String getFilePath(Integer id);

    public List<CommonFile> getUserFileList();

    public Integer updateTeacherPass(Map map);

    public Integer updateStudentPass(Map map);

    public Integer updateLeaderPass(Map map);

    public Integer judgeStudentPass(Map map);

    public Integer judgeTeacherPass(Map map);

    public Integer judgeLeaderPass(Map map);
//    判断当前是否存在 任务
    public Task isEffectiveTask(Map map);
    //通过用户名 获取这个用户对应的系id
    public Integer getStudentDepartIdByUsername(String username);

    public Integer getTeacherDepartIdByUsername(String username);

    public List<Menu> getMenu(Integer userType);

    public List<Menu> getParentMenu(Integer userType);

    public List<Menu> getSonMenu(Integer userType);


}
