package cn.duansanniu.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author duansanniu
 * @create 2019-09-15 10:30 上午
 */
@Getter
@Setter
public class ResponseEntity  {


    /**
     * 1  获取数据成功
     * 0  获取数据失败
     */
    //返回值的状态码
    private Integer code;

    //返回的文字信息
    private String info;

    //返回的数据
    private Object returnData;

    public ResponseEntity(Integer code, String info, Object returnData) {
        this.code = code;
        this.info = info;
        this.returnData = returnData;
    }

    public ResponseEntity() {
    }
}
