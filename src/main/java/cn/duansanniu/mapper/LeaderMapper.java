package cn.duansanniu.mapper;

import org.springframework.stereotype.Repository;

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
}
