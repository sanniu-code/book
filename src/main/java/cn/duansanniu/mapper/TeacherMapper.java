package cn.duansanniu.mapper;

import cn.duansanniu.entity.StudentUploadFile;
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

}
