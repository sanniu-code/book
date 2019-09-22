package cn.duansanniu.config;

import cn.duansanniu.utils.ResponseEntity;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author duansanniu
 * @create 2019-09-15 10:19 上午
 */
@Component
public class MyInterceptors implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        JSONObject noShiro = JSONObject.fromObject(new ResponseEntity(3,"没有权限",null));

        //还未登录
        if(session.getAttribute("type")==null || session.getAttribute("userInfo")==null){
            JSONObject pleaseLogin = JSONObject.fromObject(new ResponseEntity(2,"请登录",null));
            response.getWriter().print(pleaseLogin.toString());
            return false;
        }


        //获取请求路径
        String uri = request.getRequestURI();
        //获取当前用户的身份
        Integer type = (Integer)session.getAttribute("type");
        switch(type){
            case 1:

                if(uri.startsWith("/student")) return true;

                response.getWriter().print(noShiro);
                return false;
            case 2:
                if(uri.startsWith("/student") || uri.startsWith("/teacher")) return true;
                response.getWriter().print(noShiro);
                return false;
            case 3:
                if(uri.startsWith("/student") || uri.startsWith("/teacher") || uri.startsWith("/leader")) return true;
                response.getWriter().print(noShiro);
                return false;
            default:
                response.getWriter().print(noShiro);
                return false;
        }



    }
}
