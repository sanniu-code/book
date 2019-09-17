package cn.duansanniu.controller;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import cn.duansanniu.service.StudentService;
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
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
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
            @PathVariable("pageNum") Integer pageNum
    ){
        try{
            if(pageNum == null || pageNum == 0){
                pageNum = 1;
            }
            if(pageSize == null || pageSize == 0){
                pageSize = 10;
            }
            PageHelper.startPage(pageNum,pageSize);
            List<Subjects> subjects = studentService.getSubjects();
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
    @PostMapping("/getSubject")
    @ApiOperation(value = "获取当前用户所报课题的相关信息")
    public ResponseEntity getSubject(@RequestBody Student student){
        try{
            Subjects subject = studentService.getSubject(student);

            return new ResponseEntity(1,"获取成功",subject);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    /**
     * 学生 报课题
     */
    @PostMapping("/selectSubject")
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
                return new ResponseEntity(1,"选题成功",null);
            }else {
                return new ResponseEntity(0,"不可重复选择",null);
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

            String year = dateUtils.getYear(map.get("createTime").toString());
            String fileName = null;

            /**
             * 1 开题报告
             * 2 中期报告
             * 3 论文格式查重报告
             * 4 论文查重率
             * 5 论文
             */
            switch(type){
                case 1:
                    fileName = "附件1 吕梁学院"+year+"届毕业论文（设计）开题报告";
                    break;
                case 2:
                    fileName = "附件7 吕梁学院"+year+"届毕业论文（设计）中期报告";
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
    @ApiImplicitParam(name = "fileName",value = "文件的名字",required = true,paramType = "query")
    public ResponseEntity downloadStudentFile(
            @RequestParam(value = "fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{

            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            Map dateMap = new HashMap();
            dateMap.put("username",username);
            dateMap.put("name",fileName);

            //获取文件的路径
            String url = studentService.getStudentFilePath(dateMap);
            boolean flag = studentFileUtils.studentDownload(url,fileName,response,request);
            if(!flag) return new ResponseEntity(0,"下载失败",null);
            return new ResponseEntity(1,"下载成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }

    /**
     * 用于下载开题任务书
     */
    @GetMapping("downReport")
    @ResponseBody
    @ApiOperation("用于下载开题要求")
    @ApiImplicitParam(name = "fileName",value = "文件名字",required = true,paramType = "query")
    public ResponseEntity downReport(
            @RequestParam("fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            //这个用户的 这个文件
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            Map dateMap = new HashMap();
            dateMap.put("username",username);
            dateMap.put("name",fileName);


            //查找文件的全路径
            String url = studentService.getMissionBookPath(dateMap);

            boolean flag  = studentFileUtils.studentDownload(url,fileName,response,request);
            if(!flag) return new ResponseEntity(0,"下载失败",null);
            return new ResponseEntity(1,"下载成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }


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
