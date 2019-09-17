package cn.duansanniu.mapper;

import cn.duansanniu.entity.Leader;
import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Teacher;
import cn.duansanniu.entity.User;
import org.springframework.stereotype.Repository;

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

    public String getFilePath(String fileName);
}
