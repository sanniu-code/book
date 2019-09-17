package cn.duansanniu.utils;

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
 * @create 2019-09-17 10:48 上午
 */
@Component
public class StudentFileUtils {

    @Autowired
    private DateUtils dateUtils;

    /**
     *
     * @param file 上传的文件
     * @param fileName 最终显示的文件名
     * @param map 当前用户的信息
     * @return
     */
    public Map studentUpload(MultipartFile file, String fileName, Map map){
        try{
            //获取文件名字
            String name = file.getOriginalFilename();
            //后缀名
            String extendFileName = name.substring(name.lastIndexOf("."));

            File root = new File(ResourceUtils.getFile("classpath:").getPath());
            if(!root.exists())
                root = new File("");
            //获取studentFile文件
            File studentFile = new File(root.getAbsolutePath(),"static/studentFile/");
            if(!studentFile.exists())
                studentFile.mkdirs();

            //获取学生入学年份
            String year = dateUtils.getYear(map.get("createTime").toString());

            String url = year+File.separator+map.get("departName")+File.separator+map.get("professionName")+File.separator+map.get("className")+File.separator+map.get("username")+File.separator;

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

    public Boolean studentDownload(
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
            String extendFileName = fileName.substring(fileName.lastIndexOf("."));

            //动态设置响应类型 根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
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
