package cn.duansanniu.controller;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import cn.duansanniu.service.StudentService;
import cn.duansanniu.service.TeacherService;
import cn.duansanniu.utils.ResponseEntity;
import cn.duansanniu.utils.StudentFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 19:31 下午
 */
@Controller
@RequestMapping("/teacher")
@Api("用于教师的API")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;


    /**
     * 上传开题要求
     */
    @PostMapping("uploadMissionBook")
    @ResponseBody
    @ApiOperation("指导老师上传开题要求")
    @ApiImplicitParam(name = "username",value = "用户账号",required = true,paramType = "query")
    public ResponseEntity uploadMissionBook(
            MultipartFile file,
            @RequestParam("username") String username,
            HttpServletRequest request
    ){
        try{

            //判断是否是指导老师
            HttpSession session = request.getSession();
            Map map = (HashMap)session.getAttribute("userInfo");
            Integer teacherId = (Integer)map.get("id");

            //封装了 指导老师 与 学生
            Map dateMap = new HashMap();
            dateMap.put("username",username);
            dateMap.put("teacherId",teacherId);

            Integer num = teacherService.isGuideTeacher(dateMap);
            //这个学生的知道老师 不是这个学生
            if(num <= 0) return new ResponseEntity(0,"你好调皮",null);

            /************** 上传文件  *****************************/
            //上传的文件的名字
            String name = file.getOriginalFilename();
            //获取后缀名
            String extendFileName = name.substring(name.lastIndexOf("."));

            //上传的文件为空
            if(file.isEmpty()) return new ResponseEntity(0,"你好调皮",null);

            //获取静态资源的路径
            File f = new File(ResourceUtils.getURL("classpath:").getPath());

            if(!f.exists()) f = new File("");

            //获取 teacherFile 路径
            File teacherFile = new File(f.getAbsolutePath(),"static/teacherFile/");
            if(!teacherFile.exists()) teacherFile.mkdirs();

            //获取这个学生的信息
            Student student = studentService.getStudentInfo(username);
            Date date = student.getCreateTime();
            String year = new SimpleDateFormat("yyyy").format(date);


            //文件的新名字
            String newName = "吕梁学院"+year+"届毕业论文（设计）课题任务书 ————"+username+extendFileName;
            //最终的文件路径
            File finalFile = new File(teacherFile.getPath() + File.separator +newName );
            //转存
            file.transferTo(finalFile);

            //保存到数据库
            Map<String,Object> finalMap = new HashMap();
            finalMap.put("username",username);
            finalMap.put("name",newName);
            finalMap.put("url",finalFile.getPath());
            finalMap.put("teacherId",teacherId);
            finalMap.put("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));


            Integer flag = teacherService.storeReport(finalMap);
            if(flag <= 0) return new ResponseEntity(0,"上传失败",null);

            return new ResponseEntity(1,"上传成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }


    /**
     * 获取还未审核的文件
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("getNotExamineFiles")
    @ApiOperation("获取还未审核通过的文件")
    public ResponseEntity getNotExamineFiles(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            List<StudentUploadFile> studentUploadFiles = teacherService.getNotExamineFiles(username);
            return new ResponseEntity(1,"获取成功",studentUploadFiles);

        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    /**
     * 驳回学生上传的文件
     * 0 是默认状态  还未经过审核
     * 1 审核通过
     * 2 审核驳回
     * @param studentUploadFile
     * @return
     */
    @ResponseBody
    @GetMapping("FailExamineStudentUploadFile")
    @ApiOperation("驳回学生上传的文件")
    public ResponseEntity FailExamineStudentUploadFile(
            @RequestBody StudentUploadFile studentUploadFile
    ){
        try{
            Integer num = teacherService.failExamineStudentUploadFile(studentUploadFile);
            if(num <= 0) return new ResponseEntity(0,"驳回失败",null);
            return new ResponseEntity(1,"驳回成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"驳回失败",null);
        }
    }



    /**
     * 审核学生上传的文件
     * @param studentUploadFile
     * @return
     */
    @ResponseBody
    @PostMapping("ExamineStudentUploadFile")
    @ApiOperation("审核学生上传的文件")
    public ResponseEntity examineStudentUploadFile(
            @RequestBody StudentUploadFile studentUploadFile
    ){
        try{
            Integer num = teacherService.examineStudentUploadFile(studentUploadFile);
            if(num <= 0) return new ResponseEntity(0,"修改失败",null);
            return new ResponseEntity(0,"修改成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"修改失败",null);
        }
    }


    /**
     * 获取当前老师所有学生的上传的文件信息
     * @return
     */
    @GetMapping("getExamineFiles")
    @ResponseBody
    public ResponseEntity getExamineFiles(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            List<StudentUploadFile> studentUploadFiles = teacherService.getExamineFiles(username);
            return new ResponseEntity(1,"获取成功",studentUploadFiles);
        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }




}