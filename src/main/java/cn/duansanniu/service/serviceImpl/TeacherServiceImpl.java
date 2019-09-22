package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.StudentUploadFile;
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
        if(teacherMapper.isExitFile(map) <= 0){//不存在
            return teacherMapper.storeFile(map);
        }else {
            return teacherMapper.updateFile(map);
        }

    }
}
