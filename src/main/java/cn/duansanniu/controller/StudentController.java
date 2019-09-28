package cn.duansanniu.controller;

import cn.duansanniu.entity.*;
import cn.duansanniu.service.StudentService;
import cn.duansanniu.service.UserService;
import cn.duansanniu.utils.DateUtils;
import cn.duansanniu.utils.ResponseEntity;
import cn.duansanniu.utils.StudentFileUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:24 下午
 */
@Controller
@RequestMapping("/student")
@Api("关于学生的API")
public class StudentController {


    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentFileUtils studentFileUtils;

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private UserService userService;



    /**
     * 获取老师课题列表
     */
    @GetMapping("/getSubjects/{pageSize}/{pageNum}")
    @ResponseBody
    @ApiOperation(value="获取所有的课题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页数据数量",required = true,paramType = "path"),
            @ApiImplicitParam(name = "pageNum",value = "页码",required = true,paramType = "path")
    })
    public ResponseEntity getSubjects(
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("pageNum") Integer pageNum,
            HttpServletRequest request
    ){
        try{
            if(pageNum == null || pageNum == 0){
                pageNum = 1;
            }
            if(pageSize == null || pageSize == 0){
                pageSize = 10;
            }
            //获取当前任务

            /*Task task = (Task)request.getSession().getAttribute("task");
            Integer taskId = task.getId();*/
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            //获取任务的id
            Map tempMap = new HashMap();
            tempMap.put("departId",departId);
            tempMap.put("time",new Date());


            Task task = userService.isEffectiveTask(tempMap);
            if(task == null){
                return new ResponseEntity(0,"网络异常",null);
            }



            PageHelper.startPage(pageNum,pageSize);
            List<Subjects> subjects = studentService.getSubjects(task.getId());
            PageInfo pageInfo = new PageInfo(subjects);
            return new ResponseEntity(1,"获取信息成功",pageInfo);
        }catch(Exception e){
            return new ResponseEntity(0,"获取信息失败",null);
        }
    }

    /**
     * 获取这个用户所报的课题相关信息
     */
    @ResponseBody
    @GetMapping("/getSubject")
    @ApiOperation(value = "获取当前用户所报课题的相关信息")
    public ResponseEntity getSubject(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();

            Subjects subject = studentService.getSubject(username);

            return new ResponseEntity(1,"获取成功",subject);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    /**
     * 学生 报课题
     */
    @GetMapping("/selectSubject")
    @ResponseBody
    @ApiOperation(value = "学生选择课题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId",value = "课题的id",required = true,dataType = "String", paramType = "query")
    })
    public ResponseEntity selectSubject(
            @RequestParam(value = "subjectId") Integer subjectId,
            HttpServletRequest request
    ){
        try{

            Map m = (HashMap)request.getSession().getAttribute("userInfo");

            String username = m.get("username").toString();
            Map<String,Object> map = new HashMap<>();
            map.put("username",username);
            map.put("subjectId",subjectId);

            //选题
            Integer num = studentService.selectSubject(map);
            if(num >= 1){
                //选题成功后 就应该将课题申请表与学生进行绑定
                Map tempMap = new HashMap();
                tempMap.put("studentusername",username);
                tempMap.put("subjectId",subjectId);
                Integer tempNum = studentService.changeSubjectFileUsername(tempMap);
                if(tempNum <=0 ){
                    return new ResponseEntity(0,"选题失败",null);
                }

                return new ResponseEntity(1,"选题成功",null);
            }else {
                return new ResponseEntity(0,"选题失败",null);
            }



        }catch(Exception e){
            return new ResponseEntity(0,"选题失败",null);
        }
    }




    /**
     * 上传学生文件
     */
    @PostMapping("/uploadStudentFile")
    @ResponseBody
    @ApiOperation(value = "上传学生文件")
    @ApiImplicitParam(name = "type",value = "上传文件的类型 1 开题报告 2 中期报告 3 论文格式查重报告 4 论文查询率 5 论文",required = true,paramType = "query")
    public ResponseEntity uploadStudentFile(
            @RequestParam("type") Integer type,
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            //获取任务的年份
            //map.get("departId")
            //String year = dateUtils.getYear(map.get("createTime").toString());
            String fileName = null;

            //获取当前学生 处于哪个阶段
            Integer stage = studentService.getStudentStage(map.get("username").toString());
            //stage 当前学生正处于的阶段
            //type  要上传的文件的阶段
            //这是权限问题
            if(type > stage) return new ResponseEntity(2,"您还没有权限",null);


            /**
             * 1 开题报告
             * 2 中期报告
             * 3 论文格式查重报告
             * 4 论文查重率
             * 5 论文
             */
            switch(type){
                case 1:
                    fileName = "毕业论文（设计）开题报告";
                    break;
                case 2:
                    fileName = "毕业论文（设计）中期报告";
                    break;
                case 3:
                    fileName = "格式查重报告";
                    break;
                case 4:
                    fileName = "论文查重率";
                    break;
                case 5:
                    fileName = "论文";
                    break;
            }




            Map fileMap = studentFileUtils.studentUpload(file,fileName,map);
            fileMap.put("type",type);
            Integer num = studentService.storePath(fileMap);
            if(num <= 0) return new ResponseEntity(0,"上传失败",null);
            return new ResponseEntity(1,"上传成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }



    /**
     * 学生文件的下载
     */
    @GetMapping("/downloadStudentFile")
    @ResponseBody
    @ApiOperation("学生文件的下载")
    @ApiImplicitParam(name = "type",value = "文件类型",required = true,paramType = "query")
    public ResponseEntity downloadStudentFile(
            @RequestParam(value = "type") Integer type,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            String fileName = "";
            switch(type){
                case 1:
                    fileName = "毕业论文（设计）开题报告";
                    break;
                case 2:
                    fileName = "毕业论文（设计）中期报告";
                    break;
                case 3:
                    fileName = "格式查重报告";
                    break;
                case 4:
                    fileName = "论文查重率";
                    break;
                case 5:
                    fileName = "论文";
                    break;
            }

            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            Map dateMap = new HashMap();
            dateMap.put("username",username);
            dateMap.put("type",type);

            //获取文件的路径
            String url = studentService.getStudentFilePath(dateMap);
            boolean flag = studentFileUtils.studentDownload(url,fileName,response,request);
            if(!flag) return new ResponseEntity(0,"下载失败",null);
            return new ResponseEntity(1,"下载成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }


    @GetMapping("/getStudentFileInfo")
    @ResponseBody
    @ApiOperation("获取用户上传的文件详情")
    @ApiImplicitParam(name="type",value = "文件类型",required = true,paramType = "query")
    public ResponseEntity getStudentFileInfo(
            @RequestParam("type") Integer type,
            HttpServletRequest request
    ){
        try{
            String fileName = "";
            switch (type){
                case 1:
                    fileName="课题申请表";
                    break;
                case 2:
                    fileName="课题任务书";
                    break;
                case 3:
                    fileName="指导记录";
                    break;
                case 4:
                    fileName="中期检查表";
                    break;
                case 5:
                    fileName="答辩记录";
                    break;
                case 6:
                    fileName="成绩评定表";
                    break;

            }

            Map map = new HashMap();
            Map m = (HashMap)request.getSession().getAttribute("userInfo");

            map.put("username",m.get("username").toString());
            map.put("type",type);
            StudentUploadFile studentUploadFile = studentService.getStudentFileInfo(map);
            return new ResponseEntity(1,"获取成功",studentUploadFile);
        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    /**
     * 用于下载开题任务书
     */
    @GetMapping("downTeacherUploadFile")
    @ResponseBody
    @ApiOperation("用于下载老师上传的文件")
    public ResponseEntity downTeacherUploadFile(
            @RequestParam Integer type,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            //这个用户的 这个文件
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            TeacherUploadFile teacherUploadFile = new TeacherUploadFile();
            teacherUploadFile.setUsername(map.get("username").toString());
            teacherUploadFile.setType(type);

            //查找文件的全路径
            String url = studentService.getMissionBookPath(teacherUploadFile);
            String fileName = "课题任务书";
            boolean flag  = studentFileUtils.studentDownload(url,fileName,response,request);
            if(!flag) return new ResponseEntity(0,"下载失败",null);
            return new ResponseEntity(1,"下载成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }

    /**
     * 获取指导老师上传的文件
     * @return
     */
    @GetMapping("getTeacherUploadFile")
    @ResponseBody
    @ApiOperation("获取指导老师上传的文件")
    @ApiImplicitParam(name = "type",value = "1 申请书 2 任务书 3 指导记录 4 中期检查表 5 答辩记录 6成绩评定表",paramType = "query",required = true)
    public ResponseEntity getTeacherUploadFile(
            @RequestParam Integer type,
            HttpServletRequest request
    ){
        try{


            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            TeacherUploadFile teacherUploadFile = new TeacherUploadFile();
            teacherUploadFile.setType(type);
            teacherUploadFile.setUsername(username);
            TeacherUploadFile returnData = studentService.getTeacherUploadFile(teacherUploadFile);
            return new ResponseEntity(1,"获取成功",returnData);
        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }


    /**
     * 获取未通过审核的消息
     * @param request
     * @return
     */
    @GetMapping("getFailExamineFile")
    @ResponseBody
    @ApiOperation("获取未通过审核的消息")
    public ResponseEntity getFailExamineFile(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            List<StudentUploadFile> studentUploadFiles = studentService.getFailExamineFile(username);
            return new ResponseEntity(1,"获取成功",studentUploadFiles);
        }catch(Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }
}
