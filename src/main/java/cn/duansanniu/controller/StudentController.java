package cn.duansanniu.controller;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.Subjects;
import cn.duansanniu.service.StudentService;
import cn.duansanniu.utils.ResponseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.glassfish.gmbal.ParameterNames;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

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
            @ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "subjectId",value = "课题的id",required = true,dataType = "String", paramType = "query")
    })
    public ResponseEntity selectSubject(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "subjectId") Integer subjectId
    ){
        try{
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
            String url = studentService.getFilePath(fileName);
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
     * 用于学生表文件的上传
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    @ApiOperation(value = "用于学生表文件的上传")
    public ResponseEntity uploadFile(){
        try{
            return new ResponseEntity(1,"上传成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }

}
