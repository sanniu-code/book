package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.*;
import cn.duansanniu.mapper.UserMapper;
import cn.duansanniu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 9:51 上午
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map login(User user) {
        switch (user.getType()){
            case 1:
                Map studentMap = userMapper.studentLogin(user);
                studentMap.put("type",1);
                return studentMap;

            case 2:
                Map teacherMap = userMapper.teacherLogin(user);
                teacherMap.put("type",2);
                return teacherMap;

            case 3:
                Map leaderMap = userMapper.leaderLogin(user);
                leaderMap.put("type",3);
                return leaderMap;


        }
        return null;

    }

    @Override
    public String getFilePath(String fileName) {


        return userMapper.getFilePath(fileName);
    }

    @Override
    public List<CommonFile> getUserFileList() {
        return userMapper.getUserFileList();
    }


    @Override
    public Integer updatePass(Map map) {

        if(!map.get("password").equals(map.get("repeatPass")))
            return 0;

        if((Integer)map.get("type") == 1){
            return userMapper.updateStudentPass(map);
        }else if((Integer)map.get("type") == 2){
            return userMapper.updateStudentPass(map);
        }else {
            return userMapper.updateLeaderPass(map);
        }
    }

    @Override
    public Integer judgePass(Map map) {
        if((Integer)map.get("type") == 1){
            return userMapper.judgeStudentPass(map);
        }else if((Integer)map.get("type") == 2){
            return userMapper.judgeTeacherPass(map);
        }else {
            return userMapper.judgeLeaderPass(map);
        }
        //return userMapper.updateStudentPass(map);
    }

    @Override
    public Task isEffectiveTask(Map map) {
        return userMapper.isEffectiveTask(map);
    }

    @Override
    public Integer getStudentDepartIdByUsername(String username) {
        return userMapper.getStudentDepartIdByUsername(username);
    }

    @Override
    public Integer getTeacherDepartIdByUsername(String username) {
        return userMapper.getTeacherDepartIdByUsername(username);
    }
}
