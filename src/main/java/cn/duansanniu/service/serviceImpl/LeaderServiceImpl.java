package cn.duansanniu.service.serviceImpl;

import cn.duansanniu.entity.*;
import cn.duansanniu.mapper.LeaderMapper;
import cn.duansanniu.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 20:58 下午
 */
@Service
public class LeaderServiceImpl implements LeaderService {

    @Autowired
    private LeaderMapper leaderMapper;

    @Override
    public Integer preserveCommonFileInfo(Map map) {

        //判断该文件是否已经存在
        Integer num = leaderMapper.isPreserve(map);
        if(num >= 1){
            //存在
            //修改该数据
            Integer flag = leaderMapper.updateCommonFileInfo(map);
            return flag;
        }else {
            return leaderMapper.preserveCommonFileInfo(map);
        }



    }

    @Override
    public List<Teacher> getTeachersByDepartId(Integer id) {

        return leaderMapper.getTeachersByDepartId(id);
    }

    @Override
    public Integer importSubject(Map map) {
        return leaderMapper.importSubject(map);
    }

    @Override
    public Integer updateSubject(Map map) {
        return leaderMapper.updateSubject(map);
    }

    @Override
    public Integer deleteSubject(Integer id) {
        return leaderMapper.deleteSubject(id);
    }

    @Override
    public Integer getDepartIdBySubjectId(Integer id) {
        return leaderMapper.getDepartIdBySubjectId(id);
    }

    @Override
    public Integer createTask(Map map) {
        //判断 这个系  当前是否存在 有效的任务
        Date time = new Date();
        map.put("time",time);
        Integer num = leaderMapper.isEffectiveTask(map);
        if(num <= 0)
            return leaderMapper.createTask(map);
        return 0;
    }

    @Override
    public Integer addTeacher(DoTeacher doTeacher) {
        return leaderMapper.addTeacher(doTeacher);
    }

    @Override
    public Integer isExistTeacher(DoTeacher doTeacher) {
        return leaderMapper.isExistTeacher(doTeacher);
    }

    @Override
    public List<DoTeacher> getTeacherList(Integer departId) {
        return leaderMapper.getTeacherList(departId);
    }

    @Override
    public Integer updateTeacherPass(DoTeacher doTeacher) {
        return leaderMapper.updateTeacherPass(doTeacher);
    }

    @Override
    public Integer deleteTeacher(DoTeacher doTeacher) {
        return leaderMapper.deleteTeacher(doTeacher);
    }

    @Override
    public Integer getStudentClassId(Map map) {
        return leaderMapper.getStudentClassId(map);
    }

    @Override
    public Integer isExistStudent(DoStudent doStudent) {
        return leaderMapper.isExistStudent(doStudent);
    }

    @Override
    public Integer addStudent(DoStudent doStudent) {
        return leaderMapper.addStudent(doStudent);
    }

    @Override
    public List<Student> getStudentList(Integer departId) {
        return leaderMapper.getStudentList(departId);
    }

    @Override
    public Integer updateStudentPass(DoStudent doStudent) {
        return leaderMapper.updateStudentPass(doStudent);
    }

    @Override
    public Integer deleteStudent(DoStudent doStudent) {
        return leaderMapper.deleteStudent(doStudent);
    }


    @Override
    public Integer addStudent(Student student) {
        //组合为DoStudent
        Integer classId = student.getClassGrade().getId();
        //判断当前用户是否已经存在
        DoStudent doStudent = new DoStudent();
        doStudent.setClassid(classId);
        doStudent.setStatus(1);
        doStudent.setYear(student.getYear());
        doStudent.setCreateTime(student.getCreateTime());
        doStudent.setName(student.getName());
        doStudent.setSex(student.getSex());
        doStudent.setUsername(student.getUsername());
        doStudent.setPassword("1111111");



        Integer flag = leaderMapper.isExistStudent(doStudent);
        if(flag > 0){
            return 0;
        }
        //说明不存在这个用户
        Integer num = leaderMapper.addStudent(doStudent);
        if(num > 0){
            return 1;
        }
        return 0;
    }

    @Override
    public Integer addProfession(DoProfession doProfession) {
        return leaderMapper.addProfession(doProfession);
    }

    @Override
    public Integer deleteProfession(DoProfession doProfession) {
        return leaderMapper.deleteProfession(doProfession);
    }

    @Override
    public Integer updateProfession(DoProfession doProfession) {
        return leaderMapper.updateProfession(doProfession);
    }

    @Override
    public DoProfession isExsitProfession(DoProfession doProfession) {
        return leaderMapper.isExsitProfession(doProfession);
    }

    @Override
    public Integer addClassGrade(DoClassGrade doClassGrade) {
        return leaderMapper.addClassGrade(doClassGrade);
    }

    @Override
    public Integer updateClassGrade(DoClassGrade doClassGrade) {
        return leaderMapper.updateClassGrade(doClassGrade);
    }

    @Override
    public List<ClassGrade> getAllClassGrade(Integer departId) {
        return leaderMapper.getAllClassGrade(departId);
    }

    @Override
    public DoClassGrade isExsitClassGrade(DoClassGrade doClassGrade) {
        return leaderMapper.isExsitClassGrade(doClassGrade);
    }

    @Override
    public List<Subjects> getAllNoExamineFile(Integer taskId) {
        return leaderMapper.getAllNoExamineFile(taskId);
    }

    @Override
    public Integer examineTeacherFile(DoSubject doSubject) {
        return leaderMapper.examineTeacherFile(doSubject);
    }


    @Override
    public Integer updateTeacher(DoTeacher doTeacher) {
        return leaderMapper.updateTeacher(doTeacher);
    }

    @Override
    public List<DoProfession> getAllProfession(DoProfession doProfession) {
        return leaderMapper.getAllProfession(doProfession);
    }

    @Override
    public List<ClassGrade> getClassByProfession(Integer id) {
        return leaderMapper.getClassByProfession(id);
    }

    @Override
    public Integer updateStudent(Student student) {
        try{
            //组合为DoStudent
            Integer classId = student.getClassGrade().getId();
            //判断当前用户是否已经存在
            DoStudent doStudent = new DoStudent();
            doStudent.setClassid(classId);
            doStudent.setStatus(1);
            doStudent.setYear(student.getYear());



            doStudent.setCreateTime(student.getCreateTime());
            doStudent.setName(student.getName());
            doStudent.setSex(student.getSex());
            doStudent.setUsername(student.getUsername());

            System.out.println(doStudent);
            return leaderMapper.updateStudent(doStudent);
        }catch (Exception e){
            return  0;
        }




    }


    @Override
    public Task isExistTask(Integer departId) {
        Map map = new HashMap();
        map.put("departId",departId);
        map.put("time",new Date());
        return leaderMapper.isExistTask(map);
    }
}
