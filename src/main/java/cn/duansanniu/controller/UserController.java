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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
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

    /**
     * 有关常用表的下载
     */
    @GetMapping("/download")
    @ResponseBody
    @ApiOperation(value = "用于下载常用表")
    @ApiImplicitParam(name = "fileName",value = "文件的名字",required = true,paramType = "query")
    public ResponseEntity downloadApplyTable(
            @RequestParam(value = "fileName") String fileName, HttpServletRequest request, HttpServletResponse response){
        try{

            //获取文件路径
            String url = userService.getFilePath(fileName);
            if(url.length() < 0) return new ResponseEntity(0,"下载失败",null);
            File file = new File(url);
            FileInputStream fis = new FileInputStream(file);
            //获取后缀名
            String extendFileName = fileName.substring(fileName.lastIndexOf("."));
            //动态设置响应类型 根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            //获取输出流对象
            ServletOutputStream os = response.getOutputStream();
            //下载文件使用 spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fis,os);

            return new ResponseEntity(0,"下载成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }
}
