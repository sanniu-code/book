package cn.duansanniu.mapper;

import cn.duansanniu.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 15:47 下午
 */
@Repository
public interface TeacherMapper {

    public Integer isGuideTeacher(Map map);

    public Integer storeReport(Map map);

    public List<StudentUploadFile> getNotExamineFiles(String username);

    public Integer examineStudentUploadFile(StudentUploadFile studentUploadFile);

    public List<StudentUploadFile> getExamineFiles(String username);

    public Integer failExamineStudentUploadFile(StudentUploadFile studentUploadFile);

    public Integer getStudentExamineFileCount(String username);

    public Integer updateStudentStage(Map map);

    public Integer isExitFile(Map map);

    public Integer storeFile(Map map);

    public Integer updateFile(Map map);

    public Integer uploadApplyTable(ApplyTable applyTable);

    public List<StudentList> getStudentList(String username);

    public Integer getFileId(Map map);

    public List<Subjects> getTeacherSubjects(String username);

//    这个是老师的文件url
    public String getPathUrlByFileId(Integer id);

    public List<StudentUploadFile> getStudentFileInfo(Map map);

    public Integer examineStudentFile(Map map);

    public String getStudentFilePathById(Integer id);

    public List<TeacherUploadFile> getTeacherUploadFile(Map map);

    public Integer setStudentSubjectStatus(Map map);

    public Integer updateSubjectFile(Map map);


}
