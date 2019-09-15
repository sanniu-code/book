package cn.duansanniu.controller;

import cn.duansanniu.service.LeaderService;
import cn.duansanniu.utils.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author duansanniu
 * @create 2019-09-15 19:32 下午
 */
@Controller
@RequestMapping("/leader")
@Api("用于领导的API")
public class LeaderController {

    @Autowired
    private LeaderService leaderService;


    @PostMapping("/uploadCommonFile")
    @ResponseBody
    public ResponseEntity uploadCommonFile(MultipartFile file, HttpServletRequest request){
        try{
            if(file.isEmpty()){
                return new ResponseEntity(0,"你好调皮",null);
            }
            //获取上传文件的名字
            String name = file.getOriginalFilename();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) {
                path = new File("");
            }
            //路径
            File upload = new File(path.getAbsolutePath(),"static/commonFile/");
            if(!upload.exists()){
                upload.mkdirs();
            }
            File commonFile = new File(upload.getPath()+File.separator+name);
            file.transferTo(commonFile);

            //保存这个文件的所有信息
            Map<String,String> map = new HashMap<>();
            map.put("name",commonFile.getName());
            map.put("url",commonFile.getPath());
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            map.put("createTime",sdf.format(d));

            Integer num = leaderService.preserveCommonFileInfo(map);
            if(num >= 1){
                return new ResponseEntity(1,"上传成功",null);
            }else {
                return new ResponseEntity(1,"上传失败",null);
            }
        }catch(Exception e){
            return new ResponseEntity(0,"上传失败",null);
        }
    }
}
