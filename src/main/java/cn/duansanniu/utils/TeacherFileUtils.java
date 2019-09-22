package cn.duansanniu.utils;

import cn.duansanniu.entity.Task;
import cn.duansanniu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-22 16:22 下午
 */
@Component
public class TeacherFileUtils {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private UserService userService;

    public Map teacherUpload(MultipartFile file, String fileName, Map map){
        try{

            Integer departId = userService.getTeacherDepartIdByUsername(map.get("username").toString());

            Map tempMap = new HashMap();
            map.put("departId",departId);
            map.put("time",new Date());
            Task task = userService.isEffectiveTask(map);
            if(task == null){
                return null;
            }
            String year = task.getYear();

            //获取文件名字
            String name = file.getOriginalFilename();
            //后缀名
            String extendFileName = name.substring(name.lastIndexOf("."));

            File root = new File(ResourceUtils.getFile("classpath:").getPath());
            if(!root.exists())
                root = new File("");
            //获取studentFile文件
            File studentFile = new File(root.getAbsolutePath(),"static/"+year+File.separator);
            if(!studentFile.exists())
                studentFile.mkdirs();

            //获取学生入学年份
//            String year = dateUtils.getYear(map.get("createTime").toString());

            String url = File.separator+map.get("departName")+File.separator+"teacherFile"+File.separator+map.get("username")+File.separator;

            //生成年文件夹
            File yearFile = new File(studentFile.getAbsolutePath(),url);
            if(!yearFile.exists())
                yearFile.mkdirs();

            //文件的上传
            File finalFile = new File(yearFile.getPath()+File.separator+fileName+extendFileName);
            file.transferTo(finalFile);

            Map returnMap = new HashMap();
            returnMap.put("name",fileName+extendFileName);
            returnMap.put("url",finalFile.getPath());
            returnMap.put("username",map.get("username").toString());
            returnMap.put("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            return returnMap;


        }catch(Exception e){
            return null;

        }
    }
}
