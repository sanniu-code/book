package cn.duansanniu.utils;

import cn.duansanniu.entity.Task;
import cn.duansanniu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
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
            tempMap.put("departId",departId);
            tempMap.put("time",new Date());
            //判断当前任务是否有效 并且返回有效的任务
            Task task = userService.isEffectiveTask(tempMap);
            if(task == null){
                return null;
            }
            String year = task.getYear();

            //获取文件名字
            //String name = file.getOriginalFilename();
            //后缀名
            //String extendFileName = name.substring(name.lastIndexOf("."));

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
            File finalFile = new File(yearFile.getPath()+File.separator+fileName);
            file.transferTo(finalFile);

            Map returnMap = new HashMap();
            returnMap.put("name",fileName);
            returnMap.put("url",finalFile.getPath());
            returnMap.put("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

            return returnMap;


        }catch(Exception e){
            return null;

        }
    }

    public Boolean TeacherDownload(
            String url,
            String fileName,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        try{
            if(url.length() <= 0) return false;

            File file = new File(url);
            //获取文件流
            FileInputStream fileInputStream = new FileInputStream(file);
            //获取后缀名
            String extendFileName = url.substring(url.lastIndexOf("."));

            //动态设置响应类型 根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName+extendFileName,"UTF-8"));
            //获取输出流对象
            ServletOutputStream os = response.getOutputStream();
            //下载文件使用 spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fileInputStream,os);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
