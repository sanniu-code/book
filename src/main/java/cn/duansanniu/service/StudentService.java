package cn.duansanniu.service;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:25 下午
 */

public interface StudentService {
    public List<Subjects> getSubjects();

    public Subjects getSubject(Student student);

    public Integer selectSubject(Map map);

    //保存学生上传的文件路径
    public Integer storePath(Map map);

    //获取开题任务书的路径
    public String getMissionBookPath(Map map);

    //获取某个学生的相关信息
    public Student getStudentInfo(String username);

    //获取学生上传表的文件的路径
    public String getStudentFilePath(Map map);

    public List<StudentUploadFile> getFailExamineFile(String username);

}
