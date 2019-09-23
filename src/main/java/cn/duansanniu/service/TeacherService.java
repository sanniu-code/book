package cn.duansanniu.service;

import cn.duansanniu.entity.ApplyTable;
import cn.duansanniu.entity.StudentList;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-16 15:47 下午
 */
public interface TeacherService {
    public Integer isGuideTeacher(Map map);

    public Integer storeReport(Map map);

    public List<StudentUploadFile> getNotExamineFiles(String username);

    public Integer examineStudentUploadFile(StudentUploadFile studentUploadFile);

    public List<StudentUploadFile> getExamineFiles(String username);

    public Integer failExamineStudentUploadFile(StudentUploadFile studentUploadFile);

    public Integer getStudentExamineFileCount(String username);

    public Integer updateStudentStage(Map map);

    public Integer storePath(Map map);

    //
    public Integer uploadApplyTable(ApplyTable applyTable);

    public List<StudentList> getStudentList(String username);

    public List<Subjects> getTeacherSubjects(String username);

    public String getPathUrlByFileId(Integer id);

    public List<StudentUploadFile> getStudentFileInfo(Map map);

    public Integer examineStudentFile(Map map);
}
