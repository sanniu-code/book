package cn.duansanniu.controller;

import cn.duansanniu.entity.*;
import cn.duansanniu.service.LeaderService;
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
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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




            //判断这个用户的系是否存在有效的任务
            if(user.getType() == 1 || user.getType() == 2){
                Integer departId = 0;
                if(user.getType() == 1){
                    departId = userService.getStudentDepartIdByUsername(user.getUsername());
                }else {
                    departId = userService.getTeacherDepartIdByUsername(user.getUsername());
                }

                if(departId <= 0){
                    return new ResponseEntity(0,"账号错误",null);
                }
                Map map = new HashMap();
                map.put("departId",departId);
                map.put("time",new Date());
                Task task = userService.isEffectiveTask(map);
                if(task == null){
                    return new ResponseEntity(2,"您还没有权限",null);
                }
                request.getSession().setAttribute("task",task);
            }








            String code = request.getSession().getAttribute("vrifyCode").toString();
            //验证码验证
            if(!code.toLowerCase().equals(user.getCode().toLowerCase())) return new ResponseEntity(0,"验证码错误",null);


            Map o = userService.login(user);
            if(o == null)
                return new ResponseEntity(0,"账号或密码错误",null);

            //判断这个用户是否是大四的
            if(user.getType() == 1){
                Integer year = (Integer)o.get("year");
                Date createTime = (Date)o.get("createTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                String createTimeYear = simpleDateFormat.format(createTime);
                Integer startYear=Integer.valueOf(createTimeYear) + (year-1); //要开始选题了
                Integer endYear=Integer.valueOf(createTimeYear) + year; //要开始选题了\
                String startTimeStr = startYear+"-09-01 00:00:00";
                String endTimeStr = endYear+"-06-01 00:00:00";
                Date startTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTimeStr);
                Date endTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTimeStr);







                if(startTime.getTime() > new Date().getTime() || endTime.getTime() < new Date().getTime()){
                    return new ResponseEntity(2,"您还没有权限",null);
                }
            }


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
    @GetMapping("/downloadApplyTable")
    @ResponseBody
    @ApiOperation(value = "用于下载常用表")
    @ApiImplicitParam(name = "id",value = "文件的id",required = true,paramType = "query")
    public ResponseEntity downloadApplyTable(
            @RequestParam(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response){
        try{

            //获取文件路径
            String url = userService.getFilePath(id);
            if(url.length() < 0) return new ResponseEntity(0,"下载失败",null);
            File file = new File(url);
            FileInputStream fis = new FileInputStream(file);
            String fileName = url.substring(url.lastIndexOf("\\")+1);
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


    @GetMapping("/getUserFileList")
    @ResponseBody
    @ApiOperation("获取用户的常用文件列表")
    public ResponseEntity getUserFileList(){
        try{
            List<CommonFile> list = userService.getUserFileList();
            return new ResponseEntity(1,"获取成功",list);

        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
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

    @PostMapping("/updatePass")
    @ResponseBody
    @ApiOperation("修改用户密码")
    public ResponseEntity updatePass(
        @RequestBody Map map,
        HttpServletRequest request
    ){
        try{
            HttpSession session = request.getSession();
            Map m = (HashMap)session.getAttribute("userInfo");
            String username = m.get("username").toString();

            if( map.get("oldPass") == null || map.get("password") == null ||
                map.get("repeatPass") == null || map.get("type") == null
            ){
                return new ResponseEntity(0,"修改密码失败",null);
            }
            map.put("username",username);
            map.put("type",(Integer)m.get("type"));



            //查看这个原密码是否正确
            Integer flag = userService.judgePass(map);
            if(flag <= 0)
                return new ResponseEntity(0,"密码修改失败",null);

            Integer num = userService.updatePass(map);
            if(num <= 0)
                return new ResponseEntity(0,"密码修改失败",null);
            return new ResponseEntity(1,"密码修改成功",null);


        }catch (Exception e){
            return new ResponseEntity(0,"密码修改失败",null);
        }
    }


    @PostMapping("/judgePass")
    @ResponseBody
    @ApiOperation("修改密码时 判断用户输入的原密码是否正确")
    public ResponseEntity judgePass(
            @RequestBody  Map map,
            HttpServletRequest request
    ){
        try{
            Map userMap = (HashMap)request.getSession().getAttribute("userInfo");
            map.put("username",userMap.get("username").toString());
            map.put("type",(Integer)userMap.get("type"));
            Integer num = userService.judgePass(map);
            if(num <= 0)
                return  new ResponseEntity(0,"网络异常",null);
            return new ResponseEntity(1,"查询成功",null);


        }catch (Exception e){
            return  new ResponseEntity(0,"网络异常",null);
        }
    }

//    @GetMapping("/getUserTaskInfo")
//    @ResponseBody
//    public ResponseEntity getUserTaskInfo(
//            HttpServletRequest request
//    ){
//        try{
//            Map map = (HashMap)request.getSession().getAttribute("userInfo");
//            Integer departId = userService.getStudentDepartIdByUsername(map.get("username").toString());
//            if(departId <= 0){
//                return new ResponseEntity(0,"账号错误",null);
//            }
//            Map tempMap = new HashMap();
//            tempMap.put("departId",departId);
//            tempMap.put("time",new Date());
//            Task task = userService.isEffectiveTask(map);
//            if(task == null){
//                return new ResponseEntity(2,"您还没有权限",null);
//            }
//            return new ResponseEntity(2,"您还没有权限",null);
//
//
//        }catch(Exception e){
//
//        }
//    }


    @GetMapping("/getMenu")
    @ResponseBody
    @ApiOperation("获取用户对应的")
    public ResponseEntity getMenu(
            HttpServletRequest request
    ){
        try{
            Integer userType = (Integer)request.getSession().getAttribute("type");


            List<Menu> menus = userService.getMenu(userType);

            return new ResponseEntity(1,"获取成功",menus);
        }catch (Exception e){
            return new ResponseEntity(0,"网络异常",null);
        }
    }


}
