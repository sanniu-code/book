package cn.duansanniu.controller;

import cn.duansanniu.entity.*;
import cn.duansanniu.service.UserService;
import cn.duansanniu.utils.ResponseEntity;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

    @Autowired
    private DefaultKaptcha captchaProducer;

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

            String code = request.getSession().getAttribute("vrifyCode").toString();
            //验证码验证
            if(!code.toLowerCase().equals(user.getCode().toLowerCase())) return new ResponseEntity(0,"验证码错误",null);


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


    @GetMapping("/loginOut")
    @ResponseBody
    @ApiOperation("退出登录")
    public ResponseEntity loginOut(
            HttpServletRequest request
    ){
        try{
            HttpSession session = request.getSession();
            session.removeAttribute("userInfo");
            session.removeAttribute("type");
            return new ResponseEntity(1,"退出成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"退出错误",null);
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

    /**
     * 验证码获取
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/createImg")
    @ApiOperation(value = "生成验证码")
    @ResponseBody
    public void createImg(
            HttpServletResponse response,
            HttpServletRequest request
    ){
        try{
            byte[] captchaChallengeAsJpeg = null;
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            // 生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            request.getSession().setAttribute("vrifyCode", createText);

            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);

            // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();



        }catch (Exception e){

        }
    }



}
