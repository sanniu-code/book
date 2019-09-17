package cn.duansanniu.service;

import cn.duansanniu.entity.User;

import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 9:51 上午
 */
public interface UserService {
    public Map login(User user);

    public String getFilePath(String fileName);


}
