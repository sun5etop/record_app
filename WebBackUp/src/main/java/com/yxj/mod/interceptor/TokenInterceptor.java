package com.yxj.mod.interceptor;

import com.yxj.mod.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Token 拦截器
 */
@Component
@Log4j2
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisService redisService;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return true;
//        }
//
//        response.setCharacterEncoding("utf-8");
//
//        String token = request.getHeader("Authorization");
//        log.info("token:"+token);
//        if (token != null) {
//            boolean result = TokenUtil.verify(token);
//            if (result) {
//                log.info("通过拦截器");
//                return true;
//            }
//        }
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        PrintWriter out = null;
//        try {
//            JSONObject json = new JSONObject();
//            json.put("message", "认证失败，未通过拦截器");
//            json.put("resultCode", "401");
//            response.getWriter().append(json.toJSONString());
//            log.info("认证失败，未通过拦截器");
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(500);
//            return false;
//        }
//        return false;
//    }
//这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //不拦截路径（登录路径等等）
    if (request.getMethod().equals("OPTIONS")) {
        return true;
    }

    List<String> asList = Arrays.asList("/account/login", "/account/register");

    String uri = request.getRequestURI();
    //1.设置放行路径
    if(asList.contains(uri)){
        return true;
    }

    //2.拿到请求头里面的token（如果是第一次登录，那么是没有请求头的）
    String token = request.getHeader("u-token");
    if(token==null){
        response.setContentType("application/json; charset=utf-8");

        //2.1 拦截请求并返回信息给前台 （前台后置拦截器就是根据这里面返回的json数据，来判读并跳转到登录界面）
//        JSONObject json = new JSONObject();
//        json.put("message", "NoUser");
//        json.put("resultCode", "401");
//        response.getWriter().append(json.toString());
        response.getWriter().print("{\"success\":false,\"msg\":\"NoUser\"}");
        return false;
    }

    //3、如果有token，那么就根据这个token从redis查询登录用户信息，如果redis里面还没过期，那么就正常放行，没有就进行拦截，并返回信息，叫他重新登录
     Object tokenUser =redisService.getValue(token);
    if(tokenUser==null){
        response.setContentType("application/json; charset=utf-8");

        response.getWriter().print("{\"success\":false,\"msg\":\"NoUser\"}");
        return false;
    }

    //4.如果没有过期，那么就重新将token和登录用户信息存到redis
    redisService.setValueWithExpiry(token, tokenUser, 60*30, TimeUnit.SECONDS);

    return true;
}
}
