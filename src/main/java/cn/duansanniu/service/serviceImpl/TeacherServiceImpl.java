package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.*;
import cn.duansanniu.mapper.TeacherMapper;
import cn.duansanniu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 15:47 下午
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;


    @Override
    public Integer isGuideTeacher(Map map) {

        return teacherMapper.isGuideTeacher(map);
    }

    @Override
    public Integer storeReport(Map map) {
        return teacherMapper.storeReport(map);
    }

    @Override
    public List<StudentUploadFile> getNotExamineFiles(String username) {
        return teacherMapper.getNotExamineFiles(username);
    }

    @Override
    public Integer examineStudentUploadFile(StudentUploadFile studentUploadFile) {
        return teacherMapper.examineStudentUploadFile(studentUploadFile);
    }

    @Override
    public List<StudentUploadFile> getExamineFiles(String username) {
        return teacherMapper.getExamineFiles(username);
    }

    @Override
    public Integer failExamineStudentUploadFile(StudentUploadFile studentUploadFile) {
        return teacherMapper.failExamineStudentUploadFile(studentUploadFile);
    }

    @Override
    public Integer getStudentExamineFileCount(String username) {
        return teacherMapper.getStudentExamineFileCount(username);
    }

    @Override
    public Integer updateStudentStage(Map map) {
        return teacherMapper.updateStudentStage(map);
    }

    @Override
    public Integer storePath(Map map) {
        //判断是否存在
            //如果存在 就修改
            //如果不存在 就新增
        Integer num = teacherMapper.storeFile(map);
        if(num <= 0){

            return 0;
        }
        System.out.println(map);
        Integer id = Integer.valueOf(map.get("id").toString());
        return id;
//        if(teacherMapper.isExitFile(map) <= 0){//不存在
//            Integer num = teacherMapper.storeFile(map);
//            if(num <= 0){
//                return 0;
//            }
//            return num;
//        }else { //存在
//            return 0;
            //Integer num = teacherMapper.updateFile(map);
//            if(num <= 0){
//                return 0;
//            }
//            //获取这条数据的id
//            Integer id = teacherMapper.getFileId(map);
//            return id;
        //}

    }


    @Override
    public Integer uploadApplyTable(ApplyTable applyTable) {
        return teacherMapper.uploadApplyTable(applyTable);
    }

    @Override
    public List<StudentList> getStudentList(String username) {
        return teacherMapper.getStudentList(username);
    }

    @Override
    public List<Subjects> getTeacherSubjects(String username) {
        return teacherMapper.getTeacherSubjects(username);
    }

    @Override
    public String getPathUrlByFileId(Integer id) {
        return teacherMapper.getPathUrlByFileId(id);
    }

    @Override
    public List<StudentUploadFile> getStudentFileInfo(Map map) {
        return teacherMapper.getStudentFileInfo(map);
    }

    @Override
    public Integer examineStudentFile(Map map) {
        return teacherMapper.examineStudentFile(map);
    }

    @Override
    public String getStudentFilePathById(Integer id) {
        return teacherMapper.getStudentFilePathById(id);
    }

    @Override
    public List<TeacherUploadFile> getTeacherUploadFile(Map map) {
        return teacherMapper.getTeacherUploadFile(map);
    }

    @Override
    public Integer setStudentSubjectStatus(Map map) {
        return teacherMapper.setStudentSubjectStatus(map);
    }

    @Override
    public Integer updateSubjectFile(Map map) {
        return teacherMapper.updateSubjectFile(map);
    }
}
