package cn.duansanniu.controller;

import cn.duansanniu.entity.Subjects;
import cn.duansanniu.entity.Task;
import cn.duansanniu.entity.Teacher;
import cn.duansanniu.service.LeaderService;
import cn.duansanniu.utils.ResponseEntity;
import com.sun.org.glassfish.gmbal.ParameterNames;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 19:32 下午
 */
@Controller
@RequestMapping("/leader")
@Api("用于领导的API")
public class LeaderController {

    @Autowired
    private LeaderService leaderService;


    @PostMapping("/uploadCommonFile")
    @ResponseBody
    @ApiOperation("领导上传文件")
    public ResponseEntity uploadCommonFile(MultipartFile file, HttpServletRequest request){
        try{
            if(file.isEmpty()){
                return new ResponseEntity(0,"你好调皮",null);
            }
            //获取上传文件的名字
            String name = file.getOriginalFilename();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) {
                path = new File("");
            }
            //路径
            File upload = new File(path.getAbsolutePath(),"static/commonFile/");
            if(!upload.exists()){
                upload.mkdirs();
            }
            File commonFile = new File(upload.getPath()+File.separator+name);
            file.transferTo(commonFile);

            //保存这个文件的所有信息
            Map<String,String> map = new HashMap<>();
            map.put("name",commonFile.getName());
            map.put("url",commonFile.getPath());
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            map.put("createTime",sdf.format(d));

            Integer num = leaderService.preserveCommonFileInfo(map);
            if(num >= 1){
                return new ResponseEntity(1,"上传成功",null);
            }else {
                return new ResponseEntity(1,"上传失败",null);
            }
        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }

    /*
    *   获取本系的所有老师信息
    * */
    @GetMapping("getTeachersByDepartId")
    @ResponseBody
    @ApiOperation("获取本系的所有老师")
    @ApiImplicitParam(name = "departId",value = "系的id",required = true,paramType = "query")
    public ResponseEntity getTeachersByDepartId(
            @RequestParam("departId") Integer departId
    ){
        try{
            List<Teacher> teachers =  leaderService.getTeachersByDepartId(departId);
            return new ResponseEntity(1,"获取成功",teachers);

        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    @PostMapping("importSubject")
    @ResponseBody
    @ApiOperation("领导导入课题")
    public ResponseEntity importSubject(
            HttpServletRequest request,
            @RequestBody  Subjects subjects
    ){
        try{
            //获取领导的信息
            Map leaderMap = (HashMap)request.getSession().getAttribute("userInfo");
            //当前领导的系别
            Integer departId = (Integer)leaderMap.get("departId");

            Map m = new HashMap();
            m.put("username",subjects.getTeacher().getUsername());
            m.put("title",subjects.getTitle());
            m.put("detail",subjects.getDetail());
            m.put("type",subjects.getType());

            Integer flag = leaderService.importSubject(m);
            if(flag <= 0) return new ResponseEntity(0,"导入失败",null);
            return new ResponseEntity(1,"导入成功",null);

        }catch (Exception e){
            return new ResponseEntity(0,"导入失败",null);
        }
    }

    @PostMapping("updateSubject")
    @ResponseBody
    @ApiOperation("修改课题")
    public ResponseEntity updateSubject(
            HttpServletRequest request,
            @RequestBody  Subjects subjects
    ){
        try{
            //获取领导的信息
            Map leaderMap = (HashMap)request.getSession().getAttribute("userInfo");
            //当前领导的系别
            Integer departId = (Integer)leaderMap.get("departId");

            Map m = new HashMap();
            m.put("username",subjects.getTeacher().getUsername());
            m.put("title",subjects.getTitle());
            m.put("detail",subjects.getDetail());
            m.put("type",subjects.getType());
            m.put("id",subjects.getId());

            Integer flag = leaderService.updateSubject(m);
            if(flag <= 0) return new ResponseEntity(0,"导入失败",null);
            return new ResponseEntity(1,"导入成功",null);

        }catch (Exception e){
            return new ResponseEntity(0,"导入失败",null);
        }
    }

    @GetMapping("deleteSubject")
    @ResponseBody
    @ApiOperation("删除课题")
    @ApiImplicitParam(name = "id",value = "课题的id",required = true,paramType = "query")
    public ResponseEntity deleteSubject(
            @RequestParam("id") Integer id,
            HttpServletRequest request
    ){
        try{
            //判断leader是否有权限删除这个 主题
                //获取这个题目所属的系别
            Integer departId = leaderService.getDepartIdBySubjectId(id);

            Integer leaderDepartId = (Integer)request.getSession().getAttribute("departId");
            if(departId != leaderDepartId) return new ResponseEntity(0,"删除失败",null);

            Integer flag = leaderService.deleteSubject(id);
            if(flag <= 0) return new ResponseEntity(0,"删除失败",null);
            return new ResponseEntity(1,"删除成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"删除失败",null);
        }
    }

    @PostMapping("/createTask")
    @ResponseBody
    @ApiOperation("创建任务")
    public ResponseEntity createTask(
           @RequestBody Task task,
           HttpServletRequest request
    ){
        try{
            Map userInfoMap = (HashMap)request.getSession().getAttribute("userInfo");
            Map map = new HashMap();
            map.put("name",task.getName());
            map.put("year",task.getYear());
            map.put("departId",userInfoMap.get("departId").toString());
            map.put("startTime",task.getStartTime());
            map.put("endTime",task.getEndTime());
            Integer num = leaderService.createTask(map);

            if(num <= 0)
                return new ResponseEntity(0,"创建失败",null);

            //创建year 文件夹
            File root = new File(ResourceUtils.getFile("classpath:").getPath());
            if(!root.exists())
                root = new File("");
            //获取studentFile文件
            File studentFile = new File(root.getAbsolutePath(),"static/"+task.getYear()+File.separator);
            if(!studentFile.exists())
                studentFile.mkdirs();

            return new ResponseEntity(1,"创建成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"创建失败",null);
        }
    }


}
