package cn.duansanniu.service;

import cn.duansanniu.entity.*;
import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:25 下午
 */

public interface StudentService {
    public List<Subjects> getSubjects(Integer taskId);

    public Subjects getSubject(String username);

    public Integer selectSubject(Map map);

    public Integer changeSubjectFileUsername(Map map);

    //保存学生上传的文件路径
    public Integer storePath(Map map);

    public StudentUploadFile getStudentFileInfo(Map map);

    //获取开题任务书的路径
    public String getMissionBookPath(TeacherUploadFile teacherUploadFile);

    //获取某个学生的相关信息
    public Student getStudentInfo(String username);

    //获取学生上传表的文件的路径
    public String getStudentFilePath(Map map);

    //获取审核未通过的文件
    public List<StudentUploadFile> getFailExamineFile(String username);

    //获取当前学生所处的阶段
    public Integer getStudentStage(String username);

    public TeacherUploadFile getTeacherUploadFile(TeacherUploadFile teacherUploadFile);






}
