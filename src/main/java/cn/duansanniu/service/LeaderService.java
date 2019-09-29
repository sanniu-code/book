package cn.duansanniu.service;

import cn.duansanniu.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 20:58 下午
 */
public interface LeaderService {

    public Integer preserveCommonFileInfo(Map map);

    public List<Teacher> getTeachersByDepartId(Integer id);

    public Integer importSubject(Map map);

    public Integer updateSubject(Map map);

    public Integer deleteSubject(Integer id);

    public Integer getDepartIdBySubjectId(Integer id);

    public Integer createTask(Map map);

    //新增老师
    public Integer addTeacher(DoTeacher doTeacher);

    public Integer isExistTeacher(DoTeacher doTeacher);

    public List<DoTeacher> getTeacherList(Integer departId);

    public Integer updateTeacherPass(DoTeacher doTeacher);

    public Integer deleteTeacher(DoTeacher doTeacher);

    public Integer getStudentClassId(Map map);

    public Integer isExistStudent(DoStudent doStudent);

    public Integer addStudent(DoStudent doStudent);

    public List<Student> getStudentList(Integer departId);

    public Integer updateStudentPass(DoStudent doStudent);

    public Integer deleteStudent(DoStudent doStudent);

    public Integer addStudent(Student student);

    public Integer addProfession(DoProfession doProfession);

    public Integer deleteProfession(DoProfession doProfession);



    public Integer updateProfession(DoProfession doProfession);

    public DoProfession isExsitProfession(DoProfession doProfession);

    public Integer addClassGrade(DoClassGrade doClassGrade);

    public Integer updateClassGrade(DoClassGrade doClassGrade);

    public List<ClassGrade> getAllClassGrade(Integer departId);

    public DoClassGrade isExsitClassGrade(DoClassGrade doClassGrade);

    public List<Subjects> getAllNoExamineFile(Integer taskId);

    public Integer examineTeacherFile(DoSubject doSubject);

    public Integer updateTeacher(DoTeacher doTeacher);

    public List<DoProfession> getAllProfession(DoProfession doProfession);

    public List<ClassGrade> getClassByProfession(Integer id);

    public Integer updateStudent(Student student);

    public Task isExistTask(Integer departId);

}
