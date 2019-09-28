package cn.duansanniu.controller;

import cn.duansanniu.entity.*;
import cn.duansanniu.service.StudentService;
import cn.duansanniu.service.TeacherService;
import cn.duansanniu.service.UserService;
import cn.duansanniu.utils.ResponseEntity;
import cn.duansanniu.utils.StudentFileUtils;
import cn.duansanniu.utils.TeacherFileUtils;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private TeacherFileUtils teacherFileUtils;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;


    /**
     * 上传普通文件
     */
    @PostMapping("uploadFile")
    @ResponseBody
    @ApiOperation("上传学生有关的文件文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "上传文件的类型 1 课题申请表 2 课题任务书 3 指导记录 4 中期检查表 5 答辩记录 6成绩评定表",required = true,paramType = "query"),
            @ApiImplicitParam(name = "username",value = "学生用户名",paramType = "query",required = true)
    })
    public ResponseEntity uploadFile(
            MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("type") Integer type,
            HttpServletRequest request
    ){
        try{
            if(type == null  || username == null){
                return new ResponseEntity(0,"上传失败",null);
            }
            /**
             *
             * 1 课题任务书
             * 2 指导记录
             * 3 中期检查表
             * 4 答辩记录
             * 5 成绩评定表
             */


            /*判断是否是指导老师  开始*/
            HttpSession session = request.getSession();
            Map map = (HashMap)session.getAttribute("userInfo");
            String teacherUsername = (String)map.get("username");


            //封装了 指导老师 与 学生
            Map dateMap = new HashMap();
            dateMap.put("studentusername",username);
            dateMap.put("teacherUsername",teacherUsername);
            Integer num = teacherService.isGuideTeacher(dateMap);
            //这个学生的知道老师 不是这个学生
            if(num <= 0) return new ResponseEntity(0,"你好调皮",null);
            /*判断是否是指导老师  结束*/


            //上传的文件的名字
            String name = file.getOriginalFilename();
            //获取后缀名
            String extendFileName = name.substring(name.lastIndexOf("."));

            //上传的文件为空
            if(file.isEmpty()) return new ResponseEntity(0,"你好调皮",null);

            Integer departId = userService.getTeacherDepartIdByUsername(map.get("username").toString());
            /* 获取年份开始 */
            Map tempMap = new HashMap();
            tempMap.put("departId",departId);
            tempMap.put("time",new Date());
            Task task = userService.isEffectiveTask(tempMap);
            if(task == null){
                return null;
            }
            String year = task.getYear();
            /* 获取年份结束 */

            String fileName = "";
            switch (type){
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

            String newName = fileName+"————"+username.toString()+extendFileName;

            //finalMap 中保存的是 文件的名字 url 创建时间
            Map finalMap =teacherFileUtils.teacherUpload(file,newName,map);
            //学生的username
            finalMap.put("username",teacherUsername);
            finalMap.put("studentusername",username);
            finalMap.put("fileType",type);


            Integer flag = teacherService.storePath(finalMap);
            if(flag <= 0) return new ResponseEntity(0,"上传失败",null);

            return new ResponseEntity(1,"上传成功",null);

        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }

    @PostMapping("/uploadApplyTable")
    @ResponseBody
    @ApiOperation("上传申请表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "课题名称",required = true,paramType = "query"),
            @ApiImplicitParam(name = "detail",value = "详情",required = true,paramType = "query"),
            @ApiImplicitParam(name = "type",value = "类型",required = true,paramType = "query"),
            @ApiImplicitParam(name = "origin",value = "题目来源",required = true,paramType = "query"),
            @ApiImplicitParam(name = "address",value = "答辩地址",required = true,paramType = "query")

    })
    public ResponseEntity uploadApplyTable(

        @RequestParam("title") String title,
        @RequestParam("detail") String detail,
        @RequestParam("type") String type,
        @RequestParam("origin") String origin,
        @RequestParam("address") String address,
        MultipartFile multipartFile,
        HttpServletRequest request
    ){
        try{

            /**
             * title 课题名称
             * origi 课题来源
             * type 课题类型
             * address 答辩地点
             * detail 详情
             * file 文件
             */
            //上传文件
                //保存文件到文件夹
                ApplyTable applyTable = new ApplyTable();
                applyTable.setTitle(title);
                applyTable.setDetail(detail);
                applyTable.setAddress(address);
                applyTable.setMultipartFile(multipartFile);
                applyTable.setOrigin(origin);
                applyTable.setType(type);


                String name = multipartFile.getOriginalFilename();
                String extendFileName = name.substring(name.lastIndexOf("."));
                String newName = title+"申请表"+extendFileName;
                Map map = (HashMap)request.getSession().getAttribute("userInfo");
                //返回上传的文件的信息
                Map returnMap = teacherFileUtils.teacherUpload(applyTable.getMultipartFile(),newName,map);
                returnMap.put("studentusername","");
                returnMap.put("username",map.get("username").toString());
                returnMap.put("fileType",1);
                //保存到数据库
                Integer tempNum = teacherService.storePath(returnMap);

                //返回对应的id
//            Integer fileId = null;
            if(tempNum <= 0){
                return new ResponseEntity(0,"新增失败",null);
            }
//            if(tempNum == 1){
//                //这是新增
//                fileId  = Integer.valueOf(returnMap.get("id").toString());
//            }else {
//                //这是修改的
//                fileId = tempNum;
//            }

            //获取当前系的任务的id
            /***********************/
            Task task = (Task)request.getSession().getAttribute("task");
            Integer taskId = task.getId();
            //新增课题

            applyTable.setUsername(map.get("username").toString());

            applyTable.setStatus(0);

            applyTable.setFileId(tempNum);

            applyTable.setTaskId(taskId);

            //id
            Integer num = teacherService.uploadApplyTable(applyTable);


            if(num <= 0){
                return new ResponseEntity(0,"新增失败",null);
            }
            //获取这条数据的id
            //修改文件信息
            //将id赋值到t_teacher_file 的subjectId中
            Map m = new HashMap();
            m.put("id",tempNum);
            m.put("subjectId",applyTable.getId());
            Integer flag = teacherService.updateSubjectFile(m);
            if(flag<= 0){
                return new ResponseEntity(0,"新增失败",null);
            }
            return new ResponseEntity(1,"成功",null);


        }catch(Exception e){
            return new ResponseEntity(0,"新增失败",null);
        }
    }


    /**
     * 上传开题要求
     */
//    @PostMapping("uploadMissionBook")
//    @ResponseBody
//    @ApiOperation("指导老师上传开题要求")
//    @ApiImplicitParam(name = "username",value = "用户账号",required = true,paramType = "query")
//    public ResponseEntity uploadMissionBook(
//            MultipartFile file,
//            @RequestParam("username") String username,
//            HttpServletRequest request
//    ){
//        try{
//
//            //判断是否是指导老师
//            HttpSession session = request.getSession();
//            //老师信息
//            Map map = (HashMap)session.getAttribute("userInfo");
//            //学生的账号
//            String StuUsername = (String)map.get("username");
//
//            //封装了 指导老师 与 学生
//            Map dateMap = new HashMap();
//            dateMap.put("username",username);
//            dateMap.put("StuUsername",StuUsername);
//
//            Integer num = teacherService.isGuideTeacher(dateMap);
//            //这个学生的知道老师 不是这个学生
//            if(num <= 0) return new ResponseEntity(0,"你好调皮",null);
//
//            /************** 上传文件  *****************************/
//            /* 获取年份开始 */
//            Integer departId = userService.getTeacherDepartIdByUsername(map.get("username").toString());
//            Map tempMap = new HashMap();
//            map.put("departId",departId);
//            map.put("time",new Date());
//            Task task = userService.isEffectiveTask(map);
//            if(task == null){
//                return null;
//            }
//            String year = task.getYear();
//            /* 获取年份结束 */
//
//            //上传的文件的名字
//            String name = file.getOriginalFilename();
//            //获取后缀名
//            String extendFileName = name.substring(name.lastIndexOf("."));
//
//            //上传的文件为空
//            if(file.isEmpty()) return new ResponseEntity(0,"你好调皮",null);
//
//            //获取静态资源的路径
//            File f = new File(ResourceUtils.getURL("classpath:").getPath());
//
//            if(!f.exists()) f = new File("");
//
//            //获取 teacherFile 路径
//            File teacherFile = new File(f.getAbsolutePath(),"static/"+File.separator+year);
//            if(!teacherFile.exists()) teacherFile.mkdirs();
//
//            //获取这个学生的信息
////            Student student = studentService.getStudentInfo(username);
////            Date date = student.getCreateTime();
////            String year = new SimpleDateFormat("yyyy").format(date);
//
//            String url = File.separator+map.get("departName")+File.separator+"teacherFile"+File.separator+map.get("username")+File.separator;
//
//            File tempFile = new File(teacherFile.getAbsolutePath(), url+ File.separator );
//            if(!tempFile.exists()) tempFile.mkdirs();
//
//            //文件的新名字
//            String newName = "吕梁学院"+year+"届毕业论文（设计）课题任务书 ————"+username+extendFileName;
//            //最终的文件路径
//            File finalFile = new File(tempFile.getAbsolutePath(), newName );
//            //转存
//            file.transferTo(finalFile);
//
//            //保存到数据库
//            Map<String,Object> finalMap = new HashMap();
//            finalMap.put("username",username);
//            finalMap.put("name",newName);
//            finalMap.put("url",finalFile.getPath());
////            finalMap.put("teacherId",teacherId);
//            finalMap.put("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
//
//
//
//            Integer flag = teacherService.storeReport(finalMap);
//            if(flag <= 0) return new ResponseEntity(0,"上传失败",null);
//
//            return new ResponseEntity(1,"上传成功",null);
//
//        }catch(Exception e){
//            return new ResponseEntity(0,"上传失败",null);
//        }
//    }





    /**
     * 获取还未审核的文件
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("getNotExamineFiles")
    @ApiOperation("获取还未审核的文件")
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
//    @ResponseBody
//    @PostMapping("FailExamineStudentUploadFile")
//    @ApiOperation("驳回学生上传的文件")
//    public ResponseEntity FailExamineStudentUploadFile(
//            @RequestBody StudentUploadFile studentUploadFile
//    ){
//        try{
//            Integer num = teacherService.failExamineStudentUploadFile(studentUploadFile);
//            if(num <= 0) return new ResponseEntity(0,"驳回失败",null);
//            return new ResponseEntity(1,"驳回成功",null);
//        }catch(Exception e){
//            return new ResponseEntity(0,"驳回失败",null);
//        }
//    }
//
//
//
//    /**
//     * 通过学生上传的文件
//     * @param studentUploadFile
//     * @return
//     */
//    @ResponseBody
//    @PostMapping("ExamineStudentUploadFile")
//    @ApiOperation("通过学生上传的文件")
//    public ResponseEntity examineStudentUploadFile(
//            @RequestBody StudentUploadFile studentUploadFile
//    ){
//        try{
//            Integer num = teacherService.examineStudentUploadFile(studentUploadFile);
//            if(num <= 0) return new ResponseEntity(0,"修改失败",null);
//
//            //获取某个学生通过审核的文件个数
//            Integer count = teacherService.getStudentExamineFileCount(studentUploadFile.getStudent().getUsername());
//
//            //修改 某个学生处于哪个阶段
//            Map m = new HashMap();
//            m.put("username",studentUploadFile.getStudent().getUsername());
//            m.put("count",count+1);
//            Integer flag = teacherService.updateStudentStage(m);
//            if(flag <= 0) return new ResponseEntity(0,"修改失败",null);
//
//
//
//            return new ResponseEntity(0,"修改成功",null);
//        }catch (Exception e){
//            return new ResponseEntity(0,"修改失败",null);
//        }
//    }


    /**
     * 获取当前老师所有学生的上传的文件信息
     * @return
     */
    @GetMapping("getExamineFiles")
    @ResponseBody
    @ApiOperation("获取当前老师所有学生的上传的文件信息")
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


    @GetMapping("/getStudentList")
    @ResponseBody
    @ApiOperation("获取指导老师的学生")
    public ResponseEntity getStudentList(HttpServletRequest request){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");


            List<StudentList> studentLists = teacherService.getStudentList(map.get("username").toString());
            return new ResponseEntity(1,"获取成功",studentLists);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    @GetMapping("/getTeacherSubjects")
    @ResponseBody
    @ApiOperation("获取老师的所有课题")
    public ResponseEntity getTeacherSubjects(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            String username = map.get("username").toString();
            List<Subjects> subjects = teacherService.getTeacherSubjects(username);
            return new ResponseEntity(1,"获取成功",subjects);

        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }


    @GetMapping("/downTeacherFile")
    @ResponseBody
    @ApiOperation("下载老师上传的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value ="文件的id",paramType = "query",required = true)

    })
    public ResponseEntity downTeacherFile(
            @RequestParam("id") Integer id,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            String url = teacherService.getPathUrlByFileId(id);
            Boolean b = teacherFileUtils.teacherDownload(url,response,request);
            if(b){
                return  new ResponseEntity(1,"下载成功",null);
            }else {
                return  new ResponseEntity(1,"下载失败",null);
            }
        }catch (Exception e){
            return  new ResponseEntity(1,"下载失败",null);
        }
    }


    @GetMapping("/getStudentFileInfo")
    @ResponseBody
    @ApiOperation("获取所有学生的文件信息")
    @ApiImplicitParam(name = "type",value = "文件类型",paramType = "query",required = true)
    public ResponseEntity getStudentFileInfo(
            @RequestParam("type") Integer type,
            HttpServletRequest request
    ){
        try{
            Map userInfoMap = (HashMap)request.getSession().getAttribute("userInfo");
            Map map = new HashMap();
            map.put("username",userInfoMap.get("username").toString());
            map.put("type",type);
            List<StudentUploadFile> studentUploadFiles = teacherService.getStudentFileInfo(map);
            return new ResponseEntity(1,"获取成功",studentUploadFiles);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    @GetMapping("/examineStudentFile")
    @ResponseBody
    @ApiOperation("审核学生上传的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "学生文件的id",paramType = "query",required = true),
            @ApiImplicitParam(name = "status",value = "学生文件的状态 1 通过 2驳回",paramType = "query",required = true),
    })
    public ResponseEntity examineStudentFile(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status
    ){
        try{
            if(id == null || status == null){
                return new ResponseEntity(0,"网络异常",null);
            }
            Map map = new HashMap();
            map.put("id",id);
            map.put("status",status);
            Integer num = teacherService.examineStudentFile(map);
            if(num <= 0){
                return new ResponseEntity(0,"网络异常",null);
            }
            if(status == 1){


                //修改当前学生的状态
                Map m = new HashMap();
                m.put("id",id);
                Integer n = teacherService.setStudentSubjectStatus(m);
            }




            return new ResponseEntity(1,"修改成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"网络异常",null);
        }
    }

    @GetMapping("/downStudentFile")
    @ResponseBody
    @ApiOperation("下载学生的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文件的id",required = true,paramType = "query"),
            @ApiImplicitParam(name = "fileName",value = "文件的fileName 最终下载后的名字",required = true,paramType = "query")
    })
    public ResponseEntity downStudentFile(
            @RequestParam("id") Integer id,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try{
            String url = teacherService.getStudentFilePathById(id);
            Boolean b = teacherFileUtils.teacherDownload(url,response,request);
            if(b){
                return new ResponseEntity(1,"下载成功",null);
            }else {
                return new ResponseEntity(0,"下载失败",null);
            }
        }catch (Exception e){
            return new ResponseEntity(0,"下载失败",null);
        }
    }


    @GetMapping("/getTeacherUploadFile")
    @ResponseBody
    @ApiOperation("获取老师上传的文件")
    @ApiImplicitParam(name = "type",value = "文件的类型",required = true,paramType = "query")
    public ResponseEntity getTeacherUploadFile(
            @RequestParam("type") Integer type,
            HttpServletRequest request

    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Map tempMap = new HashMap();
            tempMap.put("username",map.get("username"));
            tempMap.put("type",type);
            List<TeacherUploadFile> teacherUploadFiles = teacherService.getTeacherUploadFile(tempMap);
            return new ResponseEntity(1,"获取成功",teacherUploadFiles);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }




}
