package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.Leader;
import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Teacher;
import cn.duansanniu.entity.User;
import cn.duansanniu.mapper.UserMapper;
import cn.duansanniu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
                return userMapper.studentLogin(user);

            case 2:
                return userMapper.teacherLogin(user);

            case 3:
                return userMapper.leaderLogin(user);


        }
        return null;

    }
}
