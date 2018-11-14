package com.balance.architecture.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Result;
import com.balance.utils.JwtUtils;
import com.balance.utils.ResultUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorityInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = httpServletRequest.getHeader(JwtUtils.TOKEN_NAME);
        if(null != token){
            Boolean verifyJwtResult = JwtUtils.verifyJwt(token);
            if(verifyJwtResult){
                return true;
            }
        }
        Result result = ResultUtils.reLogin("re-login");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
