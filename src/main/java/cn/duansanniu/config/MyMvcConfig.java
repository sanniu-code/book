package cn.duansanniu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author duansanniu
 * @create 2019-09-15 10:10 上午
 */
@Configuration
@EnableWebMvc
@Component
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private MyInterceptors myInterceptors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedMethods("POST","GET")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowCredentials(true);

        super.addCorsMappings(registry);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptors);
        super.addInterceptors(registry);
    }

    @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //用于swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //用于领导上传文件
        registry.addResourceHandler("/commonFile/**")
                .addResourceLocations("classpath:/static/commonFile/");
        //这是学生上传的文件
        registry.addResourceHandler("/studentFile/**")
                .addResourceLocations("classpath:/static/studentFile/");

        //这是老师上传的文件
        registry.addResourceHandler("/teacherFile/**")
                .addResourceLocations("classpath:/static/teacherFile/");
        super.addResourceHandlers(registry);
     }
}
