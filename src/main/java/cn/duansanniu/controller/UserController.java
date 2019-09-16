package cn.duansanniu.controller;

import cn.duansanniu.entity.Leader;
import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Teacher;
import cn.duansanniu.entity.User;
import cn.duansanniu.service.UserService;
import cn.duansanniu.utils.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 9:45 上午
 */
@Controller
@RequestMapping("/user")
@Api("用于所有用户的API")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "用于用户登录的")
    public ResponseEntity login(
           @RequestBody User user,
           HttpServletRequest request
    ){
        try{
            //登录检查

            if(user.getPassword().length() == 0 || user.getUsername().length() == 0
                || user.getCode().length() == 0 || user.getType() == null
            ) return new ResponseEntity(0,"请完善数据",null);


            Map o = userService.login(user);
            if(o == null)
                return new ResponseEntity(0,"账号或密码错误",null);

            //保存用户信息
            HttpSession session = request.getSession();
            session.setAttribute("type",user.getType());
            session.setAttribute("userInfo",o);

            //获取相关信息
            return new ResponseEntity(1,"登录成功",o);
        }catch(Exception e){
            return new ResponseEntity(0,"登录失败",null);
        }
    }
}
