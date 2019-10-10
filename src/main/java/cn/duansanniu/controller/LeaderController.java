package cn.duansanniu.controller;

import cn.duansanniu.entity.*;
import cn.duansanniu.service.LeaderService;
import cn.duansanniu.service.UserService;
import cn.duansanniu.utils.POIUtils;
import cn.duansanniu.utils.ResponseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private UserService userService;


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

//    /*
//    *   获取本系的所有老师信息
//    * */
//    @GetMapping("getTeachersByDepartId")
//    @ResponseBody
//    @ApiOperation("获取本系的所有老师")
//    @ApiImplicitParam(name = "departId",value = "系的id",required = true,paramType = "query")
//    public ResponseEntity getTeachersByDepartId(
//            @RequestParam("departId") Integer departId
//    ){
//        try{
//            List<Teacher> teachers =  leaderService.getTeachersByDepartId(departId);
//            return new ResponseEntity(1,"获取成功",teachers);
//
//        }catch(Exception e){
//            return new ResponseEntity(0,"获取失败",null);
//        }
//    }

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

//    @GetMapping("deleteSubject")
//    @ResponseBody
//    @ApiOperation("删除课题")
//    @ApiImplicitParam(name = "id",value = "课题的id",required = true,paramType = "query")
//    public ResponseEntity deleteSubject(
//            @RequestParam("id") Integer id,
//            HttpServletRequest request
//    ){
//        try{
//            //判断leader是否有权限删除这个 主题
//                //获取这个题目所属的系别
//            Integer departId = leaderService.getDepartIdBySubjectId(id);
//
//            Integer leaderDepartId = (Integer)request.getSession().getAttribute("departId");
//            if(departId != leaderDepartId) return new ResponseEntity(0,"删除失败",null);
//
//            Integer flag = leaderService.deleteSubject(id);
//            if(flag <= 0) return new ResponseEntity(0,"删除失败",null);
//            return new ResponseEntity(1,"删除成功",null);
//        }catch(Exception e){
//            return new ResponseEntity(0,"删除失败",null);
//        }
//    }

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

    @GetMapping("/isExistTask")
    @ResponseBody
    @ApiOperation("当前系中是否存在有效的任务")
    public ResponseEntity isExistTask(
            @RequestParam("departId") Integer departId
    ){
        try{
            Task task = leaderService.isExistTask(departId);
            if(task != null){
                return new ResponseEntity(1,"存在",task);
            }
            return new ResponseEntity(2,"不存在",null);

        }catch (Exception e){
            return new ResponseEntity(0,"网络异常",null);
        }
    }




    @PostMapping("/getDataByTeacherExcel")
    @ResponseBody
    @ApiOperation("通过excel中获取老师的数据")
    public ResponseEntity getDataByTeacherExcel(
            MultipartFile file,
            HttpServletRequest request
    ){
        List<DoTeacher> list = new ArrayList<>();
        try{
            List<String[]> lists = POIUtils.readExcel(file);
            for(int i = 0;i<lists.size();i++){
                String[] content = lists.get(i);
                DoTeacher doTeacher = new DoTeacher();
                //用户名
                doTeacher.setUsername(content[0]);
                //密码
                String pass = "";
                if(content[1].length() == 0)
                    pass = "111111";
                pass = content[1];

                doTeacher.setPassword(pass);
                //姓名
                doTeacher.setName(content[2]);
                //性别
                doTeacher.setSex(Integer.valueOf(content[3]));
                //职称
                doTeacher.setProfessionRank(content[4]);
                //学位
                doTeacher.setDegree(content[5]);
                //系别
                Map map = (HashMap)request.getSession().getAttribute("userInfo");
                Integer departId = (Integer)map.get("departId");
                doTeacher.setDepartId(departId);
                //状态
                doTeacher.setStatus(1);

                //判断当前用户是否已经存在
                Integer flag = leaderService.isExistTeacher(doTeacher);
                if(flag <= 0){
                    //新增
                    Integer num = leaderService.addTeacher(doTeacher);
                }




            }
            return new ResponseEntity(1,"导入数据成功",null);

        }catch (Exception e){
            return new ResponseEntity(0,"导入数据失败",null);
        }
    }

    @GetMapping("/getTeacherList")
    @ResponseBody
    @ApiOperation("获取本系老师的数据")
    public ResponseEntity getTeacherList(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNum") Integer pageNum,
            HttpServletRequest request
    ){
        try{
            if(pageSize == 0 || pageSize == null){
                pageSize = 1;
            }
            if(pageNum == 0 || pageNum == null){
                pageNum = 10;
            }

            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");


            PageHelper.startPage(pageNum,pageSize);
            List<DoTeacher> doTeachers = leaderService.getTeacherList(departId);
            PageInfo pageInfo = new PageInfo(doTeachers);
            return new ResponseEntity(1,"获取成功",pageInfo);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }





    @PostMapping("/updateTeacherPass")
    @ResponseBody
    @ApiOperation("修改老师的密码")
    public ResponseEntity updateTeacherPass(
            @RequestBody DoTeacher doTeacher
    ){
        try{
            if(doTeacher.getUsername() == null || doTeacher.getPassword() == null){
                return new ResponseEntity(0,"修改密码错误",null);
            }

            Integer num = leaderService.updateTeacherPass(doTeacher);
            if(num <= 0){
                return new ResponseEntity(0,"密码修改错误",null);


            }
            return new ResponseEntity(1,"密码修改成功",null);


        }catch (Exception e){
            return new ResponseEntity(0,"密码修改错误",null);
        }
    }

//    @PostMapping("/deleteTeacher")
//    @ResponseBody
//    @ApiOperation("删除老师")
//    public ResponseEntity deleteTeacher(@RequestBody  DoTeacher doTeacher){
//
//        try{
//            Integer num = leaderService.deleteTeacher(doTeacher);
//            if(num <= 0){
//                return  new ResponseEntity(0,"删除失败",null);
//            }
//            return new ResponseEntity(1,"删除成功",null);
//
//        }catch (Exception e){
//            return  new ResponseEntity(0,"删除失败",null);
//        }
//    }




    @PostMapping("/addTeacher")
    @ResponseBody
    @ApiOperation("添加老师信息")
    public ResponseEntity addTeacher(
            @RequestBody DoTeacher doTeacher,
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");

            doTeacher.setDepartId(departId);
            doTeacher.setStatus(1);
            Integer num = leaderService.addTeacher(doTeacher);
            if(num <= 0){
                return  new ResponseEntity(0,"添加失败",null);
            }
            return new ResponseEntity(1,"添加成功",null);
        }catch (Exception e){
            return  new ResponseEntity(0,"添加失败",null);
        }
    }

    @PostMapping("/updateTeacher")
    @ResponseBody
    @ApiOperation("修改老师的信息")
    public ResponseEntity updateTeacher(
            @RequestBody DoTeacher doTeacher
    ){
        try {
            Integer num  = leaderService.updateTeacher(doTeacher);
            if(num <= 0){
                return new ResponseEntity(0,"修改失败",null);
            }
            return new ResponseEntity(1,"修改成功",null);
        }catch(Exception e){
            return new ResponseEntity(0,"修改失败",null);
        }
    }





    @PostMapping("/getDataByStudentExcel")
    @ResponseBody
    @ApiOperation("通过excel中获取学生的数据")
    public ResponseEntity getDataByStudentExcel(
            MultipartFile file,
            HttpServletRequest request
    ){
        List<DoTeacher> list = new ArrayList<>();
        try{
            List<String[]> lists = POIUtils.readExcel(file);
            for(int i = 0;i<lists.size();i++){
                String[] content = lists.get(i);
                DoStudent doStudent = new DoStudent();
                //用户名
                doStudent.setUsername(content[0]);
                //密码
                String pass = "";
                if(content[1].length() == 0)
                    pass = "111";
                pass = content[1];

                doStudent.setPassword(pass);
                //姓名
                doStudent.setName(content[2]);
                //性别
                doStudent.setSex(Integer.valueOf(content[3]));

                //班级id
                String profession= content[4];
                String className = content[5];
                Map classMap = new HashMap();
                classMap.put("profession",profession);
                classMap.put("className",className);
                Integer classId = leaderService.getStudentClassId(classMap);
                if(classId <= 0){
                    continue;
                }
                doStudent.setClassid(classId);
                //入学时间

                Date createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(content[6].toString());
                doStudent.setCreateTime(createTime);

                //学习周期
                doStudent.setYear(Integer.valueOf(content[7]));

                doStudent.setStatus(1);




                //判断当前用户是否已经存在
                Integer flag = leaderService.isExistStudent(doStudent);
                if(flag <= 0){
                    //新增
                    Integer num = leaderService.addStudent(doStudent);
                }




            }
            return new ResponseEntity(1,"导入数据成功",null);

        }catch (Exception e){
            return new ResponseEntity(0,"空表",null);
        }
    }





    @GetMapping("/getStudentList")
    @ResponseBody
    @ApiOperation("获取本系学生的数据")
    public ResponseEntity getStudentList(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNum") Integer pageNum,
            HttpServletRequest request
    ){
        try{
            if(pageSize == 0 || pageSize == null){
                pageSize = 1;
            }
            if(pageNum == 0 || pageNum == null){
                pageNum = 10;
            }

            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");


            PageHelper.startPage(pageNum,pageSize);
            List<Student> doTeachers = leaderService.getStudentList(departId);
            PageInfo pageInfo = new PageInfo(doTeachers);
            return new ResponseEntity(1,"获取成功",pageInfo);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }





    @PostMapping("/updateStudentPass")
    @ResponseBody
    @ApiOperation("修改学生的密码")
    public ResponseEntity updateStudentPass(
            @RequestBody DoStudent doStudent
    ){
        try{
            if(doStudent.getUsername() == null || doStudent.getPassword() == null){
                return new ResponseEntity(0,"修改密码错误",null);
            }

            Integer num = leaderService.updateStudentPass(doStudent);
            if(num <= 0){
                return new ResponseEntity(0,"密码修改错误",null);


            }
            return new ResponseEntity(1,"密码修改成功",null);


        }catch (Exception e){
            return new ResponseEntity(0,"密码修改错误",null);
        }
    }

//    @PostMapping("/deleteStudent")
//    @ResponseBody
//    @ApiOperation("删除学生")
//    public ResponseEntity deleteTeacher(@RequestBody  DoStudent doStudent){
//
//        try{
//            Integer num = leaderService.deleteStudent(doStudent);
//            if(num <= 0){
//                return  new ResponseEntity(0,"删除失败",null);
//            }
//            return new ResponseEntity(1,"删除成功",null);
//
//        }catch (Exception e){
//            return  new ResponseEntity(0,"删除失败",null);
//        }
//    }



    @PostMapping("/addStudent")
    @ResponseBody
    @ApiOperation("添加学生信息")
    public ResponseEntity addStudent(
            @RequestBody Student student,
            HttpServletRequest request
    ){
        try{
            Integer num = leaderService.addStudent(student);

            if(num <= 0){
                return  new ResponseEntity(0,"添加失败",null);
            }
            return new ResponseEntity(1,"添加成功",null);
        }catch (Exception e){
            return  new ResponseEntity(0,"添加失败",null);
        }
    }

    @PostMapping("/updateStudent")
    @ResponseBody
    @ApiOperation("修改学生信息")
    public ResponseEntity updateStudent(
            @RequestBody Student student,
            HttpServletRequest request
    ){
        try{
            Integer num = leaderService.updateStudent(student);

            if(num <= 0){
                return  new ResponseEntity(0,"更新失败",null);
            }
            return new ResponseEntity(1,"更新成功",null);
        }catch (Exception e){
            return  new ResponseEntity(0,"更新失败",null);
        }
    }

//    专业的增删改查





    @PostMapping("/addProfession")
    @ResponseBody
    @ApiOperation("增加专业")
    public ResponseEntity addProfession(
            @RequestBody DoProfession doProfession,
            HttpServletRequest request
    ){
        try{

            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            doProfession.setDepartId(departId);
            //先判断这个专业是否存在
            DoProfession doProfession1 = leaderService.isExsitProfession(doProfession);
            if(doProfession1 != null){
                return new ResponseEntity(0,"该专业已存在",null);
            }

            //新增
            Integer num = leaderService.addProfession(doProfession);
            if(num <= 0){
                return new ResponseEntity(0,"添加失败",null);
            }
            return new ResponseEntity(1,"添加成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"添加失败",null);
        }
    }

//    @PostMapping("/deleteProfession")
//    @ResponseBody
//    @ApiOperation("删除专业")
//    public ResponseEntity deleteProfession(
//            @RequestBody DoProfession doProfession,
//            HttpServletRequest request
//    ){
//        try{
//
//            Integer num = leaderService.deleteProfession(doProfession);
//            if(num <= 0){
//                return new ResponseEntity(0,"删除失败",null);
//            }
//            return new ResponseEntity(1,"删除成功",null);
//        }catch (Exception e){
//            return new ResponseEntity(0,"删除失败",null);
//        }
//    }





    @PostMapping("/updateProfession")
    @ResponseBody
    @ApiOperation("修改专业")
    public ResponseEntity updateProfession(
            @RequestBody DoProfession doProfession,
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            doProfession.setDepartId(departId);
            //先判断这个专业是否存在
            DoProfession doProfession1 = leaderService.isExsitProfession(doProfession);
            if(doProfession1 != null){
                return new ResponseEntity(0,"该专业已存在",null);
            }

            Integer num = leaderService.updateProfession(doProfession);
            if(num <= 0){
                return new ResponseEntity(0,"修改失败",null);
            }
            return new ResponseEntity(1,"修改成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"修改失败",null);
        }
    }



    @PostMapping("/getAllProfession")
    @ResponseBody
    @ApiOperation("查询所有专业")
    public ResponseEntity getAllProfession(
            HttpServletRequest request
    ){
        try{
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            DoProfession doProfession = new DoProfession();
            doProfession.setDepartId((Integer) map.get("departId"));


            List<DoProfession>  doProfessions = leaderService.getAllProfession(doProfession);

            return new ResponseEntity(1,"获取成功",doProfessions);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }

    @GetMapping("/getClassByProfession")
    @ResponseBody
    @ApiOperation("查询某专业的班级")
    @ApiImplicitParam(name = "id",value = "专业的id",required = true,paramType = "query")
    public ResponseEntity getClassByProfession(
            @RequestParam("id") Integer id,
            HttpServletRequest request
    ){
        try{


            List<ClassGrade> classGrades  = leaderService.getClassByProfession(id);

            return new ResponseEntity(1,"获取成功",classGrades);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }


//    班级的增删改查

//    @PostMapping("/addClassGrade")
//    @ResponseBody
//    @ApiOperation("新增班级")
//    public ResponseEntity deleteClassGrade(
//            @RequestBody DoClassGrade doClassGrade,
//            HttpServletRequest request
//    ){
//        try{
//
//
//            Integer num = leaderService.addClassGrade(doClassGrade);
//
//            if(num <= 0){
//                return new ResponseEntity(0,"新增失败",null);
//            }
//            return new ResponseEntity(1,"新增成功",null);
//        }catch (Exception e){
//            return new ResponseEntity(0,"新增失败",null);
//        }
//    }



    @PostMapping("/addClassGrade")
    @ResponseBody
    @ApiOperation("新增班级")
    public ResponseEntity addClassGrade(
            @RequestBody ClassGrade classGrade,
            HttpServletRequest request
    ){
        try{
            //获取专业的id
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            //封装为DoProfession
            DoProfession doProfession = new DoProfession();
            doProfession.setDepartId(departId);
            doProfession.setName(classGrade.getProfession().getName());
            DoProfession resultDoProfession = leaderService.isExsitProfession(doProfession);
            if(resultDoProfession == null){
                return new ResponseEntity(0,"不存在该专业",null);
            }

            DoClassGrade doClassGrade = new DoClassGrade();
            doClassGrade.setProfessionid(resultDoProfession.getId());
            doClassGrade.setName(classGrade.getName());

            //判断是否存在该班级
            DoClassGrade resultDoClassGrade = leaderService.isExsitClassGrade(doClassGrade);
            if(resultDoClassGrade!=null){
                return new ResponseEntity(0,"该班级已存在",null);
            }

            Integer num = leaderService.addClassGrade(doClassGrade);

            if(num <= 0){
                return new ResponseEntity(0,"新增失败",null);
            }
            return new ResponseEntity(1,"新增成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"新增失败",null);
        }
    }



    @PostMapping("/updateClassGrade")
    @ResponseBody
    @ApiOperation("修改班级")
    public ResponseEntity updateClassGrade(
            @RequestBody ClassGrade classGrade,
            HttpServletRequest request
    ){
        try{
            //获取专业的id
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            //封装为DoProfession
            DoProfession doProfession = new DoProfession();
            doProfession.setDepartId(departId);
            doProfession.setName(classGrade.getProfession().getName());
            DoProfession resultDoProfession = leaderService.isExsitProfession(doProfession);
            if(resultDoProfession == null){
                return new ResponseEntity(0,"不存在该专业",null);
            }



            DoClassGrade doClassGrade = new DoClassGrade();
            doClassGrade.setProfessionid(classGrade.getProfession().getId());
            doClassGrade.setId(classGrade.getId());
            doClassGrade.setName(classGrade.getName());
            //判断是否存在该班级
            DoClassGrade doClassGrade1 = leaderService.isExsitClassGrade(doClassGrade);
            if(doClassGrade1 !=null){
                return new ResponseEntity(0,"该班级已经存在",null);
            }


            Integer num = leaderService.updateClassGrade(doClassGrade);

            if(num <= 0){
                return new ResponseEntity(0,"修改失败",null);
            }
            return new ResponseEntity(1,"修改成功",null);
        }catch (Exception e){
            return new ResponseEntity(0,"修改失败",null);
        }
    }




    @GetMapping("/getAllClassGrade")
    @ResponseBody
    @ApiOperation("获取所有的班级")
    public ResponseEntity getAllClassGrade(
            HttpServletRequest request,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNum") Integer pageNum
    ){
        try{

            if(pageSize == null || pageSize == 0){
                pageSize = 1;
            }
            if(pageNum == null || pageNum == 0){
                pageNum = 10;
            }


            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            PageHelper.startPage(pageNum,pageSize);
            List<ClassGrade> classGrades = leaderService.getAllClassGrade(departId);
            PageInfo pageInfo = new PageInfo(classGrades);

            return new ResponseEntity(1,"获取成功",pageInfo);


        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }




    @ResponseBody
    @GetMapping("/getAllNoExamineFile/{pageSize}/{pageNum}")
    @ApiOperation("获取未审核的文件信息")
    public ResponseEntity getAllNoExamineFile(
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("pageNum") Integer pageNum,
            HttpServletRequest request
    ){
        try{
                if(pageSize == null || pageSize == 0){
                pageSize = 10;
            }
            if(pageNum == null || pageNum == 0){
                pageNum = 1;
            }
            //获取当前任务
            Map map = (HashMap)request.getSession().getAttribute("userInfo");
            Integer departId = (Integer)map.get("departId");
            //获取当前系的task
            Map tempMap = new HashMap();
            tempMap.put("departId",departId);
            tempMap.put("time",new Date());
            Task task = userService.isEffectiveTask(tempMap);
            if(task == null){
                return new ResponseEntity(0,"网络异常",null);
            }

            PageHelper.startPage(pageNum,pageSize);
            List<Subjects> subjects = leaderService.getAllNoExamineFile(task.getId());
            PageInfo pageInfo = new PageInfo(subjects);
            return new ResponseEntity(1,"获取成功",pageInfo);
        }catch (Exception e){
            return new ResponseEntity(0,"获取失败",null);
        }
    }



    @PostMapping("/examineTeacherFile")
    @ResponseBody
    @ApiOperation("审核老师的文件")
    public ResponseEntity examineTeacherFile(@RequestBody DoSubject doSubject){
        try{
            Integer num = leaderService.examineTeacherFile(doSubject);
            if(num <= 0){
                return new ResponseEntity(0,"审核失败",null);
            }
            return new ResponseEntity(1,"审核成功",null);

        }catch (Exception e){
            return new ResponseEntity(0,"审核失败",null);
        }
    }













}
