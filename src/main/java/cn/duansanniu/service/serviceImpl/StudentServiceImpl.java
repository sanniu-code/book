package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.Student;
import cn.duansanniu.entity.StudentUploadFile;
import cn.duansanniu.entity.Subjects;
import cn.duansanniu.mapper.StudentMapper;
import cn.duansanniu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 15:00 下午
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Subjects> getSubjects() {
        List<Subjects> subjects = studentMapper.getSubjects();
        return subjects;
    }

    @Override
    public Subjects getSubject(Student student) {
        Subjects subject = studentMapper.getSubject(student);
        return subject;
    }

    @Override
    public Integer selectSubject(Map map) {
        //先判断当前用户是否已经选题成功了
        Integer isSelect = studentMapper.isSelect(map);
        if(isSelect == 1) return 0;
        Integer num =  studentMapper.selectSubject(map);
        return num;
    }

    @Override
    public Integer storePath(Map map) {

        //判断 是否已经存在
        Integer num = studentMapper.isStore(map);
        Integer flag = 0;
        if(num >= 1){
            //更新
            flag = studentMapper.updatePath(map);
        }else {
            //保存
            flag = studentMapper.storePath(map);

        }

        return flag;
    }

    /**
     * 用于获取开题报告的文件路径
     * @param map
     * @return
     */
    @Override
    public String getMissionBookPath(Map map) {
        //String url = studentMapper.getPath(map);
        return studentMapper.getMissionBookPath(map);
    }

    @Override
    public Student getStudentInfo(String username) {

        return studentMapper.getStudentInfo(username);
    }

    @Override
    public String getStudentFilePath(Map map) {

        return studentMapper.getStudentFilePath(map);
    }

    @Override
    public List<StudentUploadFile> getFailExamineFile(String username) {
        return studentMapper.getFailExamineFile(username);
    }
}
