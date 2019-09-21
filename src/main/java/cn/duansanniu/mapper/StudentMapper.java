package cn.duansanniu.mapper;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import cn.duansanniu.entity.TeacherUploadFile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 14:26 下午
 */
@Repository
public interface StudentMapper {
    public List<Subjects> getSubjects();

    public Subjects getSubject(String username);

    public Integer selectSubject(Map map);

    public Integer isSelect(Map map);

    //判断某个学生的某个文件是否已经审核通过
    public Integer isExamine(Map map);

    //保存学生上传的文件路径
    public Integer storePath(Map map);
    //判断学生是否已经上传了某个文件
    public Integer isStore(Map map);
    //修改学生已经上传的某个文件的路径
    public Integer updatePath(Map map);
    //获取学生已经上传的某个文件的路径
    public String getStudentFilePath(Map map);

    //获取开题任务书文件的路径
    public String getMissionBookPath(Map map);

    //获取某个学生的相关信息
    public Student getStudentInfo(String username);

    public List<StudentUploadFile> getFailExamineFile(String username);

    public Integer getStudentStage(String username);

    public TeacherUploadFile getOwnMissionBook(TeacherUploadFile teacherUploadFile);

    public StudentUploadFile getStudentFileInfo(Map map);




}
