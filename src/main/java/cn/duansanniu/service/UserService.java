package cn.duansanniu.service;

import cn.duansanniu.entity.CommonFile;
import cn.duansanniu.entity.Task;
import cn.duansanniu.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 9:51 上午
 */
public interface UserService {
    public Map login(User user);

    public String getFilePath(String fileName);

    public List<CommonFile> getUserFileList();

    public Integer updatePass(Map map);

    public Integer judgePass(Map map);

    public Task isEffectiveTask(Map map);

    public Integer getStudentDepartIdByUsername(String username);

    public Integer getTeacherDepartIdByUsername(String username);


}
