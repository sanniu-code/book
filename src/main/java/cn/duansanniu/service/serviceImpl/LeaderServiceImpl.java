package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.mapper.LeaderMapper;
import cn.duansanniu.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
