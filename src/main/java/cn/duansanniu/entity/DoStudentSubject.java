package cn.duansanniu.entity;

/**
 * @author duansanniu
 * @create 2019-09-26 17:07 下午
 */
public class DoStudentSubject {
    private Integer id;
    private String username;
    private Integer subjectId;
    private Integer status;

    public DoStudentSubject(Integer id, String username, Integer subjectId, Integer status) {
        this.id = id;
        this.username = username;
        this.subjectId = subjectId;
        this.status = status;
    }

    public DoStudentSubject() {
    }
}
